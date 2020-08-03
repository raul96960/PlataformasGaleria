package com.example.progaleria.presenters.interfaces;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

public interface PresentadorViewSeleccionarFoto {
    void guardarFoto(LatLng latLng, Uri uriImage);
}
