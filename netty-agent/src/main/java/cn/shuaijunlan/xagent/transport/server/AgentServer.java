package cn.shuaijunlan.xagent.transport.server;

import cn.shuaijunlan.xagent.transport.support.kryo.KryoCodecUtil;
import cn.shuaijunlan.xagent.transport.support.kryo.KryoDecoder;
import cn.shuaijunlan.xagent.transport.support.kryo.KryoEncoder;
import cn.shuaijunlan.xagent.transport.support.kryo.KryoPoolFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 21:50 2018/4/28.
 */
public class AgentServer {
    public void start(int port) {
        EventLoopGroup bossGroup = new EpollEventLoopGroup(1);
        EventLoopGroup workGroup = new EpollEventLoopGroup(8);
        KryoCodecUtil util = new KryoCodecUtil(KryoPoolFactory.getKryoPoolInstance());
        EventExecutorGroup group = new DefaultEventExecutorGroup(16);
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap
                    .group(bossGroup, workGroup)
                    .channel(EpollServerSocketChannel.class)
                    //通过NoDelay禁用Nagle,使消息立即发出去，不用等待到一定的数据量才发出去
                    .option(ChannelOption.TCP_NODELAY, true)
                    //保持长连接状态
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline p = socketChannel.pipeline();
                            p.addLast(new IdleStateHandler(10, 0, 0));
///                            p.addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, -4, 0));
                            p.addLast(new KryoDecoder(util));
                            p.addLast(new KryoEncoder(util));
                            p.addLast(group, "handler", new AgentServerHandler());
                        }
                    });

            Channel ch = bootstrap.bind(port).sync().channel();

            System.out.println("------Server Start------");

            ch.closeFuture().sync();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
