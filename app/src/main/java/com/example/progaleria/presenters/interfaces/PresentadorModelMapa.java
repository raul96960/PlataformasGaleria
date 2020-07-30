package com.example.progaleria.presenters.interfaces;

import com.example.progaleria.models.FotoGaleria;

import java.util.List;

public interface PresentadorModelMapa {
    public void onSuccessFotos(List<FotoGaleria> fotos);
    public void onError(String mensaje);
}
