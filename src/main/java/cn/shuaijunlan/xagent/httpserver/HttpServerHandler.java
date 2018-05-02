package cn.shuaijunlan.xagent.httpserver;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.EmptyByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MemoryAttribute;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpMethod.POST;


/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 15:51 2018/4/28.
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    private static Logger logger = LoggerFactory.getLogger(HttpServerHandler.class);
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {
        if (httpObject instanceof HttpRequest) {
            System.out.println("request");
            HttpRequest httpRequest = (HttpRequest) httpObject;
//            getRequestParams(channelHandlerContext, httpRequest);
            //对不同的路径进行处理
            URI uri = new URI(httpRequest.uri());
            if ("/".equals(uri.getPath())) {
                System.out.println("请求favicon.ico");
            }
            if (logger.isInfoEnabled()){
                logger.info("Request method:{}, uri:{}", httpRequest.method().name(), uri.getPath());
            }
//            ByteBuf requestContent = httpRequest.;
//            System.out.println(requestContent.toString(CharsetUtil.UTF_8));
            // 返回的内容
            ByteBuf content = Unpooled.copiedBuffer("Hello World", CharsetUtil.UTF_8);
            // http的响应
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            //响应头设置
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
            // 把响应内容写回到客户端
            channelHandlerContext.writeAndFlush(response);
        }else if (httpObject instanceof HttpContent){
            HttpContent httpContent = (HttpContent) httpObject;
            ByteBuf content = httpContent.content();
            System.out.println(content.toString(CharsetUtil.UTF_8));
        }

    }
}
