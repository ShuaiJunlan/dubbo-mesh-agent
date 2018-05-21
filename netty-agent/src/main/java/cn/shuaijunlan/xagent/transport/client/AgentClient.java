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
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;


/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 21:48 2018/4/28.
 */
public class AgentClient {
    private EventLoopGroup workGroup = new NioEventLoopGroup();
    private KryoCodecUtil util = new KryoCodecUtil(KryoPoolFactory.getKryoPoolInstance());
    public Channel channel;
    private Bootstrap bootstrap;
    private AgentClientHandler agentClientHandler;

    private Object lock = new Object();

    private String host;
    private Integer port;
    public AgentClient(){
        start();
    }
    public AgentClient(String host, Integer port){

    }

    public void start() {
        try {
            agentClientHandler = new AgentClientHandler();
            bootstrap = new Bootstrap();
            bootstrap
                    .group(workGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline p = socketChannel.pipeline();
                            p.addLast(new KryoEncoder(util));
                            p.addLast(new KryoDecoder(util));
                            p.addLast(agentClientHandler);
                        }
                    });
//            doConnect(this.host, this.port);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 连接服务器
     */
    public Channel doConnect(String host, Integer port) throws InterruptedException {

        Channel channel = bootstrap.connect(host, port).sync().channel();
        return channel;
    }


    public Integer sendData(String param) {
        synchronized (lock){
            if (channel == null || (!channel.isActive())){
                System.out.println("channel get error");
            }else {
                MessageRequest messageRequest = new MessageRequest();
                messageRequest.setInterfaceName("com.alibaba.performance.dubbomesh.provider.IHelloService");
                messageRequest.setMethod("hash");
                messageRequest.setParameterTypesString("Ljava/lang/String;");
                messageRequest.setParameter(param);
                channel.writeAndFlush(messageRequest);
            }
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return agentClientHandler.value;
    }

    public static Integer sendData(String param, Channel channel) {
        Long num;
        if (channel == null || (!channel.isActive())){
            System.out.println("channel get error");
            return 0;
        }else {
            num = ResultMap.COUNT.getAndIncrement();
            MessageRequest messageRequest = new MessageRequest();
            messageRequest.setId(num);
            ResultMap.RESULT_MAP.put(num, null);
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
