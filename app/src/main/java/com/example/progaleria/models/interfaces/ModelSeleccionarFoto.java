package com.example.progaleria.models.interfaces;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

public interface ModelSeleccionarFoto {
    void guardarFoto(Uri imageUri, final LatLng latLng);
}
