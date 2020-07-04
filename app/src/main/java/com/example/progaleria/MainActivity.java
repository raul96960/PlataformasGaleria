package com.example.progaleria;

import android.content.Intent;
import android.os.Bundle;

import com.example.progaleria.SensorGPS.FotoGeoLocalizadaActivity;
import com.example.progaleria.deteccionDeMovimiento.CamaraDeteccionMovimientoActivity;
import com.example.progaleria.sensorLight.CamaraSensorLightActivity;
import com.example.progaleria.sensorOrientacionDispositivo.CamaraOrientacionActivity;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startActivitySensorLight();
    }
    public void startActivityUbicacion(){
        Intent intent = new Intent(this, FotoGeoLocalizadaActivity.class);
        startActivity(intent);
    }

    public void startActivitySensorOrientacion(){
        Intent intent = new Intent(this, CamaraOrientacionActivity.class);
        startActivity(intent);
    }
    public void startActivityDeteccionMovimiento(){
        Intent intent = new Intent(this, CamaraDeteccionMovimientoActivity.class);
        startActivity(intent);
    }

    public void startActivitySensorLight(){
        Intent intent = new Intent(this, CamaraSensorLightActivity.class);
        startActivity(intent);
    }
}