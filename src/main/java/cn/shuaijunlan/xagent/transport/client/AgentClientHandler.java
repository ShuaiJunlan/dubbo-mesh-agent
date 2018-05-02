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
    public static LinkedList<MessageResponse> responseLinkedList = new LinkedList<>();
    public static BlockingQueue<MessageResponse> messageResponseBlockingQueue = new LinkedBlockingDeque<>();
    public AgentClientHandler() {
    }

    public AgentClientHandler(LinkedList<MessageResponse> messageResponses, Long length) {
        this.arrayList = messageResponses;
//        this.atomicLong = new AtomicLong(length);
    }
    public AgentClientHandler(Object lock) {
        this.lock = lock;
//        this.atomicLong = new AtomicLong(length);
    }
    public void setLength(Integer length){
        atomicLong = new AtomicLong(length);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageResponse messageResponse) throws Exception {

        synchronized (lock){
            messageResponseBlockingQueue.put(messageResponse);
            responseLinkedList.push(messageResponse);
            value = messageResponse.getHash();
//            arrayList.add(messageResponse);
//        atomicLong.decrementAndGet();
            lock.notify();
        }

    }
}
