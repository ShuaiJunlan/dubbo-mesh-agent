package cn.shuaijunlan.xagent.transport;

import org.junit.Test;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 21:56 2018/4/28.
 */
public class ServerTest {

    @Test
    public void start() {
        Server server = new Server();
        server.start(12345);
    }
}