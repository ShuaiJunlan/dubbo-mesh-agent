package cn.shuaijunlan.xagent.transport.support;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 11:05 2018/4/29.
 */
public interface RpcSerialize {
    void serialize(OutputStream output, Object object) throws IOException;

    Object deserialize(InputStream input) throws IOException;
}
