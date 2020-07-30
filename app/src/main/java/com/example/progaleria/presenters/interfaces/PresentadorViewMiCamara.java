package com.example.progaleria.presenters.interfaces;

import android.net.Uri;
import com.google.android.gms.maps.model.LatLng;

public interface PresentadorViewMiCamara {
    public void guardarFoto(LatLng latLng, Uri uriImage);
}
