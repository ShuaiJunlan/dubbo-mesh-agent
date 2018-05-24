package cn.shuaijunlan.agent.provider.dubbo;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author Junlan
 */
public class RpcClientInitializer extends ChannelInitializer<SocketChannel> {
    private Logger logger = LoggerFactory.getLogger(RpcClientInitializer.class);
    @Override
    protected void initChannel(SocketChannel socketChannel) {
        logger.info("RpcClientInitializer initChannel!");
        ChannelPipeline pipeline = socketChannel.pipeline();
        //设置连接空闲时间
        pipeline.addLast(new IdleStateHandler(0, 20, 0, TimeUnit.SECONDS));
        pipeline.addLast(new DubboRpcEncoder());
        pipeline.addLast(new DubboRpcDecoder());
        pipeline.addLast(new RpcClientHandler());
    }
}
