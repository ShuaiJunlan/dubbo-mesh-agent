package cn.shuaijunlan.xagent.dubbo;

import cn.shuaijunlan.xagent.dubbo.DubboRpcDecoder;
import cn.shuaijunlan.xagent.dubbo.DubboRpcEncoder;
import cn.shuaijunlan.xagent.dubbo.RpcClientHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class RpcClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new DubboRpcEncoder());
        pipeline.addLast(new DubboRpcDecoder());
        pipeline.addLast(new RpcClientHandler());
    }
}
