package cn.shuaijunlan.xagent.transport;

import cn.shuaijunlan.xagent.transport.support.MessageRequest;
import cn.shuaijunlan.xagent.transport.support.MessageResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 21:47 2018/4/28.
 */
public class AgentServerHandler extends SimpleChannelInboundHandler<MessageRequest> {
    int i = 0;

    public AgentServerHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageRequest messageRequest) throws Exception {
//        if (!messageRequest.getMethod().equals(String.valueOf((i++)))){
//            return;
//        }
//        System.out.println("server 接收数据"+ messageRequest.getMethod() + "::" + (i++)+":"+messageRequest.toString());
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setHash(messageRequest.getParameter().hashCode());
        channelHandlerContext.writeAndFlush(messageResponse);
//        System.out.println("server 发送数据:" +messageResponse.toString());
    }

}
