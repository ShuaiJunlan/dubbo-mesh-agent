package cn.shuaijunlan.xagent.dubbo.client;

import cn.shuaijunlan.xagent.dubbo.RpcClientInitializer;
import cn.shuaijunlan.xagent.dubbo.model.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 12:57 2018/5/7.
 */
public class Client {
    public EventLoopGroup eventLoopGroup;
    public Bootstrap bootstrap;
    public Channel channel;

    public void start() {
//        eventLoopGroup = new EpollEventLoopGroup(4);
        eventLoopGroup = new NioEventLoopGroup(4);

        bootstrap = new Bootstrap()
                .group(eventLoopGroup)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.ALLOCATOR, UnpooledByteBufAllocator.DEFAULT)
                .channel(NioSocketChannel.class)
                .handler(new ClientInitializer());
//        int port = Integer.valueOf(System.getProperty("dubbo.protocol.port"));
        int port = 20889;
        try {
            channel = bootstrap.connect("127.0.0.1", port).sync().channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public RpcResponse invoke(String interfaceName, String method, String parameterTypesString, String parameter) throws IOException {

        if (channel == null || !channel.isActive()){
            return null;
        }
        RpcInvocation invocation = new RpcInvocation();
        invocation.setMethodName(method);
        invocation.setAttachment("path", interfaceName);
        invocation.setParameterTypes(parameterTypesString);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(out));
        JsonUtils.writeObject(parameter, writer);
        invocation.setArguments(out.toByteArray());
        out.close();
        writer.close();

        Request request = new Request();
        request.setVersion("2.0.0");
        request.setTwoWay(true);
        request.setData(invocation);


        channel.writeAndFlush(request);

        return RequestHolder.get(String.valueOf(request.getId()));
    }
}
