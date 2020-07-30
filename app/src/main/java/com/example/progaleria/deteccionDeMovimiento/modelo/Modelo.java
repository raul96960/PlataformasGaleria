package com.example.progaleria.deteccionDeMovimiento.modelo;

import android.net.Uri;

import com.example.progaleria.SensorGPS.Foto;
import com.google.android.gms.maps.model.LatLng;

public interface Modelo {
    public void guardarFoto(Uri uri, LatLng latLng);
}
