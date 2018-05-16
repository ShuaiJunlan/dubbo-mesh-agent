package cn.shuaijunlan.xagent.transport.server;

import cn.shuaijunlan.xagent.dubbo.RpcClient;
import cn.shuaijunlan.xagent.dubbo.client.ClientConnectionManager;
import cn.shuaijunlan.xagent.dubbo.model.RpcResponse;
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

        //testnum
//        messageResponse.setHash(messageRequest.getParameter().hashCode());
        Thread.sleep(50);

        //get from rpc client
//        RpcClient rpcClient = new RpcClient();
//        Object result = rpcClient.invoke("com.alibaba.dubbo.performance.demo.provider.IHelloService","hash","Ljava/lang/String;",messageRequest.getParameter());
//        RpcResponse result = ClientConnectionManager.getClientInstance().invoke("com.alibaba.dubbo.performance.demo.provider.IHelloService","hash","Ljava/lang/String;",messageRequest.getParameter());

//        messageResponse.setHash(JSON.parseObject(result.getBytes(), Integer.class));
        messageResponse.setHash(messageRequest.getParameter().hashCode());

        channelHandlerContext.writeAndFlush(messageResponse);
    }

}
