package cn.shuaijunlan.xagent.dubbo;

import cn.shuaijunlan.xagent.dubbo.model.RpcFuture;
import cn.shuaijunlan.xagent.dubbo.model.RpcRequestHolder;
import cn.shuaijunlan.xagent.dubbo.model.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse response) {
        String requestId = response.getRequestId();
        RpcFuture future = RpcRequestHolder.get(requestId);
        if(null != future){
            RpcRequestHolder.remove(requestId);
            future.done(response);
        }
    }
}
