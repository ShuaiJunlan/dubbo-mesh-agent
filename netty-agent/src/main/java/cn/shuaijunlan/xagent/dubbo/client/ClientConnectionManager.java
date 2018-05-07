package cn.shuaijunlan.xagent.dubbo.client;

import cn.shuaijunlan.xagent.transport.client.AgentClient;
import cn.shuaijunlan.xagent.transport.client.AgentClientManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 13:00 2018/5/7.
 */
public class ClientConnectionManager {
    private static Logger logger = LoggerFactory.getLogger(AgentClientManager.class);
    //考虑优先级队列

    private static ConcurrentHashMap<Integer, Client> clients = new ConcurrentHashMap<>();
    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    private static Object object = new Object();
    static{
        add();
    }

    /**
     * 获取连接实例
     * @return
     */
    public static synchronized Client getClientInstance(){

        Client agentClient = clients.get(atomicInteger.getAndIncrement() % 80);
        if (agentClient.channel != null && agentClient.channel.isActive()){
            return agentClient;
        }else {
            logger.info("channel isn't avaliable");
            return  getClientInstance();
        }

    }

    private static void add(){
        for (int i = 0; i < 80; i++){
            Client client1 = new Client();
            client1.start();
            clients.put(i, client1);
        }
    }
}
