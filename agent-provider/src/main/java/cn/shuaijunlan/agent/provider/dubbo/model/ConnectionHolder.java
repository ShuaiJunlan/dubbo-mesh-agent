package cn.shuaijunlan.agent.provider.dubbo.model;

import cn.shuaijunlan.agent.provider.dubbo.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 16:31 2018/5/24.
 */
public class ConnectionHolder {
    private static Logger logger = LoggerFactory.getLogger(ConnectionHolder.class);
    private static Integer max_counts = Integer.valueOf(System.getProperty("agent.dubbo.client.threads"));
    private static AtomicInteger atomicInteger = new AtomicInteger(0);
    private static ArrayList<ConnectionManager> connectionManagers = new ArrayList<>(max_counts);
    private static Object object = new Object();
    public static synchronized ConnectionManager getConnectionManager(){
        if (connectionManagers.isEmpty() || connectionManagers.size() < max_counts){
            synchronized (object){
                if (connectionManagers.isEmpty() || connectionManagers.size() < max_counts){
                    newConnectionManager();
                }
            }
        }
        return getConnection();
    }
    public static  synchronized ConnectionManager getConnection(){
        return connectionManagers.get(atomicInteger.getAndIncrement() % max_counts);
    }
    public static synchronized void release(ConnectionManager c){
        connectionManagers.add(c);
    }

    private static boolean newConnectionManager(){
        ConnectionManager c = new ConnectionManager();
        logger.info("Creating a new connection: {}!", atomicInteger.get());
        return connectionManagers.add(c);
    }

}
