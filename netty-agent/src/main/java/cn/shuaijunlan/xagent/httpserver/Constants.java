package cn.shuaijunlan.xagent.httpserver;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 20:43 2018/5/21.
 */
public class Constants {
    public static final String[] URLS = {"http://10.10.10.3:30000","http://10.10.10.4:30000","http://10.10.10.5:30000"};
    public static final AtomicInteger CONNECTION_COUNT = new AtomicInteger(0);
}
