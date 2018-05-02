package cn.shuaijunlan.xagent.dubbo;

import com.alibaba.fastjson.JSON;
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
        System.out.println("weqweqw".hashCode());
        Integer a = JSON.parseObject((byte[]) result, Integer.class);
        System.out.println(a);
//        System.out.println(Integer.valueOf(new String((byte[]) result).replaceAll("(\0|\\s*|\r|\n)", "")));
//        return (byte[]) result;
    }
}