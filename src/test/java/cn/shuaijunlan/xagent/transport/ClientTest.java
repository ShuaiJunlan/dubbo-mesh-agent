package cn.shuaijunlan.xagent.transport;

import org.junit.Test;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 21:57 2018/4/28.
 */
public class ClientTest {

    @Test
    public void start() throws Exception {
        Client client = new Client("127.0.0.1", 12345);
        client.start();
        client.sendData();
    }

    @Test
    public void doConnect() {
    }

    @Test
    public void sendData() {
    }
}