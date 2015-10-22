package com.cgroup.androidcontrolinterface;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class StandardControlActivity extends Activity {
    private Intent backgroundIntentService;
    private Intent myoBackgroundService;
    private androidcontrolinterface androidcontrolinterfaceVariable;
    private boolean toMyo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_control);
    }

    @Override
    public void onResume() {
        super.onResume();
        androidcontrolinterfaceVariable = (androidcontrolinterface) getApplication();
        androidcontrolinterfaceVariable.setStandardIsRunning(true);
        androidcontrolinterfaceVariable.setMyoIsRunning(false);
        androidcontrolinterfaceVariable.setIsRunning(true);
        backgroundIntentService  = new Intent(this, BackgroundIntentService.class);
        startService(backgroundIntentService);
        Log.d("Interface", "StandardControlActivity onResume");
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
        androidcontrolinterfaceVariable = (androidcontrolinterface) getApplication();
        Log.d("Interface", "StandardControlActivity onPause");
        if(!toMyo) {
            androidcontrolinterfaceVariable = (androidcontrolinterface) getApplication();
            androidcontrolinterfaceVariable.setStandardIsRunning(false);
            androidcontrolinterfaceVariable.setMyoIsRunning(false);
            androidcontrolinterfaceVariable.setIsRunning(true);
            backgroundIntentService  = new Intent(this, BackgroundIntentService.class);
            stopService(backgroundIntentService);
            Intent intent = new Intent(this, ConnectActivity.class);
            startActivity(intent);
            finish();
        }
        else toMyo = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Interface", "StandardControlActivity onDestroy");
        finish();
    }

    public void connectMyo(View view) throws InterruptedException {
        toMyo = true;
        Intent intent = new Intent(this, MyoControlActivity.class);
        startActivity(intent);
        finish();
    }


    public void stop(View view) {
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

}