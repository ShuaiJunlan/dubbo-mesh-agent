package cn.shuaijunlan.xagent.transport.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

        AgentClient agentClient = agentClients.get(atomicInteger.getAndIncrement() % 480);
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
        for (int i = 0; i < 80; i++){
            cn.shuaijunlan.xagent.transport.client.AgentClient client1 = new cn.shuaijunlan.xagent.transport.client.AgentClient("127.0.0.1", 1234);
            client1.start();
            agentClients.put(6*i + 0, client1);

            cn.shuaijunlan.xagent.transport.client.AgentClient client2 = new cn.shuaijunlan.xagent.transport.client.AgentClient("127.0.0.1", 1235);
            client2.start();
            agentClients.put(6*i + 1, client2);

            cn.shuaijunlan.xagent.transport.client.AgentClient client3 = new cn.shuaijunlan.xagent.transport.client.AgentClient("127.0.0.1", 1235);
            client3.start();
            agentClients.put(6*i + 2, client3);

            cn.shuaijunlan.xagent.transport.client.AgentClient client4 = new cn.shuaijunlan.xagent.transport.client.AgentClient("127.0.0.1", 1236);
            client4.start();
            agentClients.put(6*i + 3, client4);

            cn.shuaijunlan.xagent.transport.client.AgentClient client5 = new cn.shuaijunlan.xagent.transport.client.AgentClient("127.0.0.1", 1236);
            client5.start();
            agentClients.put(6*i + 4, client5);

            cn.shuaijunlan.xagent.transport.client.AgentClient client6 = new AgentClient("127.0.0.1", 1236);
            client6.start();
            agentClients.put(6*i + 5, client6);

        }
    }

}
