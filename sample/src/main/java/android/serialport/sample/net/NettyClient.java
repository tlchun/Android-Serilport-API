package android.serialport.sample.net;

import android.content.Context;
import android.content.SharedPreferences;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;


public class NettyClient {

    private EventLoopGroup work;
    private NettyListener listener;
    private boolean isConnect = false;
    private int reconnectNum = Integer.MAX_VALUE;
    private long reconnectIntervalTime = 5000;
    private ChannelFuture futrue;
    private static String host = "58.61.143.133";
    private static int port = 1620;
    private static Context context;

    private NettyClient() {
        if (SingletonHolder.instance != null) {
            throw new IllegalStateException();
        }
    }
    private static class SingletonHolder {
        private static NettyClient instance = new NettyClient();
    }

    public static NettyClient getInstance(Context c) {
        if(context == null){
            context =  c;
            SharedPreferences sp  = c.getSharedPreferences("info",Context.MODE_PRIVATE);
            host = sp.getString("ip","58.61.143.133");
            port = sp.getInt("port",1620);
        }
        return SingletonHolder.instance;
    }

    public synchronized NettyClient connect() {

        if (!isConnect) {
            work = new NioEventLoopGroup();
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(work);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new NettyClientInitializer(listener,context));
            try {
                futrue = bootstrap.connect(new InetSocketAddress(host,port)).sync();
                isConnect = true;
                futrue.channel().closeFuture().sync();
                isConnect = false;
            } catch (Exception e) {
                isConnect = false;
                listener.onServiceStatusConnectChanged(NettyListener.STATUS_CONNECT_ERROR);
                reconnect();
            }
        }
        return this;
    }

    public ChannelFuture getFutrue() {
        return futrue;
    }

    public void disconnect() {
        work.shutdownGracefully();
    }

    public void reconnect() {
        if (reconnectNum > 0 && !isConnect) {
            reconnectNum--;
            try {
                Thread.sleep(reconnectIntervalTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //Timber.e("重新连接");
            disconnect();
            connect();
        } else {
            disconnect();
        }
    }

    public synchronized boolean sendMsgToServer(byte[] data, ChannelFutureListener listener) {
        boolean flag = futrue != null && isConnect;
        if (flag) {
            ByteBuf buf = Unpooled.copiedBuffer(data);
            futrue.channel().writeAndFlush(buf).addListener(listener);
        }
        return flag;
    }
    public boolean sendMsgToServer(String json, ChannelFutureListener listener) {
        boolean flag = futrue != null && isConnect;
        if (flag) {
            futrue.channel().writeAndFlush(json).addListener(listener);
        }
        return flag;
    }

    public void sendMsgToServer(String json) {
        if (futrue != null && isConnect) {
            System.out.println("sendMsgToServer "+json);
            futrue.channel().writeAndFlush(json);
        }
        System.out.println("sendMsgToServer futrue"+futrue+" isConnect "+isConnect);
    }

    public void setReconnectNum(int reconnectNum) {
        this.reconnectNum = reconnectNum;
    }

    public void setReconnectIntervalTime(long reconnectIntervalTime) {
        this.reconnectIntervalTime = reconnectIntervalTime;
    }

    public boolean getConnectStatus() {
        return isConnect;
    }

    public void setConnectStatus(boolean status) {
        this.isConnect = status;
    }

    public void setListener(NettyListener listener) {
        this.listener = listener;
    }
}
