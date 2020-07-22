package com.example.progaleria.fragments.ubicacion;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.progaleria.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class ClusterManagerRenderer extends DefaultClusterRenderer<ClusterMarker> {

    private final int IMAGE_DEFAULT_MARKET = R.drawable.image_marker_default;
    private final IconGenerator iconGenerator;
    private ImageView imageView;
    private final int markerWith;
    private final int getMarkerHeight;
    private Context context;



    public ClusterManagerRenderer(Context context, GoogleMap map, ClusterManager<ClusterMarker> clusterManager) {
        super(context, map, clusterManager);
        this.context = context;
        iconGenerator = new IconGenerator(context.getApplicationContext());
        imageView = new ImageView(context.getApplicationContext());
        markerWith = (int)context.getResources().getDimension(R.dimen.marker_image_with);
        getMarkerHeight = (int)context.getResources().getDimension(R.dimen.marker_image_height);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(markerWith,getMarkerHeight));

        int padding =  (int)context.getResources().getDimension(R.dimen.marker_image_padding);
        imageView.setPadding(padding, padding, padding, padding);
        iconGenerator.setContentView(imageView);
    }

    @Override
    protected void onBeforeClusterItemRendered(@NonNull ClusterMarker item, @NonNull MarkerOptions markerOptions) {

        imageView.setImageResource(IMAGE_DEFAULT_MARKET);
        Bitmap icon = iconGenerator.makeIcon();
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(item.getTitle());
    }

    @Override
    protected void onClusterItemRendered(@NonNull ClusterMarker clusterItem, @NonNull final Marker marker) {
        Glide.with(context)
                .load(clusterItem.getFoto().getUrlIMG())
                .centerCrop()
                .error(IMAGE_DEFAULT_MARKET)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        imageView.setImageDrawable(resource);
                        Bitmap icon = iconGenerator.makeIcon();
                        marker.setIcon(BitmapDescriptorFactory.fromBitmap(icon));
                    }
                });
    }



    @Override
    protected boolean shouldRenderAsCluster(@NonNull Cluster<ClusterMarker> cluster) {
        return false;
    }
}
