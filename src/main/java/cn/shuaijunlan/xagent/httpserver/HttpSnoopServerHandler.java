package cn.shuaijunlan.xagent.httpserver;

import cn.shuaijunlan.xagent.dubbo.RpcClient;
import cn.shuaijunlan.xagent.test.Test;
import cn.shuaijunlan.xagent.transport.client.AgentClient;
import cn.shuaijunlan.xagent.transport.client.AgentClientHandler;
import cn.shuaijunlan.xagent.transport.client.AgentClientManager;
import cn.shuaijunlan.xagent.transport.support.MessageResponse;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;
import io.netty.util.CharsetUtil;


import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.*;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 21:16 2018/4/30.
 */
public class HttpSnoopServerHandler extends SimpleChannelInboundHandler<Object> {

    private HttpRequest request;
    /** Buffer that stores the response content */
    StringBuffer stringBuffer = new StringBuffer();

    private AgentClient client;

    public HttpSnoopServerHandler(){
        client = new AgentClient("127.0.0.1", 1234);
        client.start();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof HttpRequest) {
            stringBuffer.setLength(0);
            HttpRequest request = this.request = (HttpRequest) msg;

            if (HttpUtil.is100ContinueExpected(request)) {
                send100Continue(ctx);
            }
        }
        if (msg instanceof HttpContent) {
            HttpContent httpContent = (HttpContent) msg;

            ByteBuf content = httpContent.content();
            if (content.isReadable()) {
                stringBuffer.append(content.toString(CharsetUtil.UTF_8));
            }
            if (msg instanceof LastHttpContent) {

                LastHttpContent trailer = (LastHttpContent) msg;

                //执行远程调用
                String[] tmp = stringBuffer.toString().split("&parameter=");
                String str = "";
                if (tmp.length > 1){
                    str = tmp[1];
                }
//                AgentClient client = AgentClientManager.getAgentClientInstance();
                Integer integer = client.sendData(str);
//                AgentClientManager.putOne(client);
                Test.agentClients.add(client);
                Test.channelHandlerContexts.add(ctx);
                System.out.println("Test.agentClients:" + Test.agentClients.size());
                System.out.println("Test.channelHandlerContexts:" + Test.channelHandlerContexts.size());
                writeResponse(trailer, ctx, integer.toString(), request);

            }
        }
    }

    public void writeResponse(HttpObject currentObj, ChannelHandlerContext ctx, String msg, HttpRequest request) {
        // Decide whether to close the connection or not.
        boolean keepAlive = HttpUtil.isKeepAlive(request);
        // Build the response object.
        FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1, currentObj.decoderResult().isSuccess()? OK : BAD_REQUEST,
                Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8));

        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");

        if (keepAlive) {
            // Add 'Content-Length' header only for a keep-alive connection.
            response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
            // Add keep alive header as per:
            // - http://www.w3.org/Protocols/HTTP/1.1/draft-ietf-http-v11-spec-01.html#Connection
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        // Write the response.
        ctx.write(response);

        if (!keepAlive){
            ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
    }

    private static void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, CONTINUE);
        ctx.write(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    public class Entry {
        private ChannelHandlerContext context;
        private String parameter;
        private HttpRequest request;
        private LastHttpContent content;
        public Entry(ChannelHandlerContext context, String parameter, HttpRequest request, LastHttpContent content){
            this.context = context;
            this.parameter = parameter;
            this.request = request;
            this.content = content;
        }

        public ChannelHandlerContext getContext() {
            return context;
        }

        public String getParameter() {
            return parameter;
        }

        public HttpRequest getRequest() {
            return request;
        }

        public LastHttpContent getContent() {
            return content;
        }
    }
}
