package com.cgroup.angletest;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager mSensorManager;
    private TextView mAnglesText;
    private float[] rotationMatrix, orientation, rotation;
    private Sensor mRotationSensor;
    private int[] angle;
    private int[] newReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAnglesText = (TextView) findViewById(R.id.angleText);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        rotationMatrix = new float[9];
        orientation = new float[3];
        rotation = new float[3];
        angle = new int[3];
        newReference = new int[3];
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRotationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        mSensorManager.registerListener(this, mRotationSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        System.arraycopy(event.values, 0, rotation, 0, 3);
        SensorManager.getRotationMatrixFromVector(rotationMatrix, rotation);
        SensorManager.getOrientation(rotationMatrix, orientation);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                displayAngle(orientation);
            }
        });
    }

    private void displayAngle(float[] values) {
        int[] modAngle = new int[3];

        angle[0] = Math.round((float) Math.toDegrees(values[0]));
        angle[1] = Math.round((float) Math.toDegrees(values[1]));
        if (Math.abs(angle[1]) < 15 ) {
            angle[1] = 0;
        }
        angle[2] = Math.round((float) Math.toDegrees(values[2]));

        int step = (modAngle[2] + 90) / 15;

        mAnglesText.setText(String.format(
                        "X0: %d - Y0: %d - Z0: %d\n" +
                        "X: %d - Y: %d - Z: %d\n" +
                        "Step: %d",
                        angle[0], angle[1], angle[2],
                        modAngle[0], modAngle[1], modAngle[2], step));
    }

    public void resetAngle(View view) {
        System.arraycopy(angle, 0, newReference, 0, 3);
    }
}
