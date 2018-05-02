package cn.shuaijunlan.xagent.httpserver;

import java.util.concurrent.BlockingQueue;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 12:40 2018/5/2.
 */
public class HttpServerConsumer extends Thread {
    private BlockingQueue<HttpSnoopServerHandler.Entry> bq;

    public HttpServerConsumer() {

    }
    public HttpServerConsumer(BlockingQueue<HttpSnoopServerHandler.Entry> bq) {
        super();
        this.bq = bq;
    }
    @Override
    public void run() {
        while(true){
            try{
                HttpSnoopServerHandler.Entry entry = bq.take();
                System.out.println("HttpServerConsumer:::" + entry.getParameter());
                HttpSnoopServerHandler.writeResponse(entry.getContent(), entry.getContext(), entry.getParameter(), entry.getRequest());
            }catch(Exception e){
                e.printStackTrace();
            }
        }

    }
}
