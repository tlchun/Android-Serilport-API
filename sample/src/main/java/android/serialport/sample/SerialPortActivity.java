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
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.serialport.SerialPort;
import android.serialport.sample.device.BuilderActorImpl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

public abstract class SerialPortActivity extends Activity {

    protected Application mApplication;

    protected SerialPort mSerialPortHSL0;
    protected OutputStream mOutputStreamHSL0;
    private InputStream mInputStreamHSL0;
    private ReadThreadHSL0 mReadThreadHSL0;


    protected SerialPort mSerialPortHSL1;
    protected OutputStream mOutputStreamHSL1;
    private InputStream mInputStreamHSL1;
    private ReadThreadHSL1 mReadThreadHSL1;
    private boolean isRun = true;

    private class ReadThreadHSL1 extends Thread {
        @Override
        public void run() {
            int size;
            byte[] buffer = new byte[256];
            while (isRun) {
                try {
                    if (mInputStreamHSL1 == null) return;
                    size = mInputStreamHSL1.read(buffer);
                    if (size > 0) {
                        //System.out.println("ReadThreadHSL1== size"+ size+" content= "+ BuilderActorImpl.bytesToHexString(buffer));
                        onDataReceivedTTySHL1(buffer, size);
                    }
                    sleep(500);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private class ReadThreadHSL0 extends Thread {
        @Override
        public void run() {
            byte[] buffer = new byte[8096];
            int size = 0;
            while (isRun) {
                try {
                    if (mInputStreamHSL0 == null) return;
                    size = mInputStreamHSL0.read(buffer);
                    if (size > 0) {
                        //System.out.println("ReadThreadHSL0== size"+ size+" content= "+BuilderActorImpl.bytesToHexString(buffer));
                        onDataReceivedTTySHL0(buffer, size);
                    }
                    sleep(500);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void DisplayError(int resourceId) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Error");
        b.setMessage(resourceId);
        b.setPositiveButton("OK", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SerialPortActivity.this.finish();
            }
        });
        b.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication = (Application) getApplication();
        //激光
        startSerialPortHSL0();
        //重力
        startSerialPortHSL1();
    }
    protected void startSerialPortHSL0(){
        try {
            mSerialPortHSL0 = mApplication.getSerialPort("/dev/ttyHSL0",115200);
            mOutputStreamHSL0 = mSerialPortHSL0.getOutputStream();
            mInputStreamHSL0 = mSerialPortHSL0.getInputStream();
			/* Create a receiving thread */
            mReadThreadHSL0 = new ReadThreadHSL0();
            mReadThreadHSL0.start();

        } catch (SecurityException e) {
            DisplayError(R.string.error_security);
        } catch (IOException e) {
            DisplayError(R.string.error_unknown);
        } catch (InvalidParameterException e) {
            DisplayError(R.string.error_configuration);
        }
    }
    protected void stopSerialPortHSL0(){
        isRun = false;
        //mApplication.closeSerialPort();
        if(mSerialPortHSL1 != null) {
            mSerialPortHSL1.close();
            mSerialPortHSL1 = null;
        }
    }

    protected void startSerialPortHSL1(){
        try {
            mSerialPortHSL1 = mApplication.getSerialPort("/dev/ttyHSL1",9600);
            mOutputStreamHSL1 = mSerialPortHSL1.getOutputStream();
            mInputStreamHSL1 = mSerialPortHSL1.getInputStream();
			/* Create a receiving thread */
            mReadThreadHSL1 = new ReadThreadHSL1();
            mReadThreadHSL1.start();

        } catch (SecurityException e) {
            DisplayError(R.string.error_security);
        } catch (IOException e) {
            DisplayError(R.string.error_unknown);
        } catch (InvalidParameterException e) {
            DisplayError(R.string.error_configuration);
        }
    }
    protected void stopSerialPortHSL1(){
        isRun = false;
        //mApplication.closeSerialPort();
        if(mSerialPortHSL1 != null) {
            mSerialPortHSL1.close();
            mSerialPortHSL1 = null;
        }
    }
    protected abstract void onDataReceivedTTySHL0(final byte[] buffer, final int size);
    protected abstract void onDataReceivedTTySHL1(final byte[] buffer, final int size);
    @Override
    protected void onDestroy() {
        super.onDestroy();

        stopSerialPortHSL0();
        stopSerialPortHSL1();
    }
}
