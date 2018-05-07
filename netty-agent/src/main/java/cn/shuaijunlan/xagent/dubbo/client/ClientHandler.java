package cn.shuaijunlan.xagent.dubbo.client;

import cn.shuaijunlan.xagent.dubbo.model.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 12:57 2018/5/7.
 */
public class ClientHandler extends SimpleChannelInboundHandler<RpcResponse> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
        RequestHolder.put(msg.getRequestId(), msg);
    }
}
