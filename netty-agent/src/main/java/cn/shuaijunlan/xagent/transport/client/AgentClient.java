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
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.LinkedList;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 21:48 2018/4/28.
 */
public class AgentClient {
    private EventLoopGroup workGroup = new NioEventLoopGroup();
    public Channel channel;
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

    public void start() {
        KryoCodecUtil util = new KryoCodecUtil(KryoPoolFactory.getKryoPoolInstance());
        try {
            bootstrap = new Bootstrap();
            bootstrap
                    .group(workGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline p = socketChannel.pipeline();
//                            p.addLast(new IdleStateHandler(1, 1, 5));
///                            p.addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, -4, 0));
                            p.addLast(new KryoEncoder(util));
                            p.addLast(new KryoDecoder(util));
                            p.addLast(agentClientHandler);
                        }
                    });
            doConnect(this.host, this.port);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 重连机制,每隔2s重新连接一次服务器
     */
    private void doConnect(String host, Integer port) throws InterruptedException {
        if (channel != null && channel.isActive()) {
            return;
        }
        channel = bootstrap.connect(host, port).sync().channel();
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
}
