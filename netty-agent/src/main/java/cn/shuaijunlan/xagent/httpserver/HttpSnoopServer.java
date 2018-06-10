package cn.shuaijunlan.xagent.httpserver;

import cn.shuaijunlan.xagent.registry.Endpoint;
import cn.shuaijunlan.xagent.registry.EtcdRegistry;
import cn.shuaijunlan.xagent.registry.IRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 21:14 2018/4/30.
 */
public class HttpSnoopServer {
    private static Logger logger = LoggerFactory.getLogger(HttpSnoopServer.class);

    static final int PORT = 20000;
//    private static IRegistry registry = new EtcdRegistry(System.getProperty("etcd.url"));
//    public static List<Endpoint> endpoints;
//
//    static {
//        try {
//            endpoints = registry.find("com.alibaba.dubbo.performance.demo.provider.IHelloService");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static void main(String[] args) throws Exception {
        String type = System.getProperty("agent.type");
        if (type != null && type.equals("client")){

            // Configure the server.
            EventLoopGroup bossGroup = new EpollEventLoopGroup(1);
            EventLoopGroup workerGroup = new EpollEventLoopGroup(32);
            try {
                ServerBootstrap b = new ServerBootstrap();
                b.group(bossGroup, workerGroup)
                        .channel(EpollServerSocketChannel.class)
                        //保持长连接状态
                        .childOption(ChannelOption.SO_KEEPALIVE, true)
                        .childOption(ChannelOption.TCP_NODELAY, true)
                        .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                        .childHandler(new HttpSnoopServerInitializer());

                ChannelFuture ch = b.bind(PORT).sync();
                if (ch.isSuccess()){
                    logger.info("Http server start on port :{}", PORT );
                }

                ch.channel().closeFuture().sync();
            } finally {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        }else {
            // Configure the server.
            EventLoopGroup bossGroup = new NioEventLoopGroup(1);
            EventLoopGroup workerGroup = new NioEventLoopGroup(8);
            try {
                ServerBootstrap b = new ServerBootstrap();
                b.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new HttpSnoopServerInitializer());

                Channel ch = b.bind(PORT).sync().channel();

                System.err.println("Open your web browser and navigate to " +
                        "http" + "://127.0.0.1:" + PORT + '/');

                ch.closeFuture().sync();
            } finally {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        }

    }
}
