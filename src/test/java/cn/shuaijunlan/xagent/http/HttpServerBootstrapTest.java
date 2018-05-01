package cn.shuaijunlan.xagent.http;

import cn.shuaijunlan.xagent.httpserver.HttpServerBootstrap;
import org.junit.Test;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 16:34 2018/4/28.
 */
public class HttpServerBootstrapTest {

    @Test
    public void start() {
        HttpServerBootstrap httpServerBootstrap = new HttpServerBootstrap();
        try {
            httpServerBootstrap.start(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}