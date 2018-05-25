package cn.shuaijunlan.agent.provider.dubbo.model;

import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 11:14 2018/5/25.
 */
public class ChannelContextHolder {

    private static ConcurrentHashMap<String,ChannelHandlerContext> contextConcurrentHashMap = new ConcurrentHashMap<>();

    public static void put(String requestId,ChannelHandlerContext c){
        contextConcurrentHashMap.put(requestId,c);
    }

    public static ChannelHandlerContext get(String requestId){
        return contextConcurrentHashMap.get(requestId);
    }

    public static void remove(String requestId){
        contextConcurrentHashMap.remove(requestId);
    }
}
