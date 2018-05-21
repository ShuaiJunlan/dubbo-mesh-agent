package cn.shuaijunlan.xagent.httpserver;

import cn.shuaijunlan.xagent.transport.client.AgentClient;
import cn.shuaijunlan.xagent.transport.client.AgentClientManager;
import cn.shuaijunlan.xagent.transport.client.ResultMap;
import cn.shuaijunlan.xagent.transport.support.MessageRequest;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
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
    public Channel channel;
    public HttpServerHandler(){
        channel = AgentClientManager.getChannel();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof FullHttpRequest && channel != null){
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
//                    Integer integer = str.hashCode();
//
//                    try {
//                        Thread.sleep(50);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }

//                    AgentClient client = AgentClientManager.getAgentClientInstance();
                    Integer integer = sendData(str, channel);
                    AgentClientManager.addChannel(channel);

                    FullHttpResponse response = new DefaultFullHttpResponse(
                            HTTP_1_1,
                            OK,
                            Unpooled.copiedBuffer(integer.toString(), CharsetUtil.UTF_8)
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

    public Integer sendData(String param, Channel channel) {
        Long begin = System.currentTimeMillis();
        Long num;
        if (channel == null || (!channel.isActive())){
            logger.info("channel get error");
            return 0;
        }else {
            num = ResultMap.COUNT.getAndIncrement();
            MessageRequest messageRequest = new MessageRequest();
            messageRequest.setId(num);
            messageRequest.setInterfaceName("com.alibaba.performance.dubbomesh.provider.IHelloService");
            messageRequest.setMethod("hash");
            messageRequest.setParameterTypesString("Ljava/lang/String;");
            messageRequest.setParameter(param);
            channel.writeAndFlush(messageRequest).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {

                }
            });
        }
        while (ResultMap.RESULT_MAP.get(num) == null){

        }
        Long end = System.currentTimeMillis();
        logger.info("Send data:{} spending time {}ms",num, end-begin);
        return ResultMap.RESULT_MAP.remove(num);
    }
}
