package com.example.progaleria.models.sensores;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import com.example.progaleria.views.interfaces.ISensorLight;

public class SensorLight implements SensorEventListener  {

    private float mLowPassLight;
    private float a = 0.2f;

    private ISensorLight camara;

    public SensorLight(ISensorLight camara){
        this.camara = camara;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        float light = sensorEvent.values[0];

        mLowPassLight = lowPass(light, mLowPassLight);

        this.camara.handleSensorLight(mLowPassLight);
    }

    public float lowPass(float current, float last){
        return last * (1.0f - a) + current * a;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}

}