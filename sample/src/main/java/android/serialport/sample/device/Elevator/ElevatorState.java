package android.serialport.sample.device.Elevator;

import android.serialport.sample.device.BuilderActorImpl;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by lc on 2018/11/3.
 */

public class ElevatorState implements Serializable{
    private static final byte ACC = 0x51;
    private static final byte AV = 0x52;
    private static final byte AG = 0x53;
    private static final byte PORT = 0x55;
    private static final byte SP_HG = 0x56;
    private static final byte PR = 0x58;


    private String mac;
    private AccelerationOut accelerationOut;
    private AngleOut angleOut;
    private AngularVelocityOut angularVelocityOut;
    private PortStateOut portStateOut;
    private SpeedHightOut speedHightOut;
    private PressureOut pressureOut;


    public ElevatorState(byte[] data){
        byte[] tmp = new byte[11];
        System.arraycopy(data,0,tmp,0,11);
        //System.out.println("BuilderActorImpl== "+ BuilderActorImpl.bytesToHexString(tmp));
        if(tmp[1]==ACC) {
            accelerationOut = new AccelerationOut(tmp);
        }
        System.arraycopy(data,11,tmp,0,11);
        //System.out.println("BuilderActorImpl== "+ BuilderActorImpl.bytesToHexString(tmp));
        if(tmp[1]==AV) {
            angularVelocityOut = new AngularVelocityOut(tmp);
        }
        System.arraycopy(data,22,tmp,0,11);
        //System.out.println("BuilderActorImpl== "+ BuilderActorImpl.bytesToHexString(tmp));
        if(tmp[1]==AG) {
            angleOut = new AngleOut(tmp);
        }
        System.arraycopy(data,33,tmp,0,11);
        //System.out.println("BuilderActorImpl== "+ BuilderActorImpl.bytesToHexString(tmp));
        if(tmp[1]==PORT) {
            portStateOut = new PortStateOut(tmp);
        }
        System.arraycopy(data,44,tmp,0,11);
        //System.out.println("BuilderActorImpl== "+ BuilderActorImpl.bytesToHexString(tmp));
        if(tmp[1]==SP_HG) {
            speedHightOut = new SpeedHightOut(tmp);
        }
        System.arraycopy(data,55,tmp,0,11);
       // System.out.println("BuilderActorImpl== "+ BuilderActorImpl.bytesToHexString(tmp));
        if(tmp[1]==PR) {
            pressureOut = new PressureOut(tmp);
        }
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public AccelerationOut getAccelerationOut() {
        return accelerationOut;
    }

    public void setAccelerationOut(AccelerationOut accelerationOut) {
        this.accelerationOut = accelerationOut;
    }

    public AngleOut getAngleOut() {
        return angleOut;
    }

    public void setAngleOut(AngleOut angleOut) {
        this.angleOut = angleOut;
    }

    public AngularVelocityOut getAngularVelocityOut() {
        return angularVelocityOut;
    }

    public void setAngularVelocityOut(AngularVelocityOut angularVelocityOut) {
        this.angularVelocityOut = angularVelocityOut;
    }

    public PortStateOut getPortStateOut() {
        return portStateOut;
    }

    public void setPortStateOut(PortStateOut portStateOut) {
        this.portStateOut = portStateOut;
    }

    public SpeedHightOut getSpeedHightOut() {
        return speedHightOut;
    }

    public void setSpeedHightOut(SpeedHightOut speedHightOut) {
        this.speedHightOut = speedHightOut;
    }

    public PressureOut getPressureOut() {
        return pressureOut;
    }

    public void setPressureOut(PressureOut pressureOut) {
        this.pressureOut = pressureOut;
    }

    public String objectToJson(){
        String json = "";

        return json;
    }

//    public static void main(String[] args){
//        byte[] data = {
//                0x55,0x51,0x30,0x00,0x19,0x00,0x1f,0x08,0x41,0x0e,0x65,
//                0x55,0x52,0x00,0x00,0x00,0x00,(byte)0xff,(byte)0xff,0x41,0x0e,(byte)0xf4,
//                0x55,0x53,0x79,0x00,0x09,(byte)0xff,0x52,0x04,0x41,0x0e,(byte)0xce,
//                0x55,0x55,0x00,0x00,0x01,0x00,0x01,0x00,0x01,0x00,(byte)0xad,
//                0x55,0x56,0x00,0x00,0x00,0x00,(byte)0xb9,0x02,0x00,0x00,0x66,
//                0x55,0x58,0x79,(byte)0x8b,0x01,0x00,0x00,0x00,0x00,0x00,(byte)0xb2
//        };
//
//        ElevatorState elevatorState = new ElevatorState(data);
//        elevatorState.setMac("00FFCCAABBDD");
//        String strGson = new Gson().toJson(elevatorState);
//
//        System.out.println("ElevatorState== "+strGson);
//    }

    @Override
    public String toString() {
        return "ElevatorState{" +
                "accelerationOut=" + accelerationOut +
                ", angleOut=" + angleOut +
                ", angularVelocityOut=" + angularVelocityOut +
                ", portStateOut=" + portStateOut +
                ", speedHightOut=" + speedHightOut +
                ", pressureOut=" + pressureOut +
                '}';
    }
}
