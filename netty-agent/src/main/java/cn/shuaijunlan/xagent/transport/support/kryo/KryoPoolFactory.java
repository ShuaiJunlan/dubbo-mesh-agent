package cn.shuaijunlan.xagent.transport.support.kryo;

import cn.shuaijunlan.xagent.transport.support.MessageRequest;
import cn.shuaijunlan.xagent.transport.support.MessageResponse;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.pool.KryoFactory;
import com.esotericsoftware.kryo.pool.KryoPool;
import org.objenesis.strategy.StdInstantiatorStrategy;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 11:18 2018/4/29.
 */
public class KryoPoolFactory {

    private static KryoPoolFactory poolFactory = null;

    private KryoFactory factory = () -> {
        Kryo kryo = new Kryo();
        kryo.setReferences(false);
        //把已知的结构注册到Kryo注册器里面，提高序列化/反序列化效率
        kryo.register(MessageRequest.class);
        kryo.register(MessageResponse.class);
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
        return kryo;
    };

    private KryoPool pool = new KryoPool.Builder(factory).build();

    private KryoPoolFactory() {
    }

    public static KryoPool getKryoPoolInstance() {
        if (poolFactory == null) {
            synchronized (KryoPoolFactory.class) {
                if (poolFactory == null) {
                    poolFactory = new KryoPoolFactory();
                }
            }
        }
        return poolFactory.getPool();
    }

    public KryoPool getPool() {
        return pool;
    }
}
