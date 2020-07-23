package com.example.progaleria.fragments.ubicacion.presentador;

import com.example.progaleria.fragments.galeria.FotoGaleria;

import java.util.List;

public interface PresentadorModel {
    public void onSuccessFotos(List<FotoGaleria> fotos);
    public void onError(String mensaje);
}
