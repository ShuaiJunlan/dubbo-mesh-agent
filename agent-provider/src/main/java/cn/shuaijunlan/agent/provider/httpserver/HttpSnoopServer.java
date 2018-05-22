package cn.shuaijunlan.agent.provider.httpserver;

import cn.shuaijunlan.agent.provider.registry.EtcdRegistry;
import cn.shuaijunlan.agent.provider.registry.IRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 21:14 2018/4/30.
 */
public final class HttpSnoopServer {

    static final Integer PORT = Integer.valueOf(System.getProperty("agent.port"));
//    static final Integer PORT = 10000;

    public static void main(String[] args) throws Exception {

        // Configure the server.
        EventLoopGroup bossGroup = new EpollEventLoopGroup(1);
        EventLoopGroup workerGroup = new EpollEventLoopGroup(8);
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(EpollServerSocketChannel.class)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.ALLOCATOR, UnpooledByteBufAllocator.DEFAULT)
                    .childHandler(new HttpSnoopServerInitializer());

            ChannelFuture ch = b.bind(PORT).sync();
            if (ch.isSuccess()){
                System.out.println("agent server start!");
                //register service
                IRegistry registry = new EtcdRegistry(System.getProperty("etcd.url"));
            }

            ch.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
