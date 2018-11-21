package android.serialport.sample.device.Elevator;

import java.io.Serializable;

/**
 * Created by lc on 2018/11/3.
 */

public class AngularVelocityOut implements Serializable{
    private byte wxl;
    private byte wxh;

    private byte wyl;
    private byte wyh;

    private byte wzl;
    private byte wzh;

    private byte tl;
    private byte th;

    private double wx;
    private double wy;
    private double wz;
    private double wtemp;

    public AngularVelocityOut(byte[] data){

        wxl = data[2];
        wxh = data[3];

        wyl = data[4];
        wyh = data[5];

        wzl = data[6];
        wzh = data[7];

        tl = data[8];
        th = data[9];

        wx = (((wxh<<8)|wxl)/32768)*2000;
        wy = (((wyh<<8)|wyl)/32768)*2000;
        wz = (((wzh<<8)|wzl)/32768)*2000;

        wtemp = ((th<<8)|tl)/100;
    }

    public byte getWxl() {
        return wxl;
    }

    public void setWxl(byte wxl) {
        this.wxl = wxl;
    }

    public byte getWxh() {
        return wxh;
    }

    public void setWxh(byte wxh) {
        this.wxh = wxh;
    }

    public byte getWyl() {
        return wyl;
    }

    public void setWyl(byte wyl) {
        this.wyl = wyl;
    }

    public byte getWyh() {
        return wyh;
    }

    public void setWyh(byte wyh) {
        this.wyh = wyh;
    }

    public byte getWzl() {
        return wzl;
    }

    public void setWzl(byte wzl) {
        this.wzl = wzl;
    }

    public byte getWzh() {
        return wzh;
    }

    public void setWzh(byte wzh) {
        this.wzh = wzh;
    }

    public byte getTl() {
        return tl;
    }

    public void setTl(byte tl) {
        this.tl = tl;
    }

    public byte getTh() {
        return th;
    }

    public void setTh(byte th) {
        this.th = th;
    }

    public double getWx() {
        return (((wxh<<8)|wxl)/32768)*2000;
    }

    public void setWx(double wx) {
        this.wx = wx;
    }

    public double getWy() {
        return (((wyh<<8)|wyl)/32768)*2000;
    }

    public void setWy(double wy) {
        this.wy = wy;
    }

    public double getWz() {
        return (((wzh<<8)|wzl)/32768)*2000;
    }

    public void setWz(double wz) {
        this.wz = wz;
    }

    public double getWtemp() {
        return ((th<<8)|tl)/100;
    }

    public void setWtemp(double wtemp) {
        this.wtemp = wtemp;
    }

    @Override
    public String toString() {
        return "AngularVelocityOut{" +
                "wxl=" + wxl +
                ", wxh=" + wxh +
                ", wyl=" + wyl +
                ", wyh=" + wyh +
                ", wzl=" + wzl +
                ", wzh=" + wzh +
                ", tl=" + tl +
                ", th=" + th +
                ", wx=" + wx +
                ", wy=" + wy +
                ", wz=" + wz +
                ", wtemp=" + wtemp +
                '}';
    }
}
