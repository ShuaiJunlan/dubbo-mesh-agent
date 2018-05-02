package cn.shuaijunlan.xagent.testnum;

import cn.shuaijunlan.xagent.transport.client.AgentClient;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashSet;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 21:19 2018/5/2.
 */
public class Test {
    public static HashSet<AgentClient> agentClients = new HashSet<>();
    public static HashSet<ChannelHandlerContext> channelHandlerContexts= new HashSet();
}
