package cn.shuaijunlan.xagent.httpserver;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 14:27 2018/5/6.
 */
public class HttpServerHandler extends ChannelInboundHandlerAdapter {
    private Logger logger = LoggerFactory.getLogger(ChannelInboundHandlerAdapter.class);

    // private AsyncHttpClient asyncHttpClient = org.asynchttpclient.Dsl.asyncHttpClient();
    // private static AtomicInteger atomicInteger = new AtomicInteger(0);
    // private String url = Constants.URLS[Constants.CONNECTION_COUNT.getAndIncrement()%Constants.URLS.length];

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object obj) throws Exception {

        super.userEventTriggered(ctx, obj);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof FullHttpRequest) {

            ctx.executor().execute(() -> {
                FullHttpRequest request = (FullHttpRequest) msg;
                String[] tmp = null;
                if (request.method().name().equals("GET")) {
                    tmp = request.uri().split("&parameter=");
                } else if (request.method().name().equals("POST")) {
                    ByteBuf content = request.content();
                    if (content.isReadable()) {
                        tmp = content.toString(CharsetUtil.UTF_8).split("&parameter=");
                    }
                    content.release();
                }
                String str = "";
                if (tmp != null && tmp.length > 1) {
                    str = tmp[1];
                }

                FullHttpResponse response = new DefaultFullHttpResponse(
                        HTTP_1_1,
                        OK,
                        Unpooled.copiedBuffer(msg.toString(), CharsetUtil.UTF_8)
                );
                response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
                boolean keepAlive = HttpUtil.isKeepAlive(request);
                if (keepAlive) {
                    response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
                    response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                    ctx.writeAndFlush(response);
                } else {
                    ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
                }
            });

        } else {
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HTTP_1_1,
                    BAD_REQUEST
            );
            ctx.write(response).addListener(ChannelFutureListener.CLOSE);
            logger.info("Wrong response!");
            //释放内存
            ReferenceCountUtil.release(msg);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws IOException {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelInactive();
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelUnregistered();
    }
}
