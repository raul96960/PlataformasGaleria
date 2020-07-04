package com.example.progaleria.deteccionDeMovimiento;

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
import com.example.progaleria.sensorOrientacionDispositivo.ShowCamera;

public class CamaraDeteccionMovimientoActivity extends AppCompatActivity implements ICamaraDeteccionMovimiento {

    private static final float ACELERACION_TOTAL_MAXIMA_ACEPTADA = 0.20f;
    private static final String TOAST_MENSAJE = "Por favor deje de Mover el Dispositivo";

    private SensorManager sensorManager;
    private SensorMovimiento sensorMovimiento;

    private Camera camera;
    private FrameLayout frameLayout;
    private ShowCamera showCamera;

    private Toast message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camara_deteccion_movimiento);

        message = Toast.makeText(this, TOAST_MENSAJE, Toast.LENGTH_SHORT);
        message.setGravity(Gravity.CENTER, 0, 0);

        solicitarPermisosDeCamara();
        abrirCamaraPersonalizada();
        habilitarSensorDeMovimiento();
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

    public void abrirCamaraPersonalizada(){
        frameLayout = findViewById(R.id.frameLayoutCamera);
        camera = Camera.open();
        showCamera = new ShowCamera(this, camera);
        frameLayout.addView(showCamera);
    }

    public void habilitarSensorDeMovimiento(){
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorMovimiento= new SensorMovimiento(this);
        listenerSensorAcelerometro();
    }

    /*
     * Recibe la aceleration total del dispositivo luego de hacer calculos con el
     * sensor Acelerometro
     *
     * Le sugerimos al usuario que deje de mover el dispositivo
     * */
    public void handleAceleracionTotal(float aceleracionTotal){
        if(aceleracionTotal >= ACELERACION_TOTAL_MAXIMA_ACEPTADA){
            message.show();
        }
        else{
            message.cancel();
        }
    }

    public void registerListener() {
        listenerSensorAcelerometro();
    }

    public void unregisterListener() {
        sensorManager.unregisterListener(sensorMovimiento);
    }

    private void listenerSensorAcelerometro(){
        Sensor sensor1 = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(
                sensorMovimiento,
                sensor1,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void solicitarPermisosDeCamara() {
        if (ContextCompat.checkSelfPermission(CamaraDeteccionMovimientoActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CamaraDeteccionMovimientoActivity.this,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }


}