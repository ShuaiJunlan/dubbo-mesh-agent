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

    /**
     * 获取连接实例
     * @return
     */
    public synchronized static AgentClient getAgentClientInstance(){
        if (agentClients.isEmpty()){
            add();
        }
        return agentClients.pop();
    }
    private static void add(){
        AgentClient client = new AgentClient("127.0.0.1", 1234);
        client.start();
        agentClients.add(client);
    }

}
