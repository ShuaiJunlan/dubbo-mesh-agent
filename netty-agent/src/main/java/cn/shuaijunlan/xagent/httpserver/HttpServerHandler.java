package cn.shuaijunlan.xagent.httpserver;

import cn.shuaijunlan.xagent.registry.Endpoint;
import cn.shuaijunlan.xagent.registry.EtcdRegistry;
import cn.shuaijunlan.xagent.registry.IRegistry;
import cn.shuaijunlan.xagent.transport.client.AgentClient;
import cn.shuaijunlan.xagent.transport.client.AgentClientManager;
import cn.shuaijunlan.xagent.transport.client.ResultMap;
import cn.shuaijunlan.xagent.transport.support.MessageRequest;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.Promise;
import org.apache.commons.lang3.RandomStringUtils;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;
import java.util.Random;
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
    private AsyncHttpClient asyncHttpClient = org.asynchttpclient.Dsl.asyncHttpClient();
    private AtomicInteger atomicInteger = new AtomicInteger(0);
    private String url = Constants.URLS[2];


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
//                    Integer integer = getHash(str, endpoints.get(2).getHost(), endpoints.get(2).getPort()).getHash();
//                    Integer integer = getHash(str, "127.0.0.1", 10000).getHash();
//                    System.out.println("Integer:" + integer);

//                    Integer integer = sendData(str, channel);
//                    AgentClientManager.addChannel(channel);

                    /////////////////////////////////////////////////////////////
//                    FullHttpResponse response = new DefaultFullHttpResponse(
//                            HTTP_1_1,
//                            OK,
//                            Unpooled.copiedBuffer(String.valueOf(str.hashCode()), CharsetUtil.UTF_8)
//                    );
//
//                    response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
//                    boolean keepAlive = HttpUtil.isKeepAlive(req);
//                    if (keepAlive) {
//                        response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
//                        response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
//                        ctx.writeAndFlush(response);
//                    } else {
//                        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
//                    }
                    /////////////////////////////////////////////////////////////

                    ////////////////////////////////////////////////////////////////////////////
//                    logger.info("Request url:{}", url);
//                    Result result = new Result();

                    org.asynchttpclient.Request request = org.asynchttpclient.Dsl.post(url)
                            .addFormParam("interface", "com.alibaba.dubbo.performance.demo.provider.IHelloService")
                            .addFormParam("method", "hash")
                            .addFormParam("parameterTypesString", "Ljava/lang/String;")
                            .addFormParam("parameter", str)
                            .build();
                    ListenableFuture<Response> responseFuture = asyncHttpClient.executeRequest(request);

                    Runnable callback = () -> {
                        try {
                            // 获取远程结果
                            String value = responseFuture.get().getResponseBody();

                            FullHttpResponse response = new DefaultFullHttpResponse(
                                    HTTP_1_1,
                                    OK,
                                    Unpooled.copiedBuffer(value, CharsetUtil.UTF_8)
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


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    };
                    responseFuture.addListener(callback, null);
                    ////////////////////////////////////////////////////////////////////////////
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

            channel.writeAndFlush(messageRequest);
        }
        while (ResultMap.RESULT_MAP.get(num) == null){

        }
        Long end = System.currentTimeMillis();
        logger.info("Send data:{} spending time {}ms",num, end-begin);
        return ResultMap.RESULT_MAP.remove(num);
    }

    public Result getHash(String str, String host, Integer port){

        String url = "http://" + host + ":" + port;

        Result result = new Result();

        org.asynchttpclient.Request request = org.asynchttpclient.Dsl.post(url)
                .addFormParam("interface", "com.alibaba.dubbo.performance.demo.provider.IHelloService")
                .addFormParam("method", "hash")
                .addFormParam("parameterTypesString", "Ljava/lang/String;")
                .addFormParam("parameter", str)
                .build();
        ListenableFuture<Response> responseFuture = asyncHttpClient.executeRequest(request);

        Runnable callback = () -> {
            try {
                String value = responseFuture.get().getResponseBody();
                result.setHash(Integer.valueOf(value));
                System.out.println(result.getHash());

            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        responseFuture.addListener(callback, null);
        return result;
    }

    class Result{
        private int hash;

        public int getHash() {
            return hash;
        }

        public void setHash(int hash) {
            this.hash = hash;
        }
    }
}
