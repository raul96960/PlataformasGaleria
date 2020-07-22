package com.example.progaleria.fragments.ubicacion;

import com.example.progaleria.fragments.galeria.FotoGaleria;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ClusterMarker implements ClusterItem {
    private  LatLng position;
    private  String title;
    private  String snippet;
    private  FotoGaleria foto;

    public ClusterMarker(LatLng mPosition, String mTitle, String mSnippet, FotoGaleria foto) {
        this.position = mPosition;
        this.title = mTitle;
        this.snippet = mSnippet;
        this.foto = foto;
    }

    public FotoGaleria getFoto() {
        return foto;
    }

    public ClusterMarker(){}

    @NonNull
    @Override
    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    @Nullable
    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Nullable
    @Override
    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }


}
