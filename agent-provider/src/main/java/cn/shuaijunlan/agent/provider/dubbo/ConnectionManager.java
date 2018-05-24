package cn.shuaijunlan.agent.provider.dubbo;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Junlan
 */
public class ConnectionManager {
    private Logger logger = LoggerFactory.getLogger(ConnectionManager.class);
    private static EventLoopGroup eventLoopGroup = new EpollEventLoopGroup(256);
    private static AtomicInteger atomicInteger = new AtomicInteger(0);
//    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);

    private static Bootstrap bootstrap;

    private Channel channel;
    /**
     * must be static
     */
    private static Object lock = new Object();

    public ConnectionManager() {
    }

    public Channel getChannel() throws Exception {
        if (null != channel) {
            return channel;
        }

        if (null == bootstrap) {
            synchronized (lock) {
                if (null == bootstrap) {
                    initBootstrap();
                }
            }
        }

        if (null == channel) {

            synchronized (lock){
                if (null == channel){
//                    int port = Integer.valueOf(System.getProperty("dubbo.protocol.port"));
                    int port = 20880;
                    channel = bootstrap.connect("127.0.0.1", port).sync().channel();
//                    logger.info("Create a new channel, the {}th one!", atomicInteger.incrementAndGet());
                }
            }
        }

        return channel;
    }

    public void initBootstrap() {
        logger.info("ConnectionManager initBootstrap");

        bootstrap = new Bootstrap()
                .group(eventLoopGroup)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .channel(EpollSocketChannel.class)
                .handler(new RpcClientInitializer());
    }
}
