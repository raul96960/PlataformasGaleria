package com.example.progaleria.deteccionDeMovimiento.presentador;

import android.net.Uri;
import android.view.Display;

import com.example.progaleria.SensorGPS.Foto;
import com.example.progaleria.deteccionDeMovimiento.modelo.Modelo;
import com.example.progaleria.deteccionDeMovimiento.modelo.ModeloImp;
import com.example.progaleria.deteccionDeMovimiento.vista.Vista;
import com.google.android.gms.maps.model.LatLng;

public class PresentadorImp implements PresentadorModel, PresentadorView{

    private Modelo modelo;
    private Vista vista;
    public PresentadorImp(Vista vista){
        this.vista = vista;
        modelo = new ModeloImp(this);
    }
    @Override
    public void onSuccess() {
        vista.onSuccess();
    }

    @Override
    public void onError(String message) {
        vista.onError(message);
    }

    @Override
    public void guardarFoto(LatLng latLng, Uri uriImage) {
        modelo.guardarFoto(uriImage, latLng);
    }
}
