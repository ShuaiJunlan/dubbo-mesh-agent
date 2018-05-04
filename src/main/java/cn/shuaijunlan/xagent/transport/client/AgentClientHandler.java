package cn.shuaijunlan.xagent.transport.client;

import cn.shuaijunlan.xagent.transport.support.MessageResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 21:46 2018/4/28.
 */
public class AgentClientHandler extends SimpleChannelInboundHandler<MessageResponse> {

    public Object lock;
    public Integer value;

    public AtomicLong atomicLong ;
    public LinkedList<MessageResponse> arrayList;
    public AgentClientHandler() {
    }

    public AgentClientHandler(LinkedList<MessageResponse> messageResponses, Long length) {
        this.arrayList = messageResponses;
    }
    public AgentClientHandler(Object lock) {
        this.lock = lock;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageResponse messageResponse) throws Exception {
        synchronized (lock){
            value = messageResponse.getHash();
            lock.notify();
        }
    }
}
