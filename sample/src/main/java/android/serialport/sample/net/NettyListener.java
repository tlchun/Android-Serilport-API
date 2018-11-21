package android.serialport.sample.net;

import io.netty.buffer.ByteBuf;

public interface NettyListener {

    byte STATUS_CONNECT_SUCCESS = 1;
    byte STATUS_CONNECT_CLOSED = 0;
    byte STATUS_CONNECT_ERROR = 0;

    void onMessageResponse(ByteBuf byteBuf);

    public void onServiceStatusConnectChanged(int statusCode);

}
