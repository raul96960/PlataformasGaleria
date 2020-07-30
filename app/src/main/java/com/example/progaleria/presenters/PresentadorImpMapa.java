package com.example.progaleria.presenters;

import android.util.Log;

import com.example.progaleria.models.FotoGaleria;
import com.example.progaleria.models.interfaces.ModelMapa;
import com.example.progaleria.models.ModeloImpMapa;
import com.example.progaleria.models.MarkerItem;
import com.example.progaleria.presenters.interfaces.PresentadorModelMapa;
import com.example.progaleria.presenters.interfaces.PresentadorViewMapa;
import com.example.progaleria.views.interfaces.ViewMapa;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class PresentadorImpMapa implements PresentadorViewMapa, PresentadorModelMapa {
    public String TAG = "Presentador";
    public final ViewMapa vista;
    public final ModelMapa modelo;

    public PresentadorImpMapa(ViewMapa vista){
        this.vista = vista;
        modelo = new ModeloImpMapa(this);
    }


    @Override
    public void obtenerFotos() {
        modelo.obtenerFotos();
        Log.i(TAG, "Obteniendo fotos ....");
    }

    @Override
    public void onSuccessFotos(List<FotoGaleria> fotos) {

        List<MarkerItem> markers = new ArrayList<>();
        for(FotoGaleria foto: fotos){
                double latitud = Double.parseDouble(foto.getLatitud());
                double longitud = Double.parseDouble(foto.getLongitud());
                LatLng ubicacion = new LatLng(latitud, longitud);
                MarkerItem marker = new MarkerItem(ubicacion, foto.getUrl());
                markers.add(marker);
        }
        vista.showMarkersFotosMap(markers);
        Log.i(TAG, "Se obtuvieron Exitosamente las fotos ");
    }

    @Override
    public void onError(String mensaje) {
        vista.toastError(mensaje);
    }

}
