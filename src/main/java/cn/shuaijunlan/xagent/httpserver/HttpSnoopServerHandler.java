package cn.shuaijunlan.xagent.httpserver;

import cn.shuaijunlan.xagent.dubbo.RpcClient;
import cn.shuaijunlan.xagent.transport.client.AgentClient;
import cn.shuaijunlan.xagent.transport.client.AgentClientHandler;
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
    private final StringBuilder buf = new StringBuilder();
    StringBuffer stringBuffer = new StringBuffer();


    public static BlockingQueue<Entry> queue = new ArrayBlockingQueue<>(20);

    AtomicLong atomicLong = new AtomicLong(0);

    public HttpSnoopServerHandler(){
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws InterruptedException {

        if (msg instanceof HttpRequest) {
            buf.setLength(0);
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
//                String str = content.toString(CharsetUtil.UTF_8);
                stringBuffer.append(content.toString(CharsetUtil.UTF_8));
            }
            if (msg instanceof LastHttpContent) {

                LastHttpContent trailer = (LastHttpContent) msg;

                //执行远程调用
                String[] tmp = stringBuffer.toString().split("&parameter=");
                Entry entry;
                String str ;
                if (tmp.length > 1){
                    str = tmp[1];
                    buf.append(tmp[1].hashCode());
                    entry = new Entry(ctx, buf.toString(), request, trailer);
                }else {
                    str = "";
                    buf.append("".hashCode());
                    entry = new Entry(ctx, buf.toString(), request, trailer);
                }
//                MessageResponse messageResponse = AgentClientHandler.messageResponseBlockingQueue.take();
//                if (atomicLong.get() < 1){
//                    queue.put(entry);
//                    atomicLong.incrementAndGet();
//                    System.out.println(atomicLong.get());
//                }else {
//                    System.out.println(atomicLong.get());
//                    queue.put(entry);
//                    while (!queue.isEmpty()){
//
//                        Entry entry1 = queue.take();
//                        writeResponse(entry1.getContent(), entry1.getContext(), entry1.getParameter(), entry1.getRequest());
////                        writeResponse(entry.getContent(), entry.getContext(), entry.getParameter(), entry.getRequest());
//                    }
//                }
//                queue.put(entry);
//                Entry entry1 = queue.take();
                //from agent
                LinkedList<MessageResponse> messageResponses = new LinkedList<>();
                Long length  = 1L;
                AgentClient client = new AgentClient("127.0.0.1", 1234, messageResponses, length);
                client.start();
                Integer integer = client.sendData(str);
                writeResponse(entry.getContent(), entry.getContext(), integer+"", entry.getRequest());
            }
        }
    }

    public static void writeResponse(HttpObject currentObj, ChannelHandlerContext ctx, String msg, HttpRequest request) {
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
