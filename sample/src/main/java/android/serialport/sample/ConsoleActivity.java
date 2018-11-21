/*
 * Copyright 2009 Cedric Priscal
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package android.serialport.sample;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.serialport.sample.device.BuilderActor;
import android.serialport.sample.device.BuilderActorImpl;
import android.serialport.sample.device.WeightDirector;

import android.serialport.sample.models.WeightModel;
import android.serialport.sample.net.NettyService;
import android.serialport.sample.net.SendPacket;
import android.serialport.sample.utils.NetTool;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.NotificationCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class ConsoleActivity extends SerialPortActivity{
    // ttyHSL0 115200
    // ttyHSL1 9600

    private BuilderActor builderActor;
    private WeightDirector weightDirector;
    private volatile String mac;
    private Intent intent;

    private NettyService msgService;

    private Queue<Byte> queueBuffer = new LinkedList<Byte>();
    private byte[] packBuffer = new byte[11];

    private Queue<Byte> queueBufferWeight = new LinkedList<Byte>();
    private byte[] packBufferWeight = new byte[8];

    private int pos = 1;
    //private int weight = 0;
    private int currentCount = 0;
    private int totalCount = 0;


    private TextView tv_show_weight, tv_show_rel_weight;
    private TextView tv_show_count_result,tv_show_rel_count;
    private TextView tv_show_base_weight,tv_show_base_py_weight;
    private Context mContext;
    private List<Integer> weightList = new ArrayList<>();
    private boolean isStart = false;
    Button bt_start, bt_stop;
    private volatile int minWeight = 0;
    private int beforeCount = 1;
    private volatile int count;
    private int currentCount1=0;
    private WeightModel mWeightModel = new WeightModel(1000,0);

    private MediaPlayer mediaPlayer1;
    private MediaPlayer mediaPlayer2;
    private MediaPlayer mediaPlayer3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.console);

        EventBus.getDefault().register(this);

        builderActor = new BuilderActorImpl();
        weightDirector = new WeightDirector(builderActor);

        mediaPlayer1 = new MediaPlayer();
        mediaPlayer2 = new MediaPlayer();
        mediaPlayer3 = new MediaPlayer();

        mContext = this;

        tv_show_weight = (TextView) findViewById(R.id.tv_show_weight);
        tv_show_count_result = (TextView) findViewById(R.id.tv_show_count_result);
        tv_show_rel_count = (TextView) findViewById(R.id.tv_show_rel_count);
        tv_show_rel_weight = (TextView) findViewById(R.id.tv_show_rel_weight);

        tv_show_base_py_weight= (TextView) findViewById(R.id.tv_show_base_py_weight);
        tv_show_base_weight= (TextView) findViewById(R.id.tv_show_base_weight);

        getConfig();
        showBaseConfig();

        queueBufferWeight.clear();
        intent = new Intent(this, NettyService.class);

        TextView tv_setting = (TextView) findViewById(R.id.tv_setting);
        tv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startPlayer();
                startActivity(new Intent(ConsoleActivity.this, MainMenu.class));

            }
        });

        bt_start = (Button) findViewById(R.id.bt_start);
        bt_start.setEnabled(true);

        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindService(intent, conn, Context.BIND_AUTO_CREATE);
                bt_start.setEnabled(false);
                bt_stop.setEnabled(true);
            }
        });
        bt_stop = (Button) findViewById(R.id.bt_stop);
        bt_stop.setEnabled(true);
        bt_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unbindService(conn);
                bt_start.setEnabled(true);
                bt_stop.setEnabled(false);
            }
        });
        mac= NetTool.getLocalMacAddressFromWifiInfo(this);

        bindService(intent, conn, Context.BIND_AUTO_CREATE);
        bt_start.setEnabled(false);
        bt_stop.setEnabled(true);

        initVomu3();

        scan();
        //System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
    }
    private void showBaseConfig(){
        SharedPreferences sp = getApplicationContext().getSharedPreferences("info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat("pivot_weight",mWeightModel.getPivot_weight());
        editor.putFloat("weight_deviation",mWeightModel.getWeight_deviation());
        editor.commit();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_show_base_weight.setText("基准重量:"+mWeightModel.getPivot_weight());
                tv_show_base_py_weight.setText("偏移重量:"+mWeightModel.getWeight_deviation());
            }
        });

    }
    private void getConfig() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("info", Context.MODE_PRIVATE);
        minWeight = sp.getInt("weight", 1000);

        float pivot_weight = sp.getFloat("pivot_weight",1000);
        float weight_deviation = sp.getFloat("weight_deviation",0);

        count = sp.getInt("count",1);
        if(count==0){
            count = 1;
        }
        mWeightModel.setPivot_weight(pivot_weight);
        mWeightModel.setWeight_deviation(weight_deviation);

        //System.out.println("minWeight===== " + minWeight);
    }
    @Subscribe
    public void onEventMainThread(WeightModel weightModel){
         if(weightModel != null){
             //mWeightModel = weightModel;
             mWeightModel.setPivot_weight(weightModel.getPivot_weight()*1000);
             mWeightModel.setWeight_deviation(weightModel.getWeight_deviation()*1000);
             //System.out.println("mWeightModel== "+mWeightModel.toString());
             //System.out.println("mWeightModel== weightModel "+weightModel.toString());
             showBaseConfig();
         }
    }
    @Override
    protected void onResume() {
        super.onResume();
        getConfig();
        //startPlayer();
    }
    private void initVomu3(){
        File file1 = new File(Environment.getExternalStorageDirectory(), "Music/weight_s.mp3");
        try {
            mediaPlayer3.setDataSource(file1.getPath());
            mediaPlayer3.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file2 = new File(Environment.getExternalStorageDirectory(), "Music/weight_m.mp3");
        try {
            mediaPlayer2.setDataSource(file2.getPath());
            mediaPlayer2.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file3 = new File(Environment.getExternalStorageDirectory(), "Music/weight_l.mp3");
        try {
            mediaPlayer1.setDataSource(file3.getPath());
            mediaPlayer1.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void openVomu3( ){
        if(!mediaPlayer3.isPlaying()) {
            mediaPlayer3.start();
        }else {
            mediaPlayer3.reset();
            mediaPlayer3.start();
        }
    }

    private void openVomu2( ){
        if(!mediaPlayer2.isPlaying()) {
            //System.out.println("openVomu2======= 1111");
            mediaPlayer2.start();
        }else{
            //System.out.println("openVomu2======= 2222");
            mediaPlayer2.reset();
            mediaPlayer2.start();
        }
    }

    private void openVomu1(){
        if(!mediaPlayer1.isPlaying()) {
            mediaPlayer1.start();
        }else{
            mediaPlayer1.reset();
            mediaPlayer1.start();
        }
    }



    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //返回一个MsgService对象
            //System.out.println("onServiceConnected");
            msgService = ((NettyService.MsgBinder) service).getService();
        }
    };

    @Override
    protected void onDataReceivedTTySHL1(byte[] buffer, int size) {
        for (int i = 0; i < size; i++) {
            queueBufferWeight.add(buffer[i]);
        }
        //System.out.println("onDataReceivedTTySHL1 size ==== "+queueBufferWeight.size());
        //System.out.println("queueBufferWeight buffer=== "+BuilderActorImpl.bytesToHexString(buffer));
        while (true) {
            while (queueBufferWeight.size() >= 9) {
                byte head = queueBufferWeight.poll();
                if (head != 0x01) {
                    continue;
                }
                //System.out.println("head======="+head);
                byte head2 = queueBufferWeight.poll();
                //System.out.println("head2======="+head2);
                if (head2 != 0x03) {
                    continue;
                }
                byte length = queueBufferWeight.poll();
                for (int j = 0; j < 6; j++) {
                    packBufferWeight[j] = queueBufferWeight.poll();
                }
                int w = NetTool.byteArrayToInt(packBufferWeight[0], packBufferWeight[1], packBufferWeight[2], packBufferWeight[3]);
                //System.out.println("当前的重量== "+w);
                showRelWeight(w);
                if (w > minWeight) {
                    weightList.add(w);
                } else {
                    //发送数据
                    int weight = getWeight();
                    //System.out.println("weight===== "+weight);
                    if (weight < minWeight) {
                        continue;
                    }
                    float temp1 = mWeightModel.getPivot_weight()-mWeightModel.getWeight_deviation();
                    //System.out.println("temp1=== "+temp1+" w== "+w);
                    if(temp1>weight){
                        // 过轻
                        //System.out.println("aaaaaaaa==== 过轻");
                        openVomu1( );
                    }
                    float temp2 = mWeightModel.getPivot_weight()+mWeightModel.getWeight_deviation();
                    //System.out.println("temp2=== "+temp2+" w== "+w);
                    if(temp2<weight){
                        // 过重
                        //System.out.println("aaaaaaaa==== 过重");
                        openVomu3();
                    }
                    if(weight>temp1 && weight< temp2){
                        openVomu2();
                    }
                    System.out.println("weight send ===== "+weight);
                    if (mac == null) {
                        mac = NetTool.getLocalMacAddressFromWifiInfo(ConsoleActivity.this);
                    }
                    System.out.println("weight send mac ===== "+mac);
                    if (mac != null && !"".equals(mac)) {
                        //DecimalFormat df=new DecimalFormat("0.00");
                        float sendWeight = weight / (float) 1000;
                        SendPacket sendPacket = new SendPacket(mac, 2, sendWeight, System.currentTimeMillis());
                        if (msgService != null) {
                            System.out.println("send weight to server enter " + weight);
                            msgService.sendMessageToService(sendPacket);
                            showEndWeight(weight);

                           // openVomu2();
                        }else{
                            System.out.println("msgService is null");
                        }
                    }
                    weightList.clear();
                }
            }
            return;
        }
    }

    @Override
    protected void onDataReceivedTTySHL0(byte[] buffer, int size) {
        for (int i = 0; i < size; i++) {
            queueBuffer.add(buffer[i]);
        }
        //System.out.println("onDataReceivedTTySHL0 size ==== "+queueBuffer.size());
        while (true) {
            //判断队列的数据是否大于11
            while (queueBuffer.size() >= 11) {
                if ((queueBuffer.poll()) != 0x55) {
                    continue;
                }
                byte sHead = queueBuffer.poll();
                for (int j = 0; j < 9; j++) {
                    packBuffer[j] = queueBuffer.poll();
                }
                switch (sHead) {
                    case 0x50:
                        //System.out.println("packBuffer== " + BuilderActorImpl.bytesToHexString(packBuffer));
                        break;
                    case 0x51:
                        //System.out.println("packBuffer==51 " + BuilderActorImpl.bytesToHexString(packBuffer));
                        break;
                    case 0x52:
                        //System.out.println("packBuffer==52 " + BuilderActorImpl.bytesToHexString(packBuffer));
                        break;
                    case 0x53:
                        //System.out.println("packBuffer==53 " + BuilderActorImpl.bytesToHexString(packBuffer));
                        break;
                    case 0x55:
                        //System.out.println("sHead=== " + sHead + " packBuffer " + BuilderActorImpl.bytesToHexString(packBuffer));
                        int num = ((((short) packBuffer[1]) << 8) | ((short) packBuffer[0] & 0xff));
                        //System.out.println("sHead=== num== " + num);
                        showDataStatu(num);
                        if(num == 1){
                            if(currentCount1>= count){
                                //提交数据
                                if (mac == null) {
                                    mac = NetTool.getLocalMacAddressFromWifiInfo(ConsoleActivity.this);
                                }
                                if (mac != null && !"".equals(mac)) {
                                    SendPacket sendPacket = new SendPacket(mac, 1, 1, System.currentTimeMillis());
                                    if (msgService != null) {
                                        totalCount++;
                                        msgService.sendMessageToService(sendPacket);
                                        showData();
                                    }else{
                                        System.out.println("msgService is null");
                                    }
                                }
                                //System.out.println("currentCount1== submit data to server");
                                isStart = false;
                                currentCount1 = 0;
                            }else{
                                //开始计数后，出现1，才累加
                                if(isStart) {
                                    //System.out.println("currentCount1== "+currentCount1);
                                    currentCount1++;
                                }
                            }
                        }else{
                            //触发一次计
                            currentCount1 = 0;
                            isStart = true;
                        }
                    case 0x56:
                        //System.out.println("packBuffer==56 " + BuilderActorImpl.bytesToHexString(packBuffer));
                        break;
                    case 0x58:
                        //System.out.println("packBuffer==58 " + BuilderActorImpl.bytesToHexString(packBuffer));
                        break;
                }
            }
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

        unbindService(conn);
        shutdown();
        //stopSerialPortHSL0();
        //stopSerialPortHSL1();
        if (mediaPlayer1 != null) {
            mediaPlayer1.stop();
            mediaPlayer1.release();
        }
        if (mediaPlayer2 != null) {
            mediaPlayer2.stop();
            mediaPlayer2.release();
        }
        if (mediaPlayer2 != null) {
            mediaPlayer2.stop();
            mediaPlayer2.release();
        }
    }

    private ScheduledExecutorService mScheduledExecutorService;

    private void shutdown() {
        if (mScheduledExecutorService != null) {
            mScheduledExecutorService.shutdown();
            mScheduledExecutorService = null;
        }
    }

    private void scan() {
        mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        mScheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mOutputStreamHSL1 != null) {
                        mOutputStreamHSL1.write(weightDirector.contructReadGW());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 20, 1000, TimeUnit.MILLISECONDS);
    }

    private void showDataStatu(final int num) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_show_rel_count.setText("当前状态:" + num);
            }
        });
    }

    private void showData() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_show_count_result.setText("当前计数:" + totalCount);
            }
        });
    }

    private void showEndWeight(final int weight) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_show_weight.setText("重量：" + weight);
            }
        });
    }

    private void showRelWeight(final int relWeight) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_show_rel_weight.setText("实时重量:" + relWeight);
            }
        });
    }

    private int getWeight() {
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (int i = 0; i < weightList.size(); i++) {
            if (map.containsKey(weightList.get(i))) {
                int temp = map.get(weightList.get(i));
                map.put(weightList.get(i), temp + 1);
            } else {
                map.put(weightList.get(i), 1);
            }
        }
        int count = -1;
        int max = Integer.MIN_VALUE;
        Iterator<Map.Entry<Integer, Integer>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Integer, Integer> entry = iter.next();
            if (entry.getValue() > count || (entry.getValue() == count && entry.getKey() > max)) {
                max = entry.getKey();
                count = entry.getValue();
            }
        }
        return max;
    }
}
