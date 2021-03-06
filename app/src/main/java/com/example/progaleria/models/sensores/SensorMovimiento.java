package com.example.progaleria.models.sensores;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import com.example.progaleria.views.interfaces.IDeteccionMovimiento;

public class SensorMovimiento implements SensorEventListener  {

    private float[] accelValues;
    private float mHighPassX, mHighPassY, mHighPassZ;
    private float mLastX, mLastY, mLastZ;
    private float a = 0.8f;

    private IDeteccionMovimiento camara;

    public SensorMovimiento(IDeteccionMovimiento camara){
        this.camara = camara;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        accelValues = sensorEvent.values.clone();

        float x = accelValues[0];
        float y = accelValues[1];
        float z = accelValues[2];

        mHighPassX = highPass(x, mLastX, mHighPassX);
        mHighPassY = highPass(y, mLastY, mHighPassY);
        mHighPassZ = highPass(z, mLastZ, mHighPassZ);

        mLastX = x;
        mLastY = y;
        mLastZ = z;

        float aceleracionTotal = calcularAceleracionTotal(mHighPassX, mHighPassY, mHighPassZ);
        camara.handleAceleracionTotal(aceleracionTotal);
    }

    /* Filtro de Paso Alto, para enfatizar mas los cambios bruscos de aceleracion
     * del dispositivo
    */
    private float highPass(float current, float last, float filtered){
        return a * (filtered + current  - last);
    }

    private float calcularAceleracionTotal(float x, float y, float z) {
        float x2 = (float)Math.pow(x, 2);
        float y2 = (float)Math.pow(y, 2);
        float z2 = (float)Math.pow(z, 2);
        float aceleracionTotal = (float)Math.sqrt(x2 + y2 + z2);

        return aceleracionTotal;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) { }



}