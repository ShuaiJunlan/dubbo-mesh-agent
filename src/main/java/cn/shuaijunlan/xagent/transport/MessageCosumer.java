package cn.shuaijunlan.xagent.transport;

import java.util.concurrent.BlockingQueue;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 14:58 2018/5/2.
 */
public class MessageCosumer extends Thread {
    private BlockingQueue<Object> blockingQueue;

    public MessageCosumer(BlockingQueue<Object> blockingQueue){
        this.blockingQueue = blockingQueue;
    }
    @Override
    public void run(){

    }
}
