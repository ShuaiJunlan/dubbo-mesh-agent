package cn.shuaijunlan.xagent.transport.support.msgpack;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 21:44 2018/4/28.
 */
public interface TypeData {
    //模式
    byte PING = 1;

    byte PONG = 2;

    byte CUSTOME = 3;

    //*******************************
    byte PING_SEAT = 100;

    byte PONG_SEAT = 101;

    byte SERVER_RESPONSE = 102;

    byte SERVER_RESISTANT = 103;
}
