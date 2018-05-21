package cn.shuaijunlan.xagent.transport.client;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 9:39 2018/5/21.
 */
public class ResultMap {
    public static final AtomicLong COUNT = new AtomicLong(0);
    public static final ConcurrentHashMap<Long, Integer> RESULT_MAP = new ConcurrentHashMap<>();
}
