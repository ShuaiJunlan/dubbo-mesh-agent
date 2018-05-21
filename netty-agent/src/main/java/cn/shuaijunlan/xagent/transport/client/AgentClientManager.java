package cn.shuaijunlan.xagent.transport.client;

import cn.shuaijunlan.xagent.registry.Endpoint;
import cn.shuaijunlan.xagent.registry.EtcdRegistry;
import cn.shuaijunlan.xagent.registry.IRegistry;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
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
    private static ArrayList<Channel> channels = new ArrayList<>(256);
    private static AtomicInteger atomicInteger = new AtomicInteger(0);
    private static List<Endpoint> endpoints = null;
    private static IRegistry registry = new EtcdRegistry(System.getProperty("etcd.url"));
    private static Object object = new Object();
//    static{
//        try {
//            endpoints = registry.find("com.alibaba.dubbo.performance.demo.provider.IHelloService");
//            logger.info("Host:{},Port{}", endpoints.get(2).getHost(), endpoints.get(2).getPort());
//            add();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 获取连接实例
     * @return
     */
    public static AgentClient getAgentClientInstance(){

        AgentClient agentClient = agentClients.get(2);
        if (agentClient.channel != null && agentClient.channel.isActive()){
            return agentClient;
        }else {
            logger.info("channel isn't avaliable");
            return  getAgentClientInstance();
        }

    }

    /**
     * 获取连接实例
     * @return
     */
    public static Channel getChannel(){

        return channels.get(atomicInteger.getAndIncrement() % 256);
    }

    public static void add() throws Exception {
        endpoints = registry.find("com.alibaba.dubbo.performance.demo.provider.IHelloService");
        logger.info("Host:{},Port{}", endpoints.get(2).getHost(), endpoints.get(2).getPort());
        AgentClient client1 = new AgentClient();

        for (int i = 0; i < 256; i++){
            channels.add(client1.doConnect(endpoints.get(2).getHost(), endpoints.get(2).getPort()));
        }
    }


}
