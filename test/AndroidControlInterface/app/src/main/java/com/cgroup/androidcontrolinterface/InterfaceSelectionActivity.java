package com.cgroup.androidcontrolinterface;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class InterfaceSelectionActivity extends Activity {

    private androidcontrolinterface androidcontrolinterfaceVariable;
    private Intent backgroundIntentService;
    private Intent myoBackgroundService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interface_selection);
    }

    public void connectStandard(View view) throws InterruptedException {
        Intent intent = new Intent(this, StandardControlActivity.class);
        androidcontrolinterfaceVariable = (androidcontrolinterface) getApplication();
        androidcontrolinterfaceVariable.setIsRunning(true);
        myoBackgroundService = new Intent(this, MyoBackgroundService.class);
        stopService(myoBackgroundService);
        startActivity(intent);

        finish();
    }

    public void connectMyo(View view) throws InterruptedException {
        Intent intent = new Intent(this, MyoControlActivity.class);
        androidcontrolinterfaceVariable = (androidcontrolinterface) getApplication();
        androidcontrolinterfaceVariable.setIsRunning(true);
        startActivity(intent);
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        androidcontrolinterfaceVariable = (androidcontrolinterface) getApplication();
        androidcontrolinterfaceVariable.setStandardIsRunning(false);
        androidcontrolinterfaceVariable.setMyoIsRunning(false);
        backgroundIntentService = new Intent(this, BackgroundIntentService.class);
        stopService(backgroundIntentService);
        myoBackgroundService  = new Intent(this, MyoBackgroundService.class);
        stopService(myoBackgroundService);
        Log.d("Interface", "InterfaceSelectionActivity onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Interface", "InterfaceSelectionActivity onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Interface", "InterfaceSelectionActivity onDestroy");
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
}
