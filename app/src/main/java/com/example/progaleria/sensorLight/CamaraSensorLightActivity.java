package com.example.progaleria.sensorLight;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.progaleria.R;
import com.example.progaleria.sensorOrientacionDispositivo.ShowCamera;

public class CamaraSensorLightActivity extends AppCompatActivity implements ICamaraSensorLight {
    private final float LIGHT_LIMITE = 2f; //

    private SensorManager sensorManager;
    private SensorLight sensorLight;

    private Camera camera;
    private FrameLayout frameLayout;
    private ShowCamera showCamera;
    private boolean flashMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camara_sensor_light);


        solicitarPermisosDeCamara();
        abrirCamaraPersonalizada();
        habilitarSensorLight();

        Button btn = findViewById(R.id.btnTomarFoto);

        final PictureCallback mPictureCallBack = new PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                camera.startPreview();
            }
        };
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (camera != null) {
                    camera.takePicture(null, null, mPictureCallBack);
                }
            }
        });
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

    public void habilitarSensorLight() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorLight = new SensorLight(this);
        listenerSensorLight();
    }

    /*
     * Recibe la cantidad de luz del ambiente
     * A partir de esa informacion si el ambiente es oscuro HABILITA FLASH
     * Caso contrario DESABILITA EL FLASH
     */
    @Override
    public void handleSensorLight(float light) {
        if (light <= LIGHT_LIMITE) {
            if (!flashMode) {
                flashMode = true;
                activarFlashModeCamera();
                Toast.makeText(this, "Habilitando Flash", Toast.LENGTH_LONG).show();
            }
        } else {
            if (flashMode) {
                flashMode = false;
                desactivarFlashModeCamera();
                Toast.makeText(this, "Desabilitando Flash", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void activarFlashModeCamera() {
        setFlashMode(Camera.Parameters.FLASH_MODE_ON);
    }

    public void desactivarFlashModeCamera() {
        setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
    }

    private void setFlashMode(String flashMode) {
        Camera.Parameters parameters = camera.getParameters();
        parameters.setFlashMode(flashMode);
        camera.setParameters(parameters);
    }

    public void registerListener() {
        listenerSensorLight();
    }

    public void unregisterListener() {
        sensorManager.unregisterListener(sensorLight);
    }

    public void listenerSensorLight() {
        Sensor sensor1 = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener(
                sensorLight,
                sensor1,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void solicitarPermisosDeCamara() {
        if (ContextCompat.checkSelfPermission(CamaraSensorLightActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CamaraSensorLightActivity.this,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

    }

}