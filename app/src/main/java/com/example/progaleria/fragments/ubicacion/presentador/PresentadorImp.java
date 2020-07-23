package com.example.progaleria.fragments.ubicacion.presentador;

import android.util.Log;

import com.example.progaleria.fragments.galeria.FotoGaleria;
import com.example.progaleria.fragments.ubicacion.modelo.Modelo;
import com.example.progaleria.fragments.ubicacion.modelo.ModeloImp;
import com.example.progaleria.fragments.ubicacion.modelo.ClusterMarker;
import com.example.progaleria.fragments.ubicacion.vista.Vista;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class PresentadorImp implements  PresentadorView, PresentadorModel{
    public String TAG = "Presentador";
    public final Vista vista;
    public final Modelo modelo;

    public PresentadorImp(Vista vista){
        this.vista = vista;
        modelo = new ModeloImp(this);
    }


    @Override
    public void obtenerFotos() {
        modelo.obtenerFotos();
        Log.i(TAG, "Obteniendo fotos ....");
    }

    @Override
    public void onSuccessFotos(List<FotoGaleria> fotos) {

        List<ClusterMarker> markers = new ArrayList<>();
        for(FotoGaleria foto: fotos){
                double latitud = Double.parseDouble(foto.getLatitud());
                double longitud = Double.parseDouble(foto.getLongitud());
                LatLng ubicacion = new LatLng(latitud, longitud);
                ClusterMarker marker = new ClusterMarker(ubicacion, foto.getUrlIMG());
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
