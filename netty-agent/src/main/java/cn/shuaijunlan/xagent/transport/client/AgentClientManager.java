package cn.shuaijunlan.xagent.transport.client;

import cn.shuaijunlan.xagent.registry.Endpoint;
import cn.shuaijunlan.xagent.registry.EtcdRegistry;
import cn.shuaijunlan.xagent.registry.IRegistry;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
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
    private static LinkedList<Channel> channels = new LinkedList<>();
    private static AtomicInteger atomicInteger = new AtomicInteger(0);
    private static List<Endpoint> endpoints = null;
    private static IRegistry registry = new EtcdRegistry(System.getProperty("etcd.url"));
    private static Object object = new Object();
    private static AgentClient client1 = new AgentClient();

    static{
        try {
            endpoints = registry.find("com.alibaba.dubbo.performance.demo.provider.IHelloService");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
    public static Channel getChannel() {

        try {
            while (channels.isEmpty()) {
                // addOne();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return channels.pollFirst();
    }

    /**
     * 添加channel
     * @param channel
     */
    public static void addChannel(Channel channel){
        channels.add(channel);
    }

    // public static void add() throws Exception {
    //     logger.info("Host:{},Port{}", endpoints.get(2).getHost(), endpoints.get(2).getPort());
    //
    //     for (int i = 0; i < 256; i++){
    //         Channel channel = client1.doConnect(endpoints.get(2).getHost(), endpoints.get(2).getPort());
    //         AgentClient.sendData("", channel);
    //         channels.add(channel);
    //     }
    // }

    // public static void addOne() throws Exception {
    //     logger.info("addOne -- Host:{},Port{}", endpoints.get(2).getHost(), endpoints.get(2).getPort());
    //
    //     Channel channel = client1.doConnect(endpoints.get(2).getHost(), endpoints.get(2).getPort());
    //     AgentClient.sendData("", channel);
    //     channels.add(channel);
    // }
}
