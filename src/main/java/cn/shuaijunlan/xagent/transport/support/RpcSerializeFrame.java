package cn.shuaijunlan.xagent.transport.support;

import io.netty.channel.ChannelPipeline;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 11:12 2018/4/29.
 */
public interface RpcSerializeFrame {
    void select(RpcSerializeProtocol protocol, ChannelPipeline pipeline);
}
