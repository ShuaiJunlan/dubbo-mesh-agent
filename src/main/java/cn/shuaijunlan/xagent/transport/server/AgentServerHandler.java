package cn.shuaijunlan.xagent.transport.server;

import cn.shuaijunlan.xagent.dubbo.RpcClient;
import cn.shuaijunlan.xagent.transport.support.MessageRequest;
import cn.shuaijunlan.xagent.transport.support.MessageResponse;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 21:47 2018/4/28.
 */
public class AgentServerHandler extends SimpleChannelInboundHandler<MessageRequest> {
    int i = 0;

    public AgentServerHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageRequest messageRequest) throws Exception {

        MessageResponse messageResponse = new MessageResponse();

        //test
        messageResponse.setHash(messageRequest.getParameter().hashCode());

        //get from rpc client
//        RpcClient rpcClient = new RpcClient();
//        Object result = rpcClient.invoke("com.alibaba.dubbo.performance.demo.provider.IHelloService","hash","Ljava/lang/String;",messageRequest.getParameter());
//        messageResponse.setHash(JSON.parseObject((byte[]) result, Integer.class));

        channelHandlerContext.writeAndFlush(messageResponse);
    }

}
