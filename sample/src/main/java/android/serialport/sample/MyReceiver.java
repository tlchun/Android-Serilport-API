package android.serialport.sample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by lc on 2018/11/5.
 */

public class MyReceiver extends BroadcastReceiver {

    public MyReceiver(){

    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.permission.RECEIVE_BOOT_COMPLETED")){
            Intent intent1 = new Intent(context,ConsoleActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
