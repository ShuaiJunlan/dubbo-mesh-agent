package cn.shuaijunlan.agent.provider.dubbo.model;

import cn.shuaijunlan.agent.provider.dubbo.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 16:31 2018/5/24.
 */
public class ConnectionHolder {
    private static Logger logger = LoggerFactory.getLogger(ConnectionHolder.class);
    private static AtomicInteger atomicInteger = new AtomicInteger(0);
    private static LinkedList<ConnectionManager> connectionManagers = new LinkedList<>();
    public static synchronized ConnectionManager getConnectionManager(){
        while (connectionManagers.isEmpty()){
            newConnectionManager();
        }
        return connectionManagers.pop();
    }
    public static synchronized void release(ConnectionManager c){
        connectionManagers.add(c);
    }

    private static boolean newConnectionManager(){
        ConnectionManager c = new ConnectionManager();
        logger.info("Creating a new connection: {}!", atomicInteger.incrementAndGet());
        return connectionManagers.add(c);
    }

}
