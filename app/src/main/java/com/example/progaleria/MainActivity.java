package com.example.progaleria;

import android.content.Intent;
import android.os.Bundle;

import com.example.progaleria.SensorGPS.FotoGeoLocalizadaActivity;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startActivityUbicacion();
    }
    public void startActivityUbicacion(){
        Intent intent = new Intent(this, FotoGeoLocalizadaActivity.class);
        startActivity(intent);
    }

}