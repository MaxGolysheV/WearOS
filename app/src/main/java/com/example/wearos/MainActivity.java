package com.example.wearos;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wearos.databinding.ActivityMainBinding;

public class MainActivity extends Activity {

    private ActivityMainBinding binding;
    private ImageView im;
    private TextView tv_x, tv_y, tv_z;

    private SensorManager sm;
    private Sensor s;
    private SensorEventListener sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        tv_x = findViewById(R.id.tv_x);
        tv_y = findViewById(R.id.tv_y);
        tv_z = findViewById(R.id.tv_z);
        im = findViewById(R.id.im);
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        s = sm.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        if(sm != null) {
            sv = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {
                    float[] rotationMatrix = new float[16];
                    SensorManager.getRotationMatrixFromVector(rotationMatrix,
                            sensorEvent.values);
                    float[] remappedRotationMatrix = new float[16];
                    SensorManager.remapCoordinateSystem(rotationMatrix,
                            SensorManager.AXIS_X,
                            SensorManager.AXIS_Z,
                            remappedRotationMatrix);

                    float[] orientations = new float[3];
                    SensorManager.getOrientation(remappedRotationMatrix, orientations);
                    for (int i = 0; i < 3; i++) {
                        orientations[i] = (float) (Math.toDegrees(orientations[i]));
                    }

                    tv_x.setText(String.valueOf((int) orientations[1]));
                    tv_y.setText(String.valueOf((int) orientations[0]));
                    tv_z.setText(String.valueOf((int) orientations[2]));

                    im.setRotation(-orientations[2]);
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {

                }
            };
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sm.registerListener(sv,s,SensorManager.SENSOR_DELAY_FASTEST);

    }

    @Override
    protected void onPause() {
        super.onPause();
        sm.unregisterListener(sv);

    }
}