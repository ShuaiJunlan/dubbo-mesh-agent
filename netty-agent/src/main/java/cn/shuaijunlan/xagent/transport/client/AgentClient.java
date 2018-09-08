package cn.shuaijunlan.xagent.transport.client;

import cn.shuaijunlan.xagent.transport.client.AgentClientHandler;
import cn.shuaijunlan.xagent.transport.support.MessageRequest;
import cn.shuaijunlan.xagent.transport.support.MessageResponse;
import cn.shuaijunlan.xagent.transport.support.kryo.KryoCodecUtil;
import cn.shuaijunlan.xagent.transport.support.kryo.KryoDecoder;
import cn.shuaijunlan.xagent.transport.support.kryo.KryoEncoder;
import cn.shuaijunlan.xagent.transport.support.kryo.KryoPoolFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 21:48 2018/4/28.
 */
public class AgentClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(AgentClient.class);

    private static EventLoopGroup workGroup = Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
    private static KryoCodecUtil util = new KryoCodecUtil(KryoPoolFactory.getKryoPoolInstance());
    public Channel channel;
    private static Bootstrap bootstrap;
    private AgentClientHandler agentClientHandler;

    private Object lock = new Object();

    private String host;
    private Integer port;
    public AgentClient(){
        start();
    }
    public AgentClient(String host, Integer port){

    }

    public static void start() {
        try {
//            agentClientHandler = new AgentClientHandler();
            bootstrap = new Bootstrap();
            bootstrap
                    .group(workGroup)
                    .channel(Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline p = socketChannel.pipeline();
                            p.addLast(new KryoEncoder(util));
                            p.addLast(new KryoDecoder(util));
                            p.addLast(new AgentClientHandler());
                        }
                    });
//            doConnect(this.host, this.port);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Connecting remote server
     * @param host
     * @param port
     * @return
     * @throws InterruptedException
     */
    private static Channel doConnect(String host, Integer port) throws InterruptedException {

        Channel channel = bootstrap.connect(host, port).sync().channel();
        return channel;
    }

    /**
     * Getting channel by thread name
     * @param name thread name
     * @return
     * @throws InterruptedException
     */
    public static Channel getChannel(String name) throws InterruptedException {
        if (name == null){
            throw new NullPointerException("Name can't be bull");
        }
        Channel channel = ResultMap.CHANNEL_CONCURRENT_HASH_MAP.get(name);
        if (channel == null){

            channel = doConnect(ResultMap.HOST, ResultMap.POST);
            ResultMap.CHANNEL_CONCURRENT_HASH_MAP.put(name, channel);
            if (LOGGER.isInfoEnabled()){
                LOGGER.info("Creating a new channel:{}, binding thread name:{}!", channel, name);
            }
        }
        return channel;
    }


    public static void sendData(String param, Channel channel, Long id) {
        if (channel == null || (!channel.isActive())){
            if (LOGGER.isErrorEnabled()){
                LOGGER.error("Channel:{} is unavailable!", channel);
            }
        }else {
            MessageRequest messageRequest = new MessageRequest();
            messageRequest.setId(id);
            messageRequest.setInterfaceName("com.alibaba.performance.dubbomesh.provider.IHelloService");
            messageRequest.setMethod("hash");
            messageRequest.setParameterTypesString("Ljava/lang/String;");
            messageRequest.setParameter(param);
            channel.writeAndFlush(messageRequest);
        }
    }

    public static synchronized Integer sendData(String param, Channel channel) {
        Long num;
        if (channel == null || (!channel.isActive())){
            System.out.println("channel get error");
            return 0;
        }else {
            num = ResultMap.COUNT.getAndIncrement();
            MessageRequest messageRequest = new MessageRequest();
            messageRequest.setId(num);
            messageRequest.setInterfaceName("com.alibaba.performance.dubbomesh.provider.IHelloService");
            messageRequest.setMethod("hash");
            messageRequest.setParameterTypesString("Ljava/lang/String;");
            messageRequest.setParameter(param);
            channel.writeAndFlush(messageRequest);
        }
        while (ResultMap.RESULT_MAP.get(num) == null){

        }
        return ResultMap.RESULT_MAP.remove(num);
    }
}
