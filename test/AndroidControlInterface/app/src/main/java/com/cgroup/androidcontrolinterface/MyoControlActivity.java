package com.cgroup.androidcontrolinterface;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MyoControlActivity extends Activity {
    private androidcontrolinterface androidcontrolinterfaceVariable;
    private Intent backgroundIntentService;
    private Intent myoBackgroundService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myo_control);
    }

    @Override
    public void onResume() {
        super.onResume();
        androidcontrolinterfaceVariable = (androidcontrolinterface) getApplication();
        androidcontrolinterfaceVariable.setStandardIsRunning(false);
        androidcontrolinterfaceVariable.setMyoIsRunning(true);
        androidcontrolinterfaceVariable.setIsRunning(true);
        backgroundIntentService  = new Intent(this, BackgroundIntentService.class);
        startService(backgroundIntentService);
        myoBackgroundService = new Intent(this, MyoBackgroundService.class);
        startService(myoBackgroundService);
        Log.d("Interface", "MyoControlActivity onResume");
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
        Log.d("Interface", "MyoControlActivity onPause");
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        finish();
    }

    public void connectStandard(View view) throws InterruptedException {
        Intent intent = new Intent(this, StandardControlActivity.class);
        myoBackgroundService = new Intent(this, MyoBackgroundService.class);
        stopService(myoBackgroundService);
        startActivity(intent);
        finish();
    }
}
