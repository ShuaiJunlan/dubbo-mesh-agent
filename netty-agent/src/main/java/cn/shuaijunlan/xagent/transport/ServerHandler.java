package cn.shuaijunlan.xagent.transport;

import cn.shuaijunlan.xagent.transport.CustomHeartbeatHandler;
import cn.shuaijunlan.xagent.transport.support.msgpack.DeviceValue;
import cn.shuaijunlan.xagent.transport.support.msgpack.TypeData;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 21:47 2018/4/28.
 */
public class ServerHandler extends CustomHeartbeatHandler {

    public ServerHandler() {
        super("server");
    }

    @Override
    protected void handleData(ChannelHandlerContext channelHandlerContext, Object msg) {
        DeviceValue deviceValue = (DeviceValue) msg;
        System.out.println("server 接收数据:"+deviceValue.toString());

        DeviceValue s = new DeviceValue();
        s.setType(TypeData.CUSTOME);
        s.setSpeed(0);
        s.setAngle(15);
        s.setSeatId(TypeData.SERVER_RESPONSE);

//        MessageResponse messageResponse = new MessageResponse();
//        messageResponse.setError("non");
//        messageResponse.setMessageId("111");
//        messageResponse.setResult(new String("11111"));
        channelHandlerContext.writeAndFlush(s);
        System.out.println("server 发送数据:"+s.toString());
    }

    @Override
    protected void handleReaderIdle(ChannelHandlerContext ctx) {
        super.handleReaderIdle(ctx);
        System.err.println("---client " + ctx.channel().remoteAddress().toString() + " reader timeout, close it---");
        ctx.close();
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(name+" exception"+cause.toString());
    }
}
