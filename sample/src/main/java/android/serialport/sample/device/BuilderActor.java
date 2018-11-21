package android.serialport.sample.device;

public interface BuilderActor {

    //模块地址
    public void setModeAddress(byte modeAddress);

    //功能代码
    public void setFunctionAction(byte action);

    //寄存器起始地址
    public void setRegisterStartAddress(byte[] address);

    //寄存器数量
    public void setRegisterNum(byte[] num);

    //构建命令数据
    public byte[] getSendByte();

}
