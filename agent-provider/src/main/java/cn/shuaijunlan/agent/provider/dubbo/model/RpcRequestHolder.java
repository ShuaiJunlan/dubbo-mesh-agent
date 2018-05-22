package cn.shuaijunlan.agent.provider.dubbo.model;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Junlan
 */
public class RpcRequestHolder {

    // key: requestId     value: RpcFuture
    private static ConcurrentHashMap<String,RpcFuture> processingRpc = new ConcurrentHashMap<>();

    public static void put(String requestId,RpcFuture rpcFuture){
        processingRpc.put(requestId,rpcFuture);
    }

    public static RpcFuture get(String requestId){
        return processingRpc.get(requestId);
    }

    public static void remove(String requestId){
        processingRpc.remove(requestId);
    }
}
