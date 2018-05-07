package cn.shuaijunlan.xagent.transport.support.kryo;

import cn.shuaijunlan.xagent.transport.support.MessageCodecUtil;
import cn.shuaijunlan.xagent.transport.support.MessageEncoder;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 11:25 2018/4/29.
 */
public class KryoEncoder extends MessageEncoder {
    public KryoEncoder(MessageCodecUtil util) {
        super(util);
    }
}
