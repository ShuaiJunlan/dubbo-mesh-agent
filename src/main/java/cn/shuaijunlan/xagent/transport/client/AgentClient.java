package cn.shuaijunlan.xagent.transport.client;

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
import io.netty.handler.timeout.IdleStateHandler;


/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 21:48 2018/4/28.
 */
public class AgentClient {
    private NioEventLoopGroup workGroup = new NioEventLoopGroup(4);
    private Channel channel;
    private Bootstrap bootstrap;
    private AgentClientHandler agentClientHandler;

    private Object lock = new Object();

    private String host;
    private Integer port;
    public AgentClient(String host, Integer port){
        this.host = host;
        this.port = port;
        agentClientHandler = new AgentClientHandler(lock);
    }
    public AgentClient(){
        agentClientHandler = new AgentClientHandler(lock);
    }


    public void init() {
        KryoCodecUtil util = new KryoCodecUtil(KryoPoolFactory.getKryoPoolInstance());
        try {
            bootstrap = new Bootstrap();
            bootstrap
                    .group(workGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline p = socketChannel.pipeline();
                            p.addLast(new IdleStateHandler(1, 1, 5));
                            p.addLast(new KryoEncoder(util));
                            p.addLast(new KryoDecoder(util));
                            p.addLast(agentClientHandler);
                        }
                    });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 连接服务端
     */
    public Channel doConnect() throws InterruptedException {
        Channel channel = null;
        ChannelFuture channelFuture = bootstrap.connect(this.host, this.port).sync();
        if (channelFuture.isSuccess()){
            channel = channelFuture.channel();
        }
        return channel;
    }

    public Channel doConnect(String host, Integer port) throws InterruptedException {
        Channel channel = null;
        ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
        if (channelFuture.isSuccess()){
            channel = channelFuture.channel();
        }
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

    public Integer sendData(String param, Channel channel) {
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
}
