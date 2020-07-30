package com.example.progaleria.fragments.inicio;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.progaleria.R;
import com.example.progaleria.SensorGPS.FotoGeoLocalizadaActivity;
import com.example.progaleria.deteccionDeMovimiento.vista.CamaraDeteccionMovimientoActivity;
import com.example.progaleria.sensorLight.vista.CamaraSensorLightActivity;
import com.example.progaleria.sensorOrientacionDispositivo.vista.CamaraOrientacionActivity;

public class InicioFragment extends Fragment implements  View.OnClickListener{

    private Button btnUbicacion;
    private Button btnOrientacion;
    private Button btnDeteccionM;
    private Button btnSensorLuz;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_inicio, container, false);

        btnUbicacion = root.findViewById(R.id.btnUbicacion);
        btnOrientacion = root.findViewById(R.id.btnOrientacion);
        btnDeteccionM = root.findViewById(R.id.btnDeteccionM);
        btnSensorLuz = root.findViewById(R.id.btnLuz);

        btnUbicacion.setOnClickListener(this);
        btnOrientacion.setOnClickListener(this);
        btnDeteccionM.setOnClickListener(this);
        btnSensorLuz.setOnClickListener(this);

        return root;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnUbicacion:
                 startActivityUbicacion();
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
        }
    }

    public void startActivityUbicacion(){
        Intent intent = new Intent(getContext(), FotoGeoLocalizadaActivity.class);
        startActivity(intent);
    }

    public void startActivitySensorOrientacion(){
        Intent intent = new Intent(getContext(), CamaraOrientacionActivity.class);
        startActivity(intent);
    }

    public void startActivityDeteccionMovimiento(){
        Intent intent = new Intent(getContext(), CamaraDeteccionMovimientoActivity.class);
        startActivity(intent);
    }

    public void startActivitySensorLight(){
        Intent intent = new Intent(getContext(), CamaraSensorLightActivity.class);
        startActivity(intent);
    }
}
