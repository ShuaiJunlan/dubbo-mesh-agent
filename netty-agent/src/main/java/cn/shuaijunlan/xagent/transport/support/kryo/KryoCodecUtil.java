package cn.shuaijunlan.xagent.transport.support.kryo;

import cn.shuaijunlan.xagent.transport.support.MessageCodecUtil;
import cn.shuaijunlan.xagent.transport.support.kryo.KryoSerialize;
import com.esotericsoftware.kryo.pool.KryoPool;
import com.google.common.io.Closer;
import io.netty.buffer.ByteBuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 11:22 2018/4/29.
 */
public class KryoCodecUtil implements MessageCodecUtil {

    private KryoPool pool;
//    private static Closer closer = Closer.create();

    private Object lock1 = new Object();
    private Object lock2 = new Object();


    public KryoCodecUtil(KryoPool pool) {
        this.pool = pool;
    }

    @Override
    public void encode(final ByteBuf out, final Object message) throws IOException {
//        synchronized (lock1){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                KryoSerialize kryoSerialization = new KryoSerialize(pool);
                kryoSerialization.serialize(byteArrayOutputStream, message);
                byte[] body = byteArrayOutputStream.toByteArray();
                int dataLength = body.length;
                out.writeInt(dataLength);
                out.writeBytes(body);
            } finally {
                byteArrayOutputStream.close();
            }
//        }

    }

    @Override
    public Object decode(byte[] body) throws IOException {
//        synchronized (lock2){
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);

            try {
                KryoSerialize kryoSerialization = new KryoSerialize(pool);
                Object obj = kryoSerialization.deserialize(byteArrayInputStream);
                return obj;
            } finally {
                byteArrayInputStream.close();
            }
//        }
    }
}
