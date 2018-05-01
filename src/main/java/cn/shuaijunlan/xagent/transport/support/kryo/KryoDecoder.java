package cn.shuaijunlan.xagent.transport.support.kryo;

import cn.shuaijunlan.xagent.transport.support.MessageCodecUtil;
import cn.shuaijunlan.xagent.transport.support.MessageDecoder;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 11:25 2018/4/29.
 */
public class KryoDecoder extends MessageDecoder {
    public KryoDecoder(MessageCodecUtil util) {
        super(util);
    }
}
