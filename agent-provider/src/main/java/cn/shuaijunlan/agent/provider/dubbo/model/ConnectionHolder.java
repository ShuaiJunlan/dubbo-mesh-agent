package cn.shuaijunlan.agent.provider.dubbo.model;

import cn.shuaijunlan.agent.provider.dubbo.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 16:31 2018/5/24.
 */
public class ConnectionHolder {
    private static Logger logger = LoggerFactory.getLogger(ConnectionHolder.class);
    private static AtomicInteger atomicInteger = new AtomicInteger(0);
    private static Object object = new Object();
    private static ConcurrentLinkedQueue<ConnectionManager> connectionManagers = new ConcurrentLinkedQueue<>();
    public static ConnectionManager getConnectionManager(){
        if (connectionManagers.isEmpty()){
            synchronized (object){
                while (connectionManagers.isEmpty()){
                    newConnectionManager();
                }
            }
        }
        return connectionManagers.remove();
    }
    public static void release(ConnectionManager c){
        connectionManagers.add(c);
    }

    private static boolean newConnectionManager(){
        ConnectionManager c = new ConnectionManager();
        logger.info("Creating a new connection: {}!", atomicInteger.incrementAndGet());
        return connectionManagers.add(c);
    }

}
