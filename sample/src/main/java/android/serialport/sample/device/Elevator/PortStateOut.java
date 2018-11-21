package android.serialport.sample.device.Elevator;

import java.io.Serializable;

/**
 * Created by lc on 2018/11/3.
 */

public class PortStateOut implements Serializable{
    private byte d0l;
    private byte d0h;

    private byte d1l;
    private byte d1h;

    private byte d2l;
    private byte d2h;

    private byte d3l;
    private byte d3h;

    private int d0;
    private int d1;
    private int d2;
    private int d3;

    public PortStateOut(byte[] data){

        d0l = data[2];
        d0h = data[3];

        d1l = data[4];
        d1h = data[5];

        d2l = data[6];
        d2h = data[7];

        d3l = data[8];
        d3h = data[9];

        d0 = (d0h<<8)|d0l;
        d1 = (d1h<<8)|d1l;
        d2 = (d2h<<8)|d2l;
        d3 = (d3h<<8)|d3l;

    }
    public PortStateOut(String str){
        System.out.println("PortStateOut=== "+str);
        String strd0l = str.substring(4,6);
        String strd0h = str.substring(6,8);
        System.out.println("strd0h== "+strd0h+" strd0l=="+strd0l);
        d0h = Byte.parseByte(strd0h);
        d0l = Byte.parseByte(strd0l);
        d0 = (d0h<<8)|d0l;
    }

    public byte getD0l() {
        return d0l;
    }

    public void setD0l(byte d0l) {
        this.d0l = d0l;
    }

    public byte getD0h() {
        return d0h;
    }

    public void setD0h(byte d0h) {
        this.d0h = d0h;
    }

    public byte getD1l() {
        return d1l;
    }

    public void setD1l(byte d1l) {
        this.d1l = d1l;
    }

    public byte getD1h() {
        return d1h;
    }

    public void setD1h(byte d1h) {
        this.d1h = d1h;
    }

    public byte getD2l() {
        return d2l;
    }

    public void setD2l(byte d2l) {
        this.d2l = d2l;
    }

    public byte getD2h() {
        return d2h;
    }

    public void setD2h(byte d2h) {
        this.d2h = d2h;
    }

    public byte getD3l() {
        return d3l;
    }

    public void setD3l(byte d3l) {
        this.d3l = d3l;
    }

    public byte getD3h() {
        return d3h;
    }

    public void setD3h(byte d3h) {
        this.d3h = d3h;
    }

    public int getD0() {
        return (d0h<<8)|d0l;
    }

    public void setD0(int d0) {
        this.d0 = d0;
    }

    public int getD1() {
        return (d1h<<8)|d1l;
    }

    public void setD1(int d1) {
        this.d1 = d1;
    }

    public int getD2() {
        return (d2h<<8)|d2l;
    }

    public void setD2(int d2) {
        this.d2 = d2;
    }

    public int getD3() {
        return (d3h<<8)|d3l;
    }

    public void setD3(int d3) {
        this.d3 = d3;
    }

    @Override
    public String toString() {
        return "PortStateOut{" +
                "d0l=" + d0l +
                ", d0h=" + d0h +
                ", d1l=" + d1l +
                ", d1h=" + d1h +
                ", d2l=" + d2l +
                ", d2h=" + d2h +
                ", d3l=" + d3l +
                ", d3h=" + d3h +
                ", d0=" + d0 +
                ", d1=" + d1 +
                ", d2=" + d2 +
                ", d3=" + d3 +
                '}';
    }
}
