package cn.shuaijunlan.xagent.transport.client;

import io.netty.channel.Channel;
import io.netty.util.concurrent.Promise;

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


    public static final ConcurrentHashMap<String, Channel> CHANNEL_CONCURRENT_HASH_MAP = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<Long, Promise<Integer>> PROMISE_CONCURRENT_HASH_MAP = new ConcurrentHashMap<>();

    public static final String HOST = "127.0.0.1";
    public static final Integer POST = 20001;
}
