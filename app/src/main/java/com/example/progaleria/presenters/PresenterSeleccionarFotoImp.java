package com.example.progaleria.presenters;

import android.net.Uri;

import com.example.progaleria.models.ModelSeleccionarFotoImp;
import com.example.progaleria.models.interfaces.ModelSeleccionarFoto;
import com.example.progaleria.presenters.interfaces.PresentadorModelSeleccionarFoto;
import com.example.progaleria.presenters.interfaces.PresentadorViewSeleccionarFoto;

import com.example.progaleria.views.interfaces.ViewSeleccionarFoto;
import com.google.android.gms.maps.model.LatLng;

public class PresenterSeleccionarFotoImp implements PresentadorModelSeleccionarFoto, PresentadorViewSeleccionarFoto {
    private ViewSeleccionarFoto vista;
    private ModelSeleccionarFoto modelo;

    public PresenterSeleccionarFotoImp(ViewSeleccionarFoto vista) {
        this.vista = vista;
        modelo = new ModelSeleccionarFotoImp(this);
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
