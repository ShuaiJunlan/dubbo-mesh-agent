package cn.shuaijunlan.xagent.transport.support;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 11:05 2018/4/29.
 */
public interface MessageCodecUtil {
    /**
     * RPC消息报文头长度4个字节
     */
    int MESSAGE_LENGTH = 4;

    /**
     * 编码
     * @param out
     * @param message
     * @throws IOException
     */
    void encode(final ByteBuf out, final Object message) throws IOException;

    /**
     * 解码
     * @param body
     * @return
     * @throws IOException
     */
    Object decode(byte[] body) throws IOException;
}
