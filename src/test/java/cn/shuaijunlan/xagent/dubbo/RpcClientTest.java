package cn.shuaijunlan.xagent.dubbo;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 20:38 2018/5/1.
 */
public class RpcClientTest {

    @Test
    public void invoke() throws Exception {
        RpcClient rpcClient = new RpcClient();
        Object result = rpcClient.invoke("com.alibaba.dubbo.performance.demo.provider.IHelloService","hash","Ljava/lang/String;","weqweqw");
        System.out.println(new String((byte[]) result));
        System.out.println("weqweqw".hashCode());
//        return (byte[]) result;
    }
}