package cn.shuaijunlan.xagent.transport;

import cn.shuaijunlan.xagent.transport.support.MessageResponse;
import org.junit.Test;

import java.util.LinkedList;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 13:03 2018/4/29.
 */
public class AgentClientTest {

    @Test
    public void start() throws Exception {
//        for (int i = 0; i < 10; i++){
            LinkedList<MessageResponse> messageResponses = new LinkedList<>();
            Long length  = 1000L;
            AgentClient client = new AgentClient("127.0.0.1", 1234, messageResponses, length);
            client.start();
            client.sendData(0,1000);
//        }

    }
    @Test
    public void ss() throws InterruptedException {
        LinkedList<MessageResponse> messageResponses = new LinkedList<>();
        Long length  = 10000L;
        AgentClient client = new AgentClient("127.0.0.1", 1234, messageResponses, length);
        client.start();
        new Thread(() -> {
            try {
                client.sendData(0, 5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                client.sendData(5000, 10000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        Thread.sleep(3000);
    }
}