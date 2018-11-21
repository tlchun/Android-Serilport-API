package android.serialport.sample.device.Elevator;

import java.io.Serializable;

/**
 * Created by lc on 2018/11/3.
 */

public class SpeedHightOut implements Serializable{
    private byte vl;
    private byte vh;

    private byte h0;
    private byte h1;
    private byte h2;
    private byte h3;

    private double speed;
    private double height;

    public SpeedHightOut(byte[] data){

        vl = data[2];
        vh = data[3];

        h0 = data[6];
        h1 = data[7];
        h2 = data[8];
        h3 = data[9];

        speed = ((vh<<8)|vl)/1000;
        height = ((h3<<24)|(h2<<16)|(h1<<8)|h0)/100;
    }
    public byte getVl() {
        return vl;
    }

    public void setVl(byte vl) {
        this.vl = vl;
    }

    public byte getVh() {
        return vh;
    }

    public void setVh(byte vh) {
        this.vh = vh;
    }

    public byte getH1() {
        return h1;
    }

    public void setH1(byte h1) {
        this.h1 = h1;
    }

    public byte getH2() {
        return h2;
    }

    public void setH2(byte h2) {
        this.h2 = h2;
    }

    public byte getH3() {
        return h3;
    }

    public void setH3(byte h3) {
        this.h3 = h3;
    }

    public double getSpeed() {
        return ((vh<<8)|vl)/1000;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getHeight() {
        return ((h3<<24)|(h2<<16)|(h1<<8)|h0)/100;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "SpeedHightOut{" +
                "vl=" + vl +
                ", vh=" + vh +
                ", h0=" + h0 +
                ", h1=" + h1 +
                ", h2=" + h2 +
                ", h3=" + h3 +
                ", speed=" + speed +
                ", height=" + height +
                '}';
    }
}
