package cn.shuaijunlan.agent.provider.dubbo;




import cn.shuaijunlan.agent.provider.dubbo.model.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author Junlan
 */
public class RpcClient {
    public static BlockingQueue<RpcResponse> queue = new ArrayBlockingQueue<>(16);
    private Logger logger = LoggerFactory.getLogger(RpcClient.class);

    private ConnectionManager connectManager;
    private static AtomicInteger atomicInteger = new AtomicInteger(0);
//    public Integer num;
    public RpcClient(){
//        num = atomicInteger.getAndIncrement();
        connectManager = ConnectionHolder.getConnectionManager();
    }

    public Object invoke(String interfaceName, String method, String parameterTypesString, String parameter) throws Exception {

        Channel channel = connectManager.getChannel();
        RpcInvocation invocation = new RpcInvocation();
        invocation.setMethodName(method);
        invocation.setAttachment("path", interfaceName);
        // Dubbo内部用"Ljava/lang/String"来表示参数类型是String
        invocation.setParameterTypes(parameterTypesString);
        parameter = new StringBuilder("\"").append(parameter).append("\"\n").toString();
        invocation.setArguments(parameter.getBytes());

        Request request = new Request();
//        request.setId(num);
        request.setVersion("2.0.0");
        request.setTwoWay(true);
        request.setData(invocation);

        RpcFuture future = new RpcFuture();
        RpcRequestHolder.put(String.valueOf(request.getId()),future);

        channel.writeAndFlush(request);

        Object result = null;
        try {
            result = future.get();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
