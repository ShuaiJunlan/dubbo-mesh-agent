package cn.shuaijunlan.xagent.transport;

import cn.shuaijunlan.xagent.transport.Client;
import cn.shuaijunlan.xagent.transport.CustomHeartbeatHandler;
import cn.shuaijunlan.xagent.transport.support.msgpack.DeviceValue;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 21:46 2018/4/28.
 */
public class ClientHandler extends CustomHeartbeatHandler {
    private cn.shuaijunlan.xagent.transport.Client client;
    public ClientHandler(Client client) {
        super("client");
        this.client = client;
    }


    @Override
    protected void handleData(ChannelHandlerContext channelHandlerContext, Object msg) {
        DeviceValue deviceValue = (DeviceValue) msg;
        System.out.println("client 接收数据:"+deviceValue.toString());

//        DeviceValue s = new DeviceValue();
//        s.setType(TypeData.CUSTOME);
//        s.setSpeed(0);
//        s.setAngle(15);
//        s.setSeatId(TypeData.SERVER_RESPONSE);
//        channelHandlerContext.writeAndFlush(s);
    }

    @Override
    protected void handleAllIdle(ChannelHandlerContext ctx) {
        super.handleAllIdle(ctx);
        sendPingMsg(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        client.doConnect(client.getHost(), client.getPort());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(name + " exception"+cause.toString());

    }
}
