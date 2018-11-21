package android.serialport.sample.device.Elevator;

import java.io.Serializable;

/**
 * Created by lc on 2018/11/3.
 */

public class PressureOut implements Serializable{

    private byte p0;
    private byte p1;
    private byte p2;
    private byte p3;
    private int p;

    public PressureOut(byte[] data){
        p0 = data[2];
        p1 = data[3];
        p2 = data[4];
        p3 = data[5];

        p = ((p3<<24)|(p2<<16)|(p1<<8)|p0);
    }

    public byte getP0() {
        return p0;
    }

    public void setP0(byte p0) {
        this.p0 = p0;
    }

    public byte getP1() {
        return p1;
    }

    public void setP1(byte p1) {
        this.p1 = p1;
    }

    public byte getP2() {
        return p2;
    }

    public void setP2(byte p2) {
        this.p2 = p2;
    }

    public byte getP3() {
        return p3;
    }

    public void setP3(byte p3) {
        this.p3 = p3;
    }

    public int getP() {
        return ((p3<<24)|(p2<<16)|(p1<<8)|p0);
    }

    public void setP(int p) {
        this.p = p;
    }

    @Override
    public String toString() {
        return "PressureOut{" +
                "p0=" + p0 +
                ", p1=" + p1 +
                ", p2=" + p2 +
                ", p3=" + p3 +
                ", p=" + p +
                '}';
    }
}
