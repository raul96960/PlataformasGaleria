package com.example.progaleria.sensorOrientacionDispositivo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.progaleria.R;


public class CamaraOrientacionActivity extends AppCompatActivity implements ICamaraSensorOrientation {
    private static final String MENSAJE_TOAST = "Por Favor ponga en modo Horizontal su dispositivo para tener un mejor panorama";
    private SensorManager sensorManager;
    private SensorOrientacion sensorOrientacion;

    private Camera camera;
    private FrameLayout frameLayout;
    private ShowCamera showCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camara_orientacion);

        solicitarPermisosDeCamara();
        abrirCamaraPersonalizada();
        habilitarSensorDeOrientacion();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterListener();
    }

    public void abrirCamaraPersonalizada() {
        frameLayout = findViewById(R.id.frameLayoutCamera);
        camera = Camera.open();
        showCamera = new ShowCamera(this, camera);
        frameLayout.addView(showCamera);
    }

    public void habilitarSensorDeOrientacion() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorOrientacion = new SensorOrientacion(this);
        listenerMagneticField();
        listenerSensorAcelerometro();
    }

    /*
     * Recibe la orientacion del dispositivo luego de hacer calculos con los sensores
     * de MagneticField y Acelerometro
     */
    @Override
    public void handleOrientationDevice(int orientation) {
        if (isVerticalDevice(orientation)) {
            colocarElDispositivoHorrizontalmente();
        }
    }
    /* Le sugerimos al usuario que es MAS CONVENIENTE TOMAR LAS FOTOS cuando el dispositivo se encuentre horizontalmente
     * Para que pueda apreciar el paisaje de una mejor manera.
     * */
    private void colocarElDispositivoHorrizontalmente(){
        Toast toast = Toast.makeText(this, MENSAJE_TOAST, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private boolean isVerticalDevice(int orientation) {
        return orientation == SensorOrientacion.ORIENTATION_PORTRAIT || orientation == SensorOrientacion.ORIENTATION_PORTRAIT_REVERSE;
    }


    public void registerListener() {
        listenerMagneticField();
        listenerSensorAcelerometro();
    }

    public void unregisterListener() {
        sensorManager.unregisterListener(sensorOrientacion);
    }

    public void listenerMagneticField() {
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(
                sensorOrientacion,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void listenerSensorAcelerometro() {
        Sensor sensor1 = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(
                sensorOrientacion,
                sensor1,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void solicitarPermisosDeCamara() {
        if (ContextCompat.checkSelfPermission(CamaraOrientacionActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CamaraOrientacionActivity.this,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

    }

}