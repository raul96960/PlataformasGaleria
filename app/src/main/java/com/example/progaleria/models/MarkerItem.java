package com.example.progaleria.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MarkerItem implements ClusterItem {
    private final String title = "Ver Foto";
    private final String snippet = "Descripción";
    private String imageUrl;
    private LatLng position;

    public MarkerItem(LatLng position, String imageUrl) {
        this.position = position;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @NonNull
    @Override
    public LatLng getPosition() {
        return position;
    }

    @Nullable
    @Override
    public String getTitle() {
        return title;
    }

    @Nullable
    @Override
    public String getSnippet() {
        return snippet;
    }


}
