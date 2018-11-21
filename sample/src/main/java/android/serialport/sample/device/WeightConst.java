package android.serialport.sample.device;

public class WeightConst {

    //读寄存器
    public final static byte READ_ACTION= 0x03;
    //写寄存器
    public final static byte WRITE_ACTION  = 0x10;
    //读错误
    public final static byte WRITE_ERROR = (byte) 0x83;
    //写错误
    public final static byte READ_ERROR = (byte) 0x90;

    //0103 00 50 0002 C41A

    static class RegisterAddressList {

        //模块地址
        public final static byte[] MODE_ADDRESS = {0x00, 0x00};//1-247 默认值1

        //波特率设置 0x00:1200,0x01:2400,0x02:4800,0x03:9600,0x04:19200,0x05:38400,0x06:57600,0x07:115200,0x08:230400
        public final static byte[] BPS = {0x00, 0x01};

        //数据帧格式
        //0x03(8位数据位，偶校验，1位停止位),
        //0x04(8位数据位，奇校验，1位停止位),
        //0x05(8位数据位，无校验，1位停止位),
        //0x06(8位数据位，无校验，2位停止位)
        public final static byte[] DATA_FORMAT = {0x00, 0x02};

        //协议类型 0x00:自由协议，0x01: Modbus rtu
        public final static byte[] PROTOCOL_TYPE = {0x00, 0x03};

        //指令应答延迟，用于RS485通信时有些主机收发切换慢，导致指令丢失，单位：ms，范围：0-255；0为不延迟
        public final static byte[] CMD_RESP_DELAY = {0x00, 0x04};

        //锁定/解锁系统配置
        public final static byte[] LOCK_UNLOCK_SYS_SETTING = {0x00, 0x05};

        //模块内部软件版本
        public final static byte[] MODE_SOFTWARE_VERSION = {0x00, 0x06};

        //回复出厂设置
        public final static byte[] FACTORY_DEFAULT = {0x00, 0x07};

        //测量值
        public final static byte[] MEASURED_VALUE = {0x1e, 0x1f};//80-50 81-51 00 50

        //AD转换速度
        public final static byte[] AD = {0x00, 0x20};

        //拉压双向
        public final static byte[] CFBLS = {0x00, 0x21};

        //滤波类型
        public final static byte[] FILTRATE_TYPE = {0x00, 0x22};

        //滤波强度 0-50 数字越大,滤波越强
        public final static byte[] FILTRATE_STRENGTH = {0x00, 0x23};

        //零点内码值
        public final static byte[] ZERO_ISN = {0x24, 0x25};

        //零点测量值
        public final static byte[] ZERO_MEASURED_VALUE = {0x26, 0x27};

        //增益内码值
        public final static byte[] GAIN_ISN_VALUE = {0x28, 0x29};
        //增益测量值
        public final static byte[] GAIN_MEASURED_VALUE = {0x30, 0x31};

        //读取AD内码值 AD转换经过滤波后的原始码
        public final static byte[] READ_AD_ISN_VALUE = {0x32, 0x33};

        //多点修正 此寄存器为只写，写入任何非零值关闭多点修正，读此寄存器将返回0
        public final static byte[] MUTIL_POINT_CORRECTION = {0x00, 0x3c};

        //多点修正数量 此寄存器为只读，读取此寄存器返回内部多点修正数量，写此寄存器无效
        public final static byte[] MUTIL_POINT_CORRECTION_NUM = {0x00, 0x3c};

        //第N点对应的AD内码值
        public final static byte[] N_AD_VALUE = {0x3e, 0x3f};
        //第N点对应的AD内码值
        public final static byte[] N_MEASURED_VALUE = {0x40, 0x41};
        //第N点对应的AD内码值
        public final static byte[] N_CORRECTION_VALUE = {0x42, 0x43};

        //毛重起始地址
        public final static byte[] GW_1 = {0x00, 0x50};
        public final static byte[] GW_2 = {0x00, 0x51};

        //净重起始地址
        public final static byte[] NW_1 = {0x00, 0x52};
        public final static byte[] NW_2 = {0x00, 0x53};

        //皮重起始地址
        public final static byte[] TW_1 = {0x00, 0x54};
        public final static byte[] TW_2 = {0x00, 0x55};

        //最大称重
        public final static byte[] MW_1 = {0x00, 0x54};
        public final static byte[] MW_2 = {0x00, 0x54};

        //称台分度
        public final static byte[] SWP = {0x00, 0x58};

        //零点标定时砝码重量
        public final static byte[] ZERO_POINT_W1 = {0x00, 0x59};
        public final static byte[] ZERO_POINT_W2 = {0x00, 0x5A};

        //增益标定时砝码重量
        public final static byte[] GAIN_POINT_W1 = {0x00, 0x5B};
        public final static byte[] GAIN_POINT_W2 = {0x00, 0x5C};

        //手动置零范围
        public final static byte[]  MANUAL_ZERO_SCOPE= {0x00, 0x5D};
        //执行手动置零
        public final static byte[]  MANUAL_ZERO_ACTION= {0x00, 0x5E};
        //开机置零范围
        public final static byte[]  START_UP_ZERO_SCOPE= {0x00, 0x5F};

        //自动零位跟踪范围
        public final static byte[]  AUTO_ZERO_TAIL_SCOPE= {0x00, 0x60};
        //自动零位跟踪时间
        public final static byte[]  AUTO_ZERO_TAIL_TIME= {0x00, 0x61};

    }
}
