package com.example.progaleria.fragments.ubicacion.vista;

import com.example.progaleria.fragments.ubicacion.modelo.ClusterMarker;

import java.util.List;

public interface Vista {
    public void showMarkersFotosMap(List<ClusterMarker> markers);
    public void toastError(String mensaje);
}
