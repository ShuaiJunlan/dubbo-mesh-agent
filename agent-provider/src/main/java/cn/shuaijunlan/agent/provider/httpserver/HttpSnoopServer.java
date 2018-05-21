package cn.shuaijunlan.agent.provider.httpserver;

import cn.shuaijunlan.agent.provider.registry.EtcdRegistry;
import cn.shuaijunlan.agent.provider.registry.IRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 21:14 2018/4/30.
 */
public final class HttpSnoopServer {

    static final Integer PORT = Integer.valueOf(System.getProperty("agent.port"));
//    static final Integer PORT = 10000;

    public static void main(String[] args) throws Exception {
        //register and start agent server
        IRegistry registry = new EtcdRegistry(System.getProperty("etcd.url"));
        // Configure the server.
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    //保持长连接状态
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new HttpSnoopServerInitializer());

            ChannelFuture ch = b.bind(PORT).sync();
            if (ch.isSuccess()){
                System.out.println("agent server start!");
            }

            ch.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
