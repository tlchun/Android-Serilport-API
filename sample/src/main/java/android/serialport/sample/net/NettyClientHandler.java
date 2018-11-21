package android.serialport.sample.net;

import android.content.Context;
import android.content.Intent;
import android.serialport.sample.models.WeightModel;


import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;


public class NettyClientHandler extends ChannelHandlerAdapter{

    private NettyListener listener;
    private Context mContext;

    public NettyClientHandler(NettyListener listener, Context context) {
        this.listener = listener;
        this.mContext = context;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        NettyClient.getInstance(mContext).setConnectStatus(true);
        listener.onServiceStatusConnectChanged(NettyListener.STATUS_CONNECT_SUCCESS);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        NettyClient.getInstance(mContext).setConnectStatus(false);
        listener.onServiceStatusConnectChanged(NettyListener.STATUS_CONNECT_CLOSED);
        NettyClient.getInstance(mContext).reconnect();
    }



    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
       // listener.onMessageResponse(msg);
        String str = (String)msg;
        System.out.println("channelRead=============== "+str);
        if(str != null && mContext != null){
            WeightModel weightModel = new Gson().fromJson(str,WeightModel.class);
            if(weightModel != null){
                EventBus.getDefault().post(weightModel);
            }
        }
    }
}
