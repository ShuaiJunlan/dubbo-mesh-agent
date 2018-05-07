package cn.shuaijunlan.xagent.dubbo.model;

import cn.shuaijunlan.xagent.dubbo.model.RpcFuture;

import java.util.concurrent.ConcurrentHashMap;

public class RpcRequestHolder {

    // key: requestId     value: RpcFuture
    private static ConcurrentHashMap<String, cn.shuaijunlan.xagent.dubbo.model.RpcFuture> processingRpc = new ConcurrentHashMap<>();

    public static void put(String requestId, cn.shuaijunlan.xagent.dubbo.model.RpcFuture rpcFuture){
        processingRpc.put(requestId,rpcFuture);
    }

    public static RpcFuture get(String requestId){
        return processingRpc.get(requestId);
    }

    public static void remove(String requestId){
        processingRpc.remove(requestId);
    }
}
