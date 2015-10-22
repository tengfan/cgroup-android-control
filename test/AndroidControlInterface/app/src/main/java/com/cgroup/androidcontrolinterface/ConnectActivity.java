package com.cgroup.androidcontrolinterface;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


public class ConnectActivity extends Activity {
    /**Variable Declaration */
    private androidcontrolinterface androidcontrolinterfaceVariable;
    private Intent backgroundIntentService;
    private Intent myoBackgroundService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
    }

    @Override
    public void onResume() {
        super.onResume();
        androidcontrolinterfaceVariable = (androidcontrolinterface) getApplication();
        androidcontrolinterfaceVariable.setStandardIsRunning(false);
        androidcontrolinterfaceVariable.setMyoIsRunning(false);
        androidcontrolinterfaceVariable.setIsRunning(false);
        backgroundIntentService  = new Intent(this, com.cgroup.androidcontrolinterface.BackgroundIntentService.class);
        stopService(backgroundIntentService);
        myoBackgroundService  = new Intent(this, MyoBackgroundService.class);
        stopService(myoBackgroundService);
        Log.d("Interface", "ConnectActivity onResume");
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Interface", "ConnectActivity onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Interface", "ConnectActivity onDestroy");
        finish();
    }

    public void quit(View view) {
        // TODO: Liberate resources and quit wifi
        androidcontrolinterfaceVariable = (androidcontrolinterface) getApplication();
        androidcontrolinterfaceVariable.setIsRunning(false);
        androidcontrolinterfaceVariable.setMyoIsRunning(false);
        androidcontrolinterfaceVariable.setStandardIsRunning(false);
        androidcontrolinterfaceVariable.setForceToStop(true);
        myoBackgroundService = new Intent(this, MyoBackgroundService.class);
        stopService(myoBackgroundService);
        backgroundIntentService = new Intent(this, BackgroundIntentService.class);
        stopService(backgroundIntentService);
        android.os.Process.killProcess(android.os.Process.myPid());
        finish();
    }

    public void connect(View view){
//        WiFiConnect();
        Intent intentActivity = new Intent(this, InterfaceSelectionActivity.class);
        startActivity(intentActivity);//Enter the InterfaceSelectionActivity
        androidcontrolinterfaceVariable = (androidcontrolinterface) getApplication();
        androidcontrolinterfaceVariable.setIsRunning(true);
        finish();
    }
}
