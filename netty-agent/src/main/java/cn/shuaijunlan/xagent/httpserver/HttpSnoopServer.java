package cn.shuaijunlan.xagent.httpserver;

import cn.shuaijunlan.xagent.httpserver.HttpSnoopServerInitializer;
import cn.shuaijunlan.xagent.registry.EtcdRegistry;
import cn.shuaijunlan.xagent.registry.IRegistry;
import cn.shuaijunlan.xagent.transport.server.AgentServer;
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

    static final int PORT = 20000;

    public static void main(String[] args) throws Exception {
        String type = System.getProperty("agent.type");
        if (type != null && type.equals("client")){
            // Configure the server.
            EventLoopGroup bossGroup = new EpollEventLoopGroup();
            EventLoopGroup workerGroup = new EpollEventLoopGroup();
            try {
                ServerBootstrap b = new ServerBootstrap();
                b.group(bossGroup, workerGroup)
                        .channel(EpollServerSocketChannel.class)
                        //通过NoDelay禁用Nagle,使消息立即发出去，不用等待到一定的数据量才发出去
                        .option(ChannelOption.TCP_NODELAY, true)
                        //保持长连接状态
                        .childOption(ChannelOption.SO_KEEPALIVE, true)
                        .childHandler(new HttpSnoopServerInitializer());

                ChannelFuture ch = b.bind(PORT).sync();
                if (ch.isSuccess()){
                    System.out.println("Http server start!");
                }

                ch.channel().closeFuture().sync();
            } finally {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        }else if (type != null && type.equals("server")) {
            //register and start agent server
            IRegistry registry = new EtcdRegistry(System.getProperty("etcd.url"));
            int port = Integer.valueOf(System.getProperty("agent.port"));
            AgentServer server = new AgentServer();
            server.start(port);
        } else {
            // Configure the server.
            EventLoopGroup bossGroup = new NioEventLoopGroup(4);
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
