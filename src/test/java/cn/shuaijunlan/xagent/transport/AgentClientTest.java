package cn.shuaijunlan.xagent.transport;

import org.junit.Test;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 13:03 2018/4/29.
 */
public class AgentClientTest {

    @Test
    public void start() throws Exception {
        AgentClient client = new AgentClient("127.0.0.1", 1234);
        client.start();
        client.sendData();
    }
    @Test
    public void ss(){
        System.out.println("pJ4fmv9GQ1".hashCode());
    }
}