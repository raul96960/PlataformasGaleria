package com.example.progaleria.views.fragments.galeria;

import com.example.progaleria.models.FotoGaleria;
import com.example.progaleria.models.GaleriaAdapter;

import java.util.List;

public interface ViewGaleria {
    public void showFotos(List<FotoGaleria> fotos);
    public void onError(String message);
}
