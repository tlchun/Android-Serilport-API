package android.serialport.sample.device.Elevator;

import java.io.Serializable;

/**
 * Created by lc on 2018/11/3.
 */

public class AngleOut implements Serializable{

    private byte rollL;
    private byte rollH;

    private byte pitchL;
    private byte pitchH;

    private byte yawL;
    private byte yawH;

    private byte tl;
    private byte th;

    private double roll;
    private double pitch;
    private double yaw;
    private double angleTemp;

    public AngleOut(byte[] data){
        rollL = data[2];
        rollH = data[3];

        pitchL = data[4];
        pitchH = data[5];

        yawL = data[6];
        yawH = data[7];

        tl = data[8];
        th = data[9];

        roll = ((rollH<<8)|rollL)/32768*180;
        pitch= ((pitchH<<8)|pitchL)/32768*180;
        yaw =((yawH<<8)|yawL)/32768*180;
        angleTemp = ((th<<8)|tl)/100;

    }

    public byte getRollL() {
        return rollL;
    }

    public void setRollL(byte rollL) {
        this.rollL = rollL;
    }

    public byte getRollH() {
        return rollH;
    }

    public void setRollH(byte rollH) {
        this.rollH = rollH;
    }

    public byte getPitchL() {
        return pitchL;
    }

    public void setPitchL(byte pitchL) {
        this.pitchL = pitchL;
    }

    public byte getPitchH() {
        return pitchH;
    }

    public void setPitchH(byte pitchH) {
        this.pitchH = pitchH;
    }

    public byte getYawL() {
        return yawL;
    }

    public void setYawL(byte yawL) {
        this.yawL = yawL;
    }

    public byte getYawH() {
        return yawH;
    }

    public void setYawH(byte yawH) {
        this.yawH = yawH;
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

    public double getRoll() {
        return ((rollH<<8)|rollL)/32768*180;
    }

    public void setRoll(double roll) {
        this.roll = roll;
    }

    public double getPitch() {
        return ((pitchH<<8)|pitchL)/32768*180;
    }

    public void setPitch(double pitch) {
        this.pitch = pitch;
    }

    public double getYaw() {
        return ((yawH<<8)|yawL)/32768*180;
    }

    public void setYaw(double yaw) {
        this.yaw = yaw;
    }

    public double getAngleTemp() {
        return ((th<<8)|tl)/100;
    }

    public void setAngleTemp(double angleTemp) {
        this.angleTemp = angleTemp;
    }

    @Override
    public String toString() {
        return "AngleOut{" +
                "rollL=" + rollL +
                ", rollH=" + rollH +
                ", pitchL=" + pitchL +
                ", pitchH=" + pitchH +
                ", yawL=" + yawL +
                ", yawH=" + yawH +
                ", tl=" + tl +
                ", th=" + th +
                ", roll=" + roll +
                ", pitch=" + pitch +
                ", yaw=" + yaw +
                ", angleTemp=" + angleTemp +
                '}';
    }
}
