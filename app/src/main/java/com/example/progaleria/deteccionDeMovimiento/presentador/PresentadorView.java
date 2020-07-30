package com.example.progaleria.deteccionDeMovimiento.presentador;

import android.net.Uri;
import com.google.android.gms.maps.model.LatLng;

public interface PresentadorView {
    public void guardarFoto(LatLng latLng, Uri uriImage);
}
