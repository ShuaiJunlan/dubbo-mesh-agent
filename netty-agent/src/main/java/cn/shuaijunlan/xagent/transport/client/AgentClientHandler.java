package cn.shuaijunlan.xagent.transport.client;

import cn.shuaijunlan.xagent.transport.support.MessageResponse;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
//@ChannelHandler.Sharable
public class AgentClientHandler extends SimpleChannelInboundHandler<MessageResponse> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AgentClientHandler.class);

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
        // if (LOGGER.isInfoEnabled()){
        //     LOGGER.info(messageResponse.toString());
        // }
        Promise<Integer> promise = ResultMap.PROMISE_CONCURRENT_HASH_MAP.remove(messageResponse.getId());
        if (promise != null){
            promise.trySuccess(messageResponse.getHash());
        }
        // ResultMap.RESULT_MAP.put(messageResponse.getId(), messageResponse.getHash());
    }
}
