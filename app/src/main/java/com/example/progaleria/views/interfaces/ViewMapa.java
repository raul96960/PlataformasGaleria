package com.example.progaleria.views.interfaces;

import com.example.progaleria.models.MarkerItem;

import java.util.List;

public interface ViewMapa {
    public void showMarkersFotosMap(List<MarkerItem> markers);
    public void toastError(String mensaje);
}
