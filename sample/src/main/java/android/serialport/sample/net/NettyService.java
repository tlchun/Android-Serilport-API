package android.serialport.sample.net;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.serialport.sample.JumpActivity;
import android.serialport.sample.R;
import android.serialport.sample.utils.NetTool;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;


import com.google.gson.Gson;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

public class NettyService extends Service implements NettyListener {

    private NetworkReceiver receiver;
    private static String sessionId = null;
    private SendPacket heartPacket;
//    private SerialPort serialPortTtyHSL0;
//    private SerialPort serialPortTtyHSL1;
    private static final int NOTICE_ID = 100;

    private ScheduledExecutorService mScheduledExecutorService;

    private void shutdown() {
        if (mScheduledExecutorService != null) {
            mScheduledExecutorService.shutdown();
            mScheduledExecutorService = null;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        receiver = new NetworkReceiver();

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);

        System.out.println("BBBBBB=== onCreate");

        //自定义心跳，每隔20秒向服务器发送心跳包
        mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        mScheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                //写死了
                if(heartPacket == null) {
                    heartPacket = new SendPacket(NetTool.getLocalMacAddressFromWifiInfo(getApplicationContext()), 3, 0, System.currentTimeMillis());
                }
                String json =  new Gson().toJson(heartPacket)+"#";
                NettyClient.getInstance(getApplicationContext()).sendMsgToServer(json, new ChannelFutureListener() {    //3
                    @Override
                    public void operationComplete(ChannelFuture future) {
                        if (future.isSuccess()) {                //4
                            //Timber.d("Write heartbeat successful");
                            System.out.println("Write heartbeat successful");
                        } else {
                            System.out.println("Write heartbeat error");
                            //Timber.e("Write heartbeat error");
                            //WriteLogUtil.writeLogByThread("heartbeat error");
                        }
                    }
                });
            }
        }, 20, 20, TimeUnit.SECONDS);

        NettyClient.getInstance(getApplicationContext()).setListener(this);
        connect();
        setForeground();
    }

    public synchronized void sendMessageToService(SendPacket sendPacket){
        System.out.println("json=== sendPacket "+sendPacket.toString());
        String json =  new Gson().toJson(sendPacket)+"#";
        System.out.println("json=== "+json);
        NettyClient.getInstance(getApplicationContext()).sendMsgToServer(json);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_NOT_STICKY ;
    }

    @Override
    public void onServiceStatusConnectChanged(int statusCode) {        //连接状态监听
        //Timber.d("connect status:%d", statusCode);
        if (statusCode == NettyListener.STATUS_CONNECT_SUCCESS) {
            //authenticData();
        } else {
            //WriteLogUtil.writeLogByThread("tcp connect error");
        }
    }

    @Override
    public void onMessageResponse(ByteBuf byteBuf) {
        //处理服务器回传的信息
    }

    private void connect() {
        if (!NettyClient.getInstance(getApplicationContext()).getConnectStatus()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    NettyClient.getInstance(getApplicationContext()).connect();//连接服务器
                }
            }).start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        shutdown();
        NettyClient.getInstance(getApplicationContext()).setReconnectNum(0);
        NettyClient.getInstance(getApplicationContext()).disconnect();
        //stopForeground(true);
        System.out.println("NettyService onDestroy");
    }
    public class NetworkReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null) { // connected to the internet
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI
                        || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    connect();
                    System.out.println("network is ok");
                }
            }
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return new MsgBinder();
    }

    public class MsgBinder extends Binder {
        /**
         * 获取当前Service的实例
         * @return
         */
        public NettyService getService(){
            return NettyService.this;
        }
    }

    private void setForeground(){
        Intent intent = new Intent(this, JumpActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("标题")
                .setContentText("内容")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.icon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.icon))
                .setContentIntent(pi)
                .build();
        startForeground(NOTICE_ID, notification);
    }
//    public void sendDataToDevice(byte[] array,boolean flag){
//        if(flag) {
//            if (serialPortTtyHSL0 != null) {
//                serialPortTtyHSL0.send(array);
//            }else{
//                System.out.println("SerialPort TtyHSL0 is null");
//            }
//        }else{
//            if (serialPortTtyHSL1 != null) {
//                serialPortTtyHSL1.send(array);
//            }else{
//                System.out.println("SerialPort TtyHSL1 is null");
//            }
//        }
//    }
}
