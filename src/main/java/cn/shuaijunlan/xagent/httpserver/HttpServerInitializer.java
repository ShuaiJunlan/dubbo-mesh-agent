package cn.shuaijunlan.xagent.httpserver;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 15:49 2018/4/28.
 */
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

        //http的编解码
        pipeline.addLast("httpServerCodec", new HttpServerCodec());
        //处理http请求的handler
        pipeline.addLast("testHttpServerHandler", new HttpServerHandler());
        pipeline.addLast(new HttpObjectAggregator(1024*1024*60*60*60*30));
        pipeline.addLast(new ChunkedWriteHandler());
    }
}
