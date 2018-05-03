package cn.shuaijunlan.xagent.transport.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 20:47 2018/5/2.
 */
public class AgentClientManager {
    private static Logger logger = LoggerFactory.getLogger(AgentClientManager.class);
    //考虑优先级队列

    private static LinkedList<AgentClient> agentClients = new LinkedList<>();

    private static Object object = new Object();
    static{
        add();
    }

    /**
     * 获取连接实例
     * @return
     */
    public static AgentClient getAgentClientInstance(){
        synchronized (object){
            if (agentClients.isEmpty()){
                add();
            }
        }
        AgentClient client = agentClients.pop();
        if (logger.isInfoEnabled()){
            logger.info("Execute getAgentClientInstance, AgentClientManager Size:{}", agentClients.size());
        }
        return client;
    }
    public static void putOne(AgentClient client){
        synchronized (object){
            agentClients.add(client);
        }
        if (logger.isInfoEnabled()){
            logger.info("Execute putOne, AgentClientManager Size:{}", agentClients.size());
        }
    }

    private static void add(){
        for (int i = 0; i < 240; i++){
            AgentClient client = new AgentClient("127.0.0.1", 1234 + (i%3));
            client.start();
            agentClients.add(client);
        }
        if (logger.isInfoEnabled()){
            logger.info("Execute add(), AgentClientManager Size:{}", agentClients.size());
        }
    }

}
