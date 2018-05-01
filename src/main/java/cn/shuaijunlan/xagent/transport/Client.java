package cn.shuaijunlan.xagent.transport;

import cn.shuaijunlan.xagent.transport.support.msgpack.DeviceValue;
import cn.shuaijunlan.xagent.transport.support.msgpack.MsgPackDecode;
import cn.shuaijunlan.xagent.transport.support.msgpack.MsgPackEncode;
import cn.shuaijunlan.xagent.transport.support.msgpack.TypeData;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 21:48 2018/4/28.
 */
public class Client {
    private NioEventLoopGroup workGroup = new NioEventLoopGroup(4);
    private Channel channel;
    private Bootstrap bootstrap;

    private String host;
    private Integer port;
    public Client(String host, Integer port){
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public void start() {
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
                            p.addLast(new MsgPackDecode());
                            p.addLast(new MsgPackEncode());
                            p.addLast(new ClientHandler(Client.this));
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
    protected void doConnect(String host, Integer port) {
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
        Thread.sleep(random.nextInt(200));
        for (int i = 0; i < 10000; i++) {
            if (channel != null && channel.isActive()) {

                DeviceValue deviceValue = new DeviceValue();
                deviceValue.setType(TypeData.CUSTOME);
                deviceValue.setAngle(i%15);
                deviceValue.setSeatId(i%30);
                deviceValue.setSpeed(i%120);

                System.out.println("client 发送数据:"+deviceValue.toString());

                channel.writeAndFlush(deviceValue);
            }

            Thread.sleep(random.nextInt(20000));
        }
//        Thread.sleep(random.nextInt(20000));
    }


}
