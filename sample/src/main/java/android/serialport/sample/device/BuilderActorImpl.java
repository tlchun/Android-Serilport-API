package android.serialport.sample.device;

import java.nio.ByteBuffer;

public class BuilderActorImpl implements BuilderActor {

    private byte modeAddress;
    private byte action;
    private byte[] registerStart;
    private byte[] registerNum;


    @Override
    public void setModeAddress(byte modeAddress) {
        this.modeAddress = modeAddress;
    }

    @Override
    public void setFunctionAction(byte action) {
        this.action = action;
    }

    @Override
    public void setRegisterStartAddress(byte[] address) {
        this.registerStart = address;
    }

    @Override
    public void setRegisterNum(byte[] num) {
        this.registerNum = num;
    }

    @Override
    public byte[] getSendByte() {

        ByteBuffer buffer = ByteBuffer.allocate(6);
        buffer.put(modeAddress);
        buffer.put(action);
        buffer.put(registerStart);
        buffer.put(registerNum);

        //计算CRC16
        //getCRC();
        byte[] crc = getCRC(buffer.array());

        ByteBuffer buffer2 = ByteBuffer.allocate(8);
        buffer2.put(buffer.array());
        buffer2.put(crc[0]);
        buffer2.put(crc[1]);

        return buffer2.array();
    }

    public static byte[] getCRC(byte[] bytes) {
        int CRC = 0x0000ffff;
        int POLYNOMIAL = 0x0000a001;
        int i, j;
        for (i = 0; i < bytes.length; i++) {
            CRC ^= ((int) bytes[i] & 0x000000ff);
            for (j = 0; j < 8; j++) {
                if ((CRC & 0x00000001) != 0) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }
        }
        //System.out.println("CRC== " + Integer.toHexString(CRC));

        byte[] crc = IntToByteArray(CRC);

        return crc;
    }

    public static byte[] IntToByteArray(int n) {

        byte[] b = new byte[2];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        //b[2] = (byte) (n >> 16 & 0xff);
        //b[3] = (byte) (n >> 24 & 0xff);

        return b;
    }

    public static String bytesToHexString(byte[] src) {

        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }



//    public static void main(String[] args) {
//
//        byte modeAddress = 0x01;
//        byte action = 0x03;
//        byte[] registerStart = {0x00, 0x50};
//        byte[] registerNum = {0x00, 0x02};
//
//        ByteBuffer buffer = ByteBuffer.allocate(6);
//
//        buffer.put(modeAddress);
//        buffer.put(action);
//        buffer.put(registerStart);
//        buffer.put(registerNum);
//
//        byte[] crc = getCRC(buffer.array());
//
//        ByteBuffer buffer2 = ByteBuffer.allocate(8);
//        buffer2.put(buffer.array());
//        buffer2.put(crc[0]);
//        buffer2.put(crc[1]);
//        //0103005000021ac4
//        //010300500002C41A
//        System.out.println("Hex== "+bytesToHexString(buffer2.array()));
//    }
}
