package com.example.progaleria.models.sensores;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.ExifInterface;

import com.example.progaleria.views.interfaces.ISensorOrientation;

public class SensorOrientacion implements SensorEventListener {

    public static final int ORIENTATION_PORTRAIT = ExifInterface.ORIENTATION_ROTATE_90; // 6
    public static final int ORIENTATION_LANDSCAPE_REVERSE = ExifInterface.ORIENTATION_ROTATE_180; // 3
    public static final int ORIENTATION_LANDSCAPE = ExifInterface.ORIENTATION_NORMAL; // 1
    public static final int ORIENTATION_PORTRAIT_REVERSE = ExifInterface.ORIENTATION_ROTATE_270; // 8

    int smoothness = 2;
    private float averagePitch = 0;
    private float averageRoll = 0;

    private float[] pitches;
    private float[] rolls;

    private float[] accelValues;
    private float[] magValues;
    private float[] orientationVals = new float[3];

    private ISensorOrientation camara;
    private int orientation = ORIENTATION_PORTRAIT;

    public SensorOrientacion(ISensorOrientation camara) {
        pitches = new float[smoothness];
        rolls = new float[smoothness];
        this.camara = camara;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        switch (sensorEvent.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER: {
                accelValues = sensorEvent.values.clone();
                break;
            }
            case Sensor.TYPE_MAGNETIC_FIELD: {
                magValues = sensorEvent.values.clone();
                break;
            }
        }

        float[] rotationMatrix = new float[16];
        if (magValues != null && accelValues != null) {
            SensorManager.getRotationMatrix(rotationMatrix, null, accelValues, magValues);
            SensorManager.getOrientation(rotationMatrix, orientationVals);

            float pitch = orientationVals[1];
            float roll = orientationVals[2];
            averagePitch = addValue(pitch, pitches);
            averageRoll = addValue(roll, rolls);

            orientation = calculateOrientation();
            camara.handleOrientationDevice(orientation);

        }


    }
    /*Filtro Simple Moving Average SMA*/
    private float addValue(float value, float[] values) {
        value = (float) Math.round((Math.toDegrees(value)));
        float average = 0;
        for (int i = 1; i < smoothness; i++) {
            values[i - 1] = values[i];
            average += values[i];
        }
        values[smoothness - 1] = value;
        average = (average + value) / smoothness;
        return average;
    }

    private int calculateOrientation() {
        if (((orientation == ORIENTATION_PORTRAIT || orientation == ORIENTATION_PORTRAIT_REVERSE)
                && (averageRoll > -30 && averageRoll < 30))) {
            if (averagePitch > 0)
                return ORIENTATION_PORTRAIT_REVERSE;
            else
                return ORIENTATION_PORTRAIT;
        } else {
            if (Math.abs(averagePitch) >= 30) {
                if (averagePitch > 0)
                    return ORIENTATION_PORTRAIT_REVERSE;
                else
                    return ORIENTATION_PORTRAIT;
            } else {
                if (averageRoll > 0) {
                    return ORIENTATION_LANDSCAPE_REVERSE;
                } else {
                    return ORIENTATION_LANDSCAPE;
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }


}
