package cn.shuaijunlan.xagent.transport.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 20:47 2018/5/2.
 */
public class AgentClientManager {
    private static Logger logger = LoggerFactory.getLogger(AgentClientManager.class);
    //考虑优先级队列

    private static ConcurrentHashMap<Integer, AgentClient> agentClients = new ConcurrentHashMap<>();
    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    private static Object object = new Object();
    static{
        add();
    }

    /**
     * 获取连接实例
     * @return
     */
    public static AgentClient getAgentClientInstance(){

        AgentClient agentClient = agentClients.get(atomicInteger.getAndIncrement() % 240);
        if (agentClient.channel != null && agentClient.channel.isActive()){
            return agentClient;
        }else {
            logger.info("channel isn't avaliable");
            return  getAgentClientInstance();
        }
//        agentClients.add(agentClient);
//        if (logger.isInfoEnabled()){
//            logger.info("Execute getAgentClientInstance, AgentClientManager Size:{}", agentClients.size());
//        }
    }
//    public static void putOne(AgentClient client){
//        synchronized (object){
//            agentClients.add(client);
//        }
//        if (logger.isInfoEnabled()){
//            logger.info("Execute putOne, AgentClientManager Size:{}", agentClients.size());
//        }
//    }

    private static void add(){
        for (int i = 0; i < 240; i++){
            AgentClient client = new AgentClient("127.0.0.1", 1234 + (i%3));
            client.start();
            agentClients.put(i, client);
        }
//        if (logger.isInfoEnabled()){
//            logger.info("Execute add(), AgentClientManager Size:{}", agentClients.size());
//        }
    }

}
