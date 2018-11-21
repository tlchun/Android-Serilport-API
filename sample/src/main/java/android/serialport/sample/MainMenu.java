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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainMenu extends Activity {

    private EditText ev_ip;
    private EditText ev_port;
    private EditText ev_weight;
    private EditText ev_count_1;
    private SharedPreferences sp;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        ev_ip  = (EditText)findViewById(R.id.ev_ip);

        ev_port= (EditText)findViewById(R.id.ev_port);

        ev_weight= (EditText)findViewById(R.id.ev_weight);

        ev_count_1= (EditText)findViewById(R.id.ev_count_1);

        TextView tv_wifi = (TextView)findViewById(R.id.tv_wifi);
        tv_wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent0 = new Intent(Settings.ACTION_WIFI_SETTINGS  );
                startActivityForResult(intent0, 0);
            }
        });

        sp = getApplicationContext().getSharedPreferences("info", Context.MODE_PRIVATE);

        String ip = sp.getString("ip","58.61.143.133");
        int port = sp.getInt("port",1620);
        final int weight = sp.getInt("weight",1000);
        final  int count  =  sp.getInt("count",1);
        ev_ip.setText(ip);
        ev_port.setText(""+port);
        ev_weight.setText(""+weight);

        ev_count_1.setText(""+count);

        final Button buttonConsole = (Button) findViewById(R.id.ButtonConsole);
        buttonConsole.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String strIp = ev_ip.getText().toString();
                String strPort = ev_port.getText().toString();
                String weight = ev_weight.getText().toString();
                String count = ev_count_1.getText().toString();
                System.out.println("weight============ "+weight);

                if(strIp == null && "".equals(strIp)){
                    Toast.makeText(MainMenu.this,"IP IS NOT NULL",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(strPort == null && "".equals(strPort)){
                    Toast.makeText(MainMenu.this,"PORT IS NOT NULL",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(weight == null && "".equals(weight)){
                    Toast.makeText(MainMenu.this,"Weight IS NOT NULL",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(weight == null && "".equals(count)){
                    Toast.makeText(MainMenu.this,"Weight IS NOT NULL",Toast.LENGTH_SHORT).show();
                    return;
                }
                cacheData(strIp,strPort,weight,count);
                //startActivity(new Intent(MainMenu.this, ConsoleActivity.class));
                MainMenu.this.finish();
            }
        });

        //startActivity(new Intent(MainMenu.this, ConsoleActivity.class));
    }
    private void cacheData(String ip,String port,String weight,String count){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("ip",ip);
        editor.putInt("port",Integer.parseInt(port));
        editor.putInt("weight",Integer.parseInt(weight));
        editor.putInt("count",Integer.parseInt(count));
        editor.commit();
    }

}
