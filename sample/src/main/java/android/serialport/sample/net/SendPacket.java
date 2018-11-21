package android.serialport.sample.net;

import java.io.Serializable;

/**
 * Created by lc on 2018/11/4.
 */

public class SendPacket implements Serializable{

    private String mac;
    private int type;
    private float num;
    private long time;

    public SendPacket(String mac,int type,float num,long time){

        this.mac = mac;
        this.type = type;
        this.num = num;
        this.time = time;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getNum() {
        return num;
    }

    public void setNum(float num) {
        this.num = num;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "SendPacket{" +
                "mac='" + mac + '\'' +
                ", type=" + type +
                ", num=" + num +
                ", time=" + time +
                '}';
    }
}
