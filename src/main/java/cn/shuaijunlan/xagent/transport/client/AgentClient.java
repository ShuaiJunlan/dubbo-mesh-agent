package cn.shuaijunlan.xagent.transport.client;

import cn.shuaijunlan.xagent.transport.support.MessageRequest;
import cn.shuaijunlan.xagent.transport.support.MessageResponse;
import cn.shuaijunlan.xagent.transport.support.kryo.KryoCodecUtil;
import cn.shuaijunlan.xagent.transport.support.kryo.KryoDecoder;
import cn.shuaijunlan.xagent.transport.support.kryo.KryoEncoder;
import cn.shuaijunlan.xagent.transport.support.kryo.KryoPoolFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
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
    private NioEventLoopGroup workGroup = new NioEventLoopGroup(4);
    private Channel channel;
    private Bootstrap bootstrap;
    private AgentClientHandler agentClientHandler;

    private String host;
    private Integer port;
    private LinkedList<MessageResponse> linkedList;
    private Long length;
    public AgentClient(String host, Integer port, LinkedList<MessageResponse> messageResponses, Long length){
        this.host = host;
        this.port = port;
        this.linkedList = messageResponses;
        this.length = length;
        agentClientHandler = new AgentClientHandler(linkedList, this.length);
    }

    public void start() {
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

//        ChannelFuture future = bootstrap.connect(host, port).sync();
        channel = bootstrap.connect(host, port).sync().channel();

//        future.addListener((ChannelFutureListener) futureListener -> {
//            if (futureListener.isSuccess()) {
//                channel = futureListener.channel();
//                System.out.println("Connect to server successfully!");
//            } else {
//                System.out.println("Failed to connect to server, try connect after 2s");
//
//                futureListener.channel().eventLoop().schedule(() -> {
//                    try {
//                        doConnect(host, port);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }, 2, TimeUnit.SECONDS);
//            }
//        });
    }

    /**
     * 测试发送数据
     * @throws Exception
     */
    public void sendDataTest(Integer start, Integer end) throws Exception {
        if (channel == null || (!channel.isActive())){
            System.out.println("channel get error");
        }else {
            for (; start < end; start++) {
                MessageRequest messageRequest = new MessageRequest();
                messageRequest.setInterfaceName("com.alibaba.performance.dubbomesh.provider.IHelloService");
                messageRequest.setMethod("hash");
                messageRequest.setParameterTypesString("Ljava/lang/String;");
                messageRequest.setParameter(RandomStringUtils.randomAlphanumeric(10));
                channel.writeAndFlush(messageRequest);
//                System.out.println("client 发送数据" + start + ":"+messageRequest.toString());
            }
        }
//        while (channel == null || !channel.isActive()){}

        for (;agentClientHandler.atomicLong.get() > 0;) {
//            System.out.println(agentClientHandler.atomicLong.get());
        }
//        channel.closeFuture();
    }
    public void sendData(String param) {
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
        for (;agentClientHandler.atomicLong.get() > 0;) {

        }
    }
}
