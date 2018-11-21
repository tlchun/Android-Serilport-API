package android.serialport.sample.device;

public class WeightDirector {

    private BuilderActor builderActor;

    public WeightDirector(BuilderActor builderActor) {
        this.builderActor = builderActor;
    }

    //读模块地址
    public byte[] constructReadModeAddress() {

        builderActor.setModeAddress((byte) 0x01);
        builderActor.setFunctionAction(WeightConst.READ_ACTION);
        builderActor.setRegisterStartAddress(WeightConst.RegisterAddressList.MODE_ADDRESS);
        builderActor.setRegisterNum(new byte[]{0x00, 0x02});

        return builderActor.getSendByte();
    }

    //写模块地址
    public byte[] constructWriteModeAddress() {

        builderActor.setModeAddress((byte) 0x01);
        builderActor.setFunctionAction(WeightConst.WRITE_ACTION);
        builderActor.setRegisterStartAddress(WeightConst.RegisterAddressList.MODE_ADDRESS);
        builderActor.setRegisterNum(new byte[]{0x00, 0x02});

        return builderActor.getSendByte();
    }

    //读毛重
    public byte[] contructReadGW() {
        builderActor.setModeAddress((byte) 0x01);
        builderActor.setFunctionAction(WeightConst.READ_ACTION);//0x03
        builderActor.setRegisterStartAddress(WeightConst.RegisterAddressList.GW_1);//00 50
        builderActor.setRegisterNum(new byte[]{0x00, 0x02});
        return builderActor.getSendByte();
    }
}
