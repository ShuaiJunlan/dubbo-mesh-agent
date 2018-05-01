package cn.shuaijunlan.xagent.transport;

import cn.shuaijunlan.xagent.transport.support.MessageResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 21:46 2018/4/28.
 */
public class AgentClientHandler extends SimpleChannelInboundHandler<MessageResponse> {
    public AgentClientHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageResponse messageResponse) throws Exception {
        System.out.println("client 接收数据:" + messageResponse.toString());
    }
}
