package cn.shuaijunlan.xagent.transport.support;

import cn.shuaijunlan.xagent.transport.support.MessageCodecUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 11:14 2018/4/29.
 */
public class MessageEncoder extends MessageToByteEncoder<Object> {

    private MessageCodecUtil util = null;

    public MessageEncoder(final MessageCodecUtil util) {
        this.util = util;
    }

    @Override
    protected void encode(final ChannelHandlerContext ctx, final Object msg, final ByteBuf out) throws Exception {
        util.encode(out, msg);
    }
}
