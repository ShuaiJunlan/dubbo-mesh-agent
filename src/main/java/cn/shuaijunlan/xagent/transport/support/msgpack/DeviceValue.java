package cn.shuaijunlan.xagent.transport.support.msgpack;

import org.msgpack.annotation.Message;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 21:43 2018/4/28.
 */
@Message
public class DeviceValue {
    private int type;

    private int seatId;

    private int speed;

    private int angle;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    @Override
    public String toString() {
        return "DeviceValue{" +
                "type=" + type +
                ", seatId=" + seatId +
                ", speed=" + speed +
                ", angle=" + angle +
                '}';
    }
}
