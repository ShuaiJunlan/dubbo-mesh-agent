package cn.shuaijunlan.xagent.transport;

import java.util.concurrent.BlockingQueue;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 14:59 2018/5/2.
 */
public class MessageProvider extends Thread {
    private BlockingQueue<Object> blockingQueue;

    public MessageProvider(BlockingQueue<Object> blockingQueue){
        this.blockingQueue = blockingQueue;
    }
    @Override
    public void run(){

    }
}
