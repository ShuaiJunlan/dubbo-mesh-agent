package cn.shuaijunlan.agent.provider.dubbo;


import cn.shuaijunlan.agent.provider.dubbo.model.RpcFuture;
import cn.shuaijunlan.agent.provider.dubbo.model.RpcRequestHolder;
import cn.shuaijunlan.agent.provider.dubbo.model.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author Junlan
 */
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
