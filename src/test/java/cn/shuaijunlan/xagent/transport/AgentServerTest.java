package cn.shuaijunlan.xagent.transport;

import org.junit.Test;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 13:03 2018/4/29.
 */
public class AgentServerTest {

    @Test
    public void start() {
        AgentServer server = new AgentServer();
        server.start(1234);
    }
}