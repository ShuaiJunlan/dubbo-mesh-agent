package cn.shuaijunlan.xagent.transport;

import cn.shuaijunlan.xagent.transport.support.MessageResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 21:46 2018/4/28.
 */
public class AgentClientHandler extends SimpleChannelInboundHandler<MessageResponse> {
    public AtomicLong atomicLong ;
    public LinkedList<MessageResponse> arrayList;
    public AgentClientHandler() {
    }

    public AgentClientHandler(LinkedList<MessageResponse> messageResponses, Long length) {
        this.arrayList = messageResponses;
        this.atomicLong = new AtomicLong(length);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageResponse messageResponse) throws Exception {
        arrayList.add(messageResponse);
        atomicLong.decrementAndGet();
        System.out.println(arrayList.size() + "::" + messageResponse.getHash());
    }
}
