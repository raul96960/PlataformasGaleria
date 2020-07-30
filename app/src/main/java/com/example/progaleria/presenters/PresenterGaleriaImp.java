package com.example.progaleria.presenters;

import com.example.progaleria.models.FotoGaleria;
import com.example.progaleria.models.interfaces.ModelGaleria;
import com.example.progaleria.models.ModelGaleriaImp;
import com.example.progaleria.presenters.interfaces.PresenterModelGaleria;
import com.example.progaleria.presenters.interfaces.PresenterViewGaleria;
import com.example.progaleria.views.fragments.galeria.ViewGaleria;

import java.util.List;

public class PresenterGaleriaImp  implements PresenterModelGaleria, PresenterViewGaleria {
    private ViewGaleria vista;
    private ModelGaleria modelo;
    public PresenterGaleriaImp(ViewGaleria vista){
        this.vista = vista;
        modelo = new ModelGaleriaImp(this);

    }
    @Override
    public void onSuccessFotos(List<FotoGaleria> fotos) {
        vista.showFotos(fotos);
    }

    @Override
    public void onError(String message) {
        vista.onError(message);
    }

    @Override
    public void obtenerFotos() {
        modelo.obtenerFotos();
    }
}
