package cn.shuaijunlan.agent.provider.dubbo;


import cn.shuaijunlan.agent.provider.dubbo.model.ChannelContextHolder;
import cn.shuaijunlan.agent.provider.dubbo.model.RpcResponse;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @author Junlan
 */
public class RpcClientHandlerT extends SimpleChannelInboundHandler<RpcResponse> {
    private Logger logger = LoggerFactory.getLogger(RpcClientHandlerT.class);
//    private static AtomicInteger atomicInteger = new AtomicInteger(0);
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object obj) throws Exception {
//        if (obj instanceof IdleStateEvent) {
//            IdleStateEvent event = (IdleStateEvent) obj;
//            if (IdleState.WRITER_IDLE.equals(event.state())) {
////                logger.info("Closing an idle channel: {}!", atomicInteger.incrementAndGet());
//                ctx.channel().close();
////            }
////        } else {
////            super.userEventTriggered(ctx, obj);
////        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse response) {
        String requestId = response.getRequestId();
        logger.info("Get response ID: {}!", requestId);
        String integer = new String(response.getBytes()).replaceFirst("\r\n", "");
//                String integer = String.valueOf(str.hashCode());

        FullHttpResponse httpResponse = new DefaultFullHttpResponse(
                HTTP_1_1,
                OK,
                Unpooled.copiedBuffer(integer, CharsetUtil.UTF_8)
        );
        httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
        httpResponse.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, httpResponse.content().readableBytes());
        httpResponse.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);

        ChannelContextHolder.get(String.valueOf(requestId)).writeAndFlush(httpResponse);
//        RpcFuture future = RpcRequestHolder.get(requestId);
//        if(null != future){
//            RpcRequestHolder.remove(requestId);
//            future.done(response);
//        }
    }
}
