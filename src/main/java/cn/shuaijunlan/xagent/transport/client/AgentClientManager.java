package cn.shuaijunlan.xagent.transport.client;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 20:47 2018/5/2.
 */
public class AgentClientManager {
    //考虑优先级队列

    private static LinkedList<AgentClient> agentClients = new LinkedList<>();

    private static Object object = new Object();

    /**
     * 获取连接实例
     * @return
     */
    public synchronized static AgentClient getAgentClientInstance(){
        synchronized (object){
            if (agentClients.isEmpty()){
                add();
            }
        }

        return agentClients.pop();
    }
    public static void putOne(AgentClient client){
        synchronized (object){
            agentClients.add(client);
        }
    }

    private static void add(){
        for (int i = 0; i < 80; i++){
            AgentClient client = new AgentClient("127.0.0.1", 1234);
            client.start();
            agentClients.add(client);
        }
        for (int i = 0; i < 80; i++){
            AgentClient client = new AgentClient("127.0.0.1", 1235);
            client.start();
            agentClients.add(client);
        }
        for (int i = 0; i < 80; i++){
            AgentClient client = new AgentClient("127.0.0.1", 1236);
            client.start();
            agentClients.add(client);
        }
    }

}
