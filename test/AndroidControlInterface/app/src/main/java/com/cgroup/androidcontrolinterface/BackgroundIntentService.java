package com.cgroup.androidcontrolinterface;

import android.app.IntentService;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.SocketException;

/**
 * Created by Teng on 2015/9/21.
 */

public class BackgroundIntentService extends IntentService implements SensorEventListener {
    /** Variables Declaration */
    androidcontrolinterface androidcontrolinterfaceVariable;
    /** isRunning is a boolean which is used for start and stop the service */
    protected Boolean isRunning  = false;
    protected Boolean standardIsRunning  = false;
    protected Boolean myoIsRunning  = false;

    /** isRunning is a boolean which is used for udp errors */
    protected Boolean errorUDP = false;

    /** Class UDP Client */
    protected com.cgroup.androidcontrolinterface.UDPClientClass udpClientClass = new com.cgroup.androidcontrolinterface.UDPClientClass();

    /** a package of bytes is defined to be 3 bytes, #1 byte is the identification, #2 byte and #3 byte is the data */
    protected byte[] bytes = new byte[3];

    /** Class MediaPlayer which will launch the alert.mp3 in the src/res/raw/alert.mp3 */
    protected MediaPlayer alertPlayer;

    /** Raspberry Pi IP address */
    String ipAddress = "10.0.0.1";//"10.214.12.218";//"192.168.1.17";//"192.168.43.161"//"10.0.0.1";
    int port = 1001;

    /** Counter for times of timeout */
    int counter = 0;

    /** Declaration of the package */
    byte identification = 0;
    byte b1 = 0;
    byte b2 = 0;

    /** Power Manager to keep CPU On*/
    PowerManager powerManager;
    PowerManager.WakeLock wakeLock;

    /** the Sensor Manager */
    protected SensorManager sManager;
    protected byte gravityY, gravityZ;

    /** The constructor */
    public BackgroundIntentService() {
        super("BackgroundIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakelockTag");
        alertPlayer = MediaPlayer.create(getApplicationContext(), R.raw.alert);
        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        androidcontrolinterfaceVariable = (androidcontrolinterface) getApplication();
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        androidcontrolinterfaceVariable = (androidcontrolinterface) getApplication();
        androidcontrolinterfaceVariable.setForceToStop(false);
        /*register the sensor listener to listen to the gyroscope sensor, use the
        callbacks defined in this class, and gather the sensor information as quick
        as possible*/
        sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_GRAVITY),SensorManager.SENSOR_DELAY_FASTEST);
    /** Set Timeout  500ms for reception */
        try {
            udpClientClass.setSoTimeout(500);
        } catch (SocketException e) {
            e.printStackTrace();
        }

    /** If isRunning is false, we quit the loop and close the service */
        while((androidcontrolinterfaceVariable.getIsRunning() || wakeLock.isHeld()) && !androidcontrolinterfaceVariable.getForceToStop()) {
            isRunning = androidcontrolinterfaceVariable.getIsRunning();
            standardIsRunning = androidcontrolinterfaceVariable.getStandardIsRunning();
            myoIsRunning = androidcontrolinterfaceVariable.getMyoIsRunning();
            /** Per 5ms, we send a message */
            if (myoIsRunning || standardIsRunning || wakeLock.isHeld()) {
                if (standardIsRunning) {
                    if(wakeLock.isHeld()) {
                        wakeLock.release();
                        Log.d("WakeLock", "Off");
                    }
                    Log.d("BackgroundService", "standardIsRunning == true");
                    identification = 's';//0x01;
                }
                if (myoIsRunning) {
                    if(!wakeLock.isHeld()) {
                        wakeLock.acquire();
                        Log.d("WakeLock", "On");
                    }
                    Log.d("BackgroundService", "myoIsRunning == true");
                    identification = 'm';//0x02;
                }

                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
    /** Data Traitment for StandardControl */
                    if (standardIsRunning) {
                        b1 = gravityY;
                        b2 = gravityZ;
                    }

                    if (myoIsRunning) {
                        b1 = androidcontrolinterfaceVariable.getMyoRunStopReverse();
                        b2 = androidcontrolinterfaceVariable.getMyoDirection();
                    }
    /** Make the package */
                    bytes = udpClientClass.intToByteArray(identification, b1, b2);
                    udpClientClass.setSendData(bytes);
                    udpClientClass.send(ipAddress, port);
                    Log.d("DEBUG", "Sent");
                } catch (IOException e) {
                    e.printStackTrace();
                }
    /** If android continue not to receive the response for 20 times, we break the loop, close the socket and stop the service */

                try {
                    if (!udpClientClass.receive(ipAddress, port)) {
                        Log.d("DBackgroundService", "Intent Service Failure for the reception");
                        counter++;
                        Log.d("BackgroundService", "Counter = "+counter);
                        if (counter == 5) {
                            Log.d("BackgroundService", "Counter = "+10);
                            errorUDP = true;
                            break;
                        }
                    } else {
                        Log.d("BackgroundService", "Counter reinitialisation");
                        counter = 0;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("BackgroundService", "Intent Service Success for the reception");

            }
        }

    /** If wee have to stop the intent service, the alert will be launched
     * and we will inform
     * */
        if(errorUDP) {
            try {
            udpClientClass.closeSocket();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(getApplicationContext(), "You lose the connection WiFi, please re-connect the WiFi and click on the button'Connect'", Toast.LENGTH_LONG).show();
    /** alert Part for Myo */
            if(myoIsRunning){
                alertPlayer.start();
                Log.d("ALERT", "Start");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (alertPlayer.isPlaying()) {
                    alertPlayer.stop();
                    Log.d("ALERT", "Stop");
                }
            }
            /** If it has errors of connection, we will return to the interface ConnectActivity */
            Intent connectActivity = new Intent(BackgroundIntentService.this,ConnectActivity.class);
            connectActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(connectActivity);
        }
    /** Stop the intent service */
        stopSelf();
        //unregister the sensor listener
        sManager.unregisterListener(this);
        Log.d("Intent Service", "Stop");
        if(wakeLock.isHeld()) {
            wakeLock.release();
            Log.d("BackgroundService", "WakeLock Off");
            Log.d("BackgroundService", "MyoControlActivity onDestroy");
            // We don't want any callbacks when the Activity is gone, so unregister the listener.
            //Hub.getInstance().removeListener(mListener);

            // The Activity is finishing, so shutdown the Hub. This will disconnect from the Myo.
            //Hub.getInstance().shutdown();
        }
    }

    @Override
    //If the task is finished, the intentService will be closed
    public void onDestroy() {
        Log.d("BackgroundService", "onDestroy");
        super.onDestroy();
        isRunning = false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d("SensorManager", "onSensorChanged");
        float gY, gZ;
        //if sensor is unreliable, return void
        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
            return;
        }

        //else it will output the Y and Z values
        gY = event.values[1];
        gZ = event.values[2];
        /** Translate the gravity to bytes */
        gY = (float) (((gY+9.81)/9.81/2)*255);
        gZ = (float) (((gZ+9.81)/9.81/2)*255);
        gravityY = (byte)(gY+0.5);
        gravityZ = (byte)(gZ+0.5);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Do nothing
    }
}


