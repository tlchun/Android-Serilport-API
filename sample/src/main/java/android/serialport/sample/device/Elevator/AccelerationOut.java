package android.serialport.sample.device.Elevator;

import java.io.Serializable;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * Created by lc on 2018/11/3.
 */

public class AccelerationOut implements Serializable{

    private byte axl;
    private byte axh;

    private byte ayl;
    private byte ayh;

    private byte azl;
    private byte azh;

    private byte tl;
    private byte th;

    private double ax;
    private double ay;
    private double az;
    private double atemp;

    public AccelerationOut(byte[] data){
        axl = data[2];
        axh = data[3];

        ayl = data[4];
        ayh = data[5];

        azl = data[6];
        azh = data[7];

        tl = data[8];
        th = data[9];

        ax = (((axh<<8)|axl)/32768)*16*9.8;
        ay = (((ayh<<8)|ayl)/32768)*16*9.8;
        az = (((azh<<8)|azl)/32768)*16*9.8;

        atemp = ((th<<8)|tl)/100;
    }

    public byte getAxl() {
        return axl;
    }

    public void setAxl(byte axl) {
        this.axl = axl;
    }

    public byte getAxh() {
        return axh;
    }

    public void setAxh(byte axh) {
        this.axh = axh;
    }

    public byte getAyl() {
        return ayl;
    }

    public void setAyl(byte ayl) {
        this.ayl = ayl;
    }

    public byte getAyh() {
        return ayh;
    }

    public void setAyh(byte ayh) {
        this.ayh = ayh;
    }

    public byte getAzl() {
        return azl;
    }

    public void setAzl(byte azl) {
        this.azl = azl;
    }

    public byte getAzh() {
        return azh;
    }

    public void setAzh(byte azh) {
        this.azh = azh;
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

    public double getAx() {
        return (((axh<<8)|axl)/32768)*16*9.8;
    }

    public void setAx(double ax) {
        this.ax = ax;
    }

    public double getAy() {
        return (((ayh<<8)|ayh)/32768)*16*9.8;
    }

    public void setAy(double ay) {
        this.ay = ay;
    }

    public double getAz() {
        return (((azh<<8)|azh)/32768)*16*9.8;
    }

    public void setAz(double az) {
        this.az = az;
    }

    public double getAtemp() {
        return ((th<<8)|tl)/100;
    }

    public void setAtemp(double atemp) {
        this.atemp = atemp;
    }


    @Override
    public String toString() {
        return "AccelerationOut{" +
                "axl=" + axl +
                ", axh=" + axh +
                ", ayl=" + ayl +
                ", ayh=" + ayh +
                ", azl=" + azl +
                ", azh=" + azh +
                ", tl=" + tl +
                ", th=" + th +
                ", ax=" + ax +
                ", ay=" + ay +
                ", az=" + az +
                ", atemp=" + atemp +
                '}';
    }
}
