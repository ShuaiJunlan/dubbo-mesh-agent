package cn.shuaijunlan.xagent.transport.client;

import io.netty.channel.Channel;
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
    public static AgentClient agentClient = new AgentClient();
    private static LinkedList<AgentClient> agentClients = new LinkedList<>();
    private static LinkedList<Channel> channels = new LinkedList<>();

    private static Object object = new Object();
    static{
        agentClient.init();
        add();
    }
    /**
     * 获取连接实例
     * @return
     */
    public static Channel getAgentClientInstance(){
        Channel channel;
        synchronized (object){
            if (channels.isEmpty()){
                add();
            }
            channel = channels.pop();
            channels.add(channel);
        }
        if (logger.isInfoEnabled()){
            logger.info("Execute getAgentClientInstance, channels Size:{}", channels.size());
        }
        return channel;
    }
    public static void putOne(AgentClient client){
        synchronized (object){
            agentClients.add(client);
        }
        if (logger.isInfoEnabled()){
            logger.info("Execute putOne, channels Size:{}", agentClients.size());
        }
    }

    private static void add() {
        for (int i = 0; i < 240; i++){
            Channel channel;
            try {
                channel = agentClient.doConnect("127.0.0.1", 1234 + (i%3));
                if (channel == null){
                    continue;
                }
                channels.add(channel);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (logger.isInfoEnabled()){
            logger.info("Execute add(), channels Size:{}", channels.size());
        }
    }
}
