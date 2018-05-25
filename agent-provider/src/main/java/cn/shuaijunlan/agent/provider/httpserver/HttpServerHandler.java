package cn.shuaijunlan.agent.provider.httpserver;

import cn.shuaijunlan.agent.provider.dubbo.RpcClient;
import cn.shuaijunlan.agent.provider.dubbo.RpcClientHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

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

//    private static AtomicInteger atomicInteger = new AtomicInteger(0);
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object obj) throws Exception {
        if (obj instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) obj;
            if (IdleState.READER_IDLE.equals(event.state())) {
//                logger.info("Closing an idle channel: {}!", atomicInteger.incrementAndGet());
                ctx.channel().close();
            }
        } else {
            super.userEventTriggered(ctx, obj);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof FullHttpRequest){
            FullHttpRequest req = (FullHttpRequest) msg;
            // 将耗时任务交给任务线程池处理
            ctx.executor().execute(() -> {
//                long start = System.currentTimeMillis();
                //执行远程调用
                String[] tmp = req.uri().split("&parameter=");
                String str = "";
                Object result = new Object();
                if (tmp.length > 1){
                    str = tmp[1];
                }
                try {
                    logger.info("HttpServerHandler");
                    result = rpcClient.invoke("com.alibaba.dubbo.performance.demo.provider.IHelloService",
                            "hash",
                            "Ljava/lang/String;",
                            str);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String integer = new String((byte[]) result).replaceFirst("\r\n", "");
//                String integer = String.valueOf(str.hashCode());

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
//                long end = System.currentTimeMillis();
//                logger.info("Get response from provider service spending {}ms!", end-start);
            });
        }else {
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HTTP_1_1,
                    BAD_REQUEST
            );
            ctx.write(response).addListener(ChannelFutureListener.CLOSE);
            logger.error("Wrong response!");
        }
        //释放内存
        ReferenceCountUtil.release(msg);
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
