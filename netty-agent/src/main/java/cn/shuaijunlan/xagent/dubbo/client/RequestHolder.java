package cn.shuaijunlan.xagent.dubbo.client;

import cn.shuaijunlan.xagent.dubbo.model.RpcResponse;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 13:11 2018/5/7.
 */
public class RequestHolder {
    private static final ConcurrentHashMap<String, RpcResponse> RPC_RESPONSE_CONCURRENT_HASH_MAP = new ConcurrentHashMap<>();
    public static void put(String id, RpcResponse response){
        RPC_RESPONSE_CONCURRENT_HASH_MAP.put(id, response);
    }
    public static RpcResponse get(String id){
        while (!RPC_RESPONSE_CONCURRENT_HASH_MAP.containsKey(id)){}
        return RPC_RESPONSE_CONCURRENT_HASH_MAP.remove(id);
    }
}
