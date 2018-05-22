package cn.shuaijunlan.agent.provider.httpserver;

import cn.shuaijunlan.agent.provider.dubbo.RpcClient;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 14:27 2018/5/6.
 */
public class HttpServerHandler extends ChannelInboundHandlerAdapter {
    private Logger logger = LoggerFactory.getLogger(ChannelInboundHandlerAdapter.class);
    private RpcClient rpcClient = new RpcClient();


    public HttpServerHandler(){

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof FullHttpRequest){
            FullHttpRequest req = (FullHttpRequest) msg;
            ByteBuf content = req.content();
            if (content.isReadable()) {
                // 将耗时任务交给任务线程池处理
                ctx.executor().execute(() -> {
                    //执行远程调用
                    String[] tmp = content.toString(CharsetUtil.UTF_8).split("&parameter=");
                    content.release();
                    String str = "";
                    if (tmp.length > 1){
                        str = tmp[1];
                    }

                    Object result = null;
                    try {
                        result = rpcClient.invoke("com.alibaba.dubbo.performance.demo.provider.IHelloService",
                                "hash",
                                "Ljava/lang/String;",
                                str);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String integer = "";

                    if (result != null){
                        integer = new String((byte[]) result).replaceFirst("\n", "");
                    }

                    FullHttpResponse response = new DefaultFullHttpResponse(
                            HTTP_1_1,
                            OK,
                            Unpooled.copiedBuffer(integer, CharsetUtil.UTF_8)
                    );

                    response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
                    boolean keepAlive = HttpUtil.isKeepAlive(req);
                    if (keepAlive) {
                        response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
                        response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                        ctx.writeAndFlush(response);
                    } else {
                        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
                    }
                });

            }
        }else {
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HTTP_1_1,
                    BAD_REQUEST
            );
            ctx.write(response).addListener(ChannelFutureListener.CLOSE);
            logger.info("Wrong response!");
        }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
