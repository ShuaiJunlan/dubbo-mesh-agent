package cn.shuaijunlan.xagent.dubbo.client;

import cn.shuaijunlan.xagent.dubbo.DubboRpcDecoder;
import cn.shuaijunlan.xagent.dubbo.DubboRpcEncoder;
import cn.shuaijunlan.xagent.dubbo.RpcClientHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 12:58 2018/5/7.
 */
public class ClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new DubboRpcEncoder());
        pipeline.addLast(new DubboRpcDecoder());
        pipeline.addLast(new ClientHandler());
    }
}
