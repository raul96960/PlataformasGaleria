package com.example.progaleria.presenters;

import android.net.Uri;

import com.example.progaleria.models.interfaces.ModelMiCamara;
import com.example.progaleria.models.ModelMiCamaraImp;
import com.example.progaleria.presenters.interfaces.PresentadorModelMiCamara;
import com.example.progaleria.presenters.interfaces.PresentadorViewMiCamara;
import com.example.progaleria.views.interfaces.ViewMiCamara;
import com.google.android.gms.maps.model.LatLng;

public class PresentadorImpMiCamara implements PresentadorModelMiCamara, PresentadorViewMiCamara {

    private ModelMiCamara modelo;
    private ViewMiCamara vista;

    public PresentadorImpMiCamara(ViewMiCamara vista){
        this.vista = vista;
        modelo = new ModelMiCamaraImp(this);
    }
    @Override
    public void onSuccess() {
        vista.onSuccess();
        vista.hideProgressDialog();
    }

    @Override
    public void onError(String message) {
        vista.onError(message);
        vista.hideProgressDialog();
    }

    @Override
    public void guardarFoto(LatLng latLng, Uri uriImage) {
        modelo.guardarFoto(uriImage, latLng);
        vista.showProgressDialog();
    }
}
