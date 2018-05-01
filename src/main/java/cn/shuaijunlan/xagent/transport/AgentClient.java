package cn.shuaijunlan.xagent.transport;

import cn.shuaijunlan.xagent.transport.support.MessageRequest;
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
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 21:48 2018/4/28.
 */
public class AgentClient {
    private NioEventLoopGroup workGroup = new NioEventLoopGroup(4);
    private Channel channel;
    private Bootstrap bootstrap;

    private String host;
    private Integer port;
    public AgentClient(String host, Integer port){
        this.host = host;
        this.port = port;
    }


    public void start() {
        KryoCodecUtil util = new KryoCodecUtil(KryoPoolFactory.getKryoPoolInstance());
        try {
            bootstrap = new Bootstrap();
            bootstrap
                    .group(workGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline p = socketChannel.pipeline();
                            p.addLast(new IdleStateHandler(0, 0, 5));
///                            p.addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, -4, 0));
                            p.addLast(new KryoEncoder(util));
                            p.addLast(new KryoDecoder(util));
                            p.addLast(new AgentClientHandler());
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
    private void doConnect(String host, Integer port) {
        if (channel != null && channel.isActive()) {
            return;
        }

        ChannelFuture future = bootstrap.connect(host, port);

        future.addListener((ChannelFutureListener) futureListener -> {
            if (futureListener.isSuccess()) {
                channel = futureListener.channel();
                System.out.println("Connect to server successfully!");
            } else {
                System.out.println("Failed to connect to server, try connect after 2s");

                futureListener.channel().eventLoop().schedule(() -> doConnect(host, port), 2, TimeUnit.SECONDS);
            }
        });
    }

    /**
     * 发送数据 每隔2秒发送一次
     * @throws Exception
     */
    public void sendData() throws Exception {
        Random random = new Random(System.currentTimeMillis());
        Thread.sleep(random.nextInt(2000));

        for (int i = 0; i < 1000; i++) {
            if (channel != null && channel.isActive()) {

                MessageRequest messageRequest = new MessageRequest();
                messageRequest.setInterfaceName("com.alibaba.performance.dubbomesh.provider.IHelloService");
                messageRequest.setMethod("hash");
                messageRequest.setParameterTypesString("Ljava/lang/String;");
                messageRequest.setParameter(RandomStringUtils.randomAlphanumeric(10));
                channel.writeAndFlush(messageRequest);
                System.out.println("client 发送数据:"+messageRequest.toString());
            }
        }
        //获取接受数据
        Thread.sleep(2000);
    }


}
