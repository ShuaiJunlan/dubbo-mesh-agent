package cn.shuaijunlan.agent.provider.dubbo.model;

import cn.shuaijunlan.agent.provider.dubbo.ConnectionManager;

import java.util.LinkedList;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 16:31 2018/5/24.
 */
public class ConnectionHolder {
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
        return connectionManagers.add(c);
    }

}
