package com.example.progaleria.sensorLight;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class SensorLight implements SensorEventListener  {

    private float mLowPassLight;
    private float a = 0.2f;

    private ICamaraSensorLight camara;

    public SensorLight(ICamaraSensorLight camara){
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