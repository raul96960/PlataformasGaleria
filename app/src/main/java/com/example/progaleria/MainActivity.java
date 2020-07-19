package com.example.progaleria;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.progaleria.SensorGPS.FotoGeoLocalizadaActivity;
import com.example.progaleria.deteccionDeMovimiento.CamaraDeteccionMovimientoActivity;
import com.example.progaleria.login.View.IniciarSesion;
import com.example.progaleria.sensorLight.CamaraSensorLightActivity;
import com.example.progaleria.sensorOrientacionDispositivo.CamaraOrientacionActivity;
import com.example.progaleria.fragments.NavigationMain;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnUbicacion;
    private Button btnOrientacion;
    private Button btnDeteccionM;
    private Button btnSensorLuz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnUbicacion = findViewById(R.id.btnUbicacion);
        btnOrientacion = findViewById(R.id.btnOrientacion);
        btnDeteccionM = findViewById(R.id.btnDeteccionM);
        btnSensorLuz = findViewById(R.id.btnLuz);
        Button btnLogin = findViewById(R.id.btnLogin);

        btnUbicacion.setOnClickListener(this);
        btnOrientacion.setOnClickListener(this);
        btnDeteccionM.setOnClickListener(this);
        btnSensorLuz.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnUbicacion:
                startActivityFragments();
               // startActivityUbicacion();
                break;
            case R.id.btnOrientacion:
                startActivitySensorOrientacion();
                break;
            case R.id.btnDeteccionM:
                startActivityDeteccionMovimiento();
                break;
            case R.id.btnLuz:
                startActivitySensorLight();
                break;
            case R.id.btnLogin:
                startActivityLogin();
                break;
        }
    }
    public void startActivityUbicacion(){
        Intent intent = new Intent(this, FotoGeoLocalizadaActivity.class);
        startActivity(intent);
    }
    public void startActivityFragments(){
        Intent intent = new Intent(this, NavigationMain.class);
        startActivity(intent);
    }

    public void startActivityLogin(){
        Intent intent = new Intent(this, IniciarSesion.class);
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