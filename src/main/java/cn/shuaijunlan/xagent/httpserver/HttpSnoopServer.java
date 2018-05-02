package cn.shuaijunlan.xagent.httpserver;

import cn.shuaijunlan.xagent.transport.server.AgentServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 21:14 2018/4/30.
 */
public final class HttpSnoopServer {

    static final int PORT = 20000;

    public static void main(String[] args) throws Exception {


        // Configure the server.
        EventLoopGroup bossGroup = new NioEventLoopGroup(4);
        EventLoopGroup workerGroup = new NioEventLoopGroup(4);
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
//                    .handler(new LoggingHandler(LogLevel.ERROR))
                    .childHandler(new HttpSnoopServerInitializer());

            Channel ch = b.bind(PORT).sync().channel();

            System.err.println("Open your web browser and navigate to " +
                     "http" + "://127.0.0.1:" + PORT + '/');

            //start agent server
            AgentServer server = new AgentServer();
            server.start(1234);

            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }
}
