package com.example.progaleria.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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

public class MarkerManagerRenderer extends DefaultClusterRenderer<MarkerItem> {
    private final int IMAGE_DEFAULT_MARKER = R.drawable.image_marker_default;
    private final IconGenerator iconGenerator;
    private ImageView imageView;
    private Context context;

    public MarkerManagerRenderer(Context context, GoogleMap map, ClusterManager<MarkerItem> clusterManager) {
        super(context, map, clusterManager);
        this.context = context;
        iconGenerator = new IconGenerator(context.getApplicationContext());
        imageViewCreate();
        iconGenerator.setContentView(imageView);
    }

    @Override
    protected void onBeforeClusterItemRendered(@NonNull MarkerItem item, @NonNull MarkerOptions markerOptions) {
        imageView.setImageResource(IMAGE_DEFAULT_MARKER);
        Bitmap icon = iconGenerator.makeIcon();
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(item.getTitle());
    }

    @Override
    protected void onClusterItemRendered(@NonNull MarkerItem clusterItem, @NonNull final Marker marker) {
        renderizarFotosAsync(clusterItem, marker);
    }
    private void renderizarFotosAsync(MarkerItem clusterItem, final Marker marker){
        Glide.with(context)
                .load(clusterItem.getImageUrl())
                .centerCrop()
                .error(IMAGE_DEFAULT_MARKER)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        actualizarFotoMarker(resource, marker);
                    }
                });
    }

    private void actualizarFotoMarker(Drawable resource, Marker marker){
        imageView.setImageDrawable(resource);
        Bitmap icon = iconGenerator.makeIcon();
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(icon));
    }

    @Override
    protected boolean shouldRenderAsCluster(@NonNull Cluster<MarkerItem> cluster) {
        return false;
    }

    private void imageViewCreate(){
        imageView = new ImageView(context.getApplicationContext());;
        imageView.setLayoutParams(new ViewGroup.LayoutParams(markerWith(), getMarkerHeight()));
        int padding =  padding();
        imageView.setPadding(padding, padding, padding, padding);
    }

    private int markerWith(){
        return (int)context.getResources().getDimension(R.dimen.marker_image_with);
    }

    private int getMarkerHeight(){
        return (int)context.getResources().getDimension(R.dimen.marker_image_height);
    }

    private int padding(){
        return (int)context.getResources().getDimension(R.dimen.marker_image_padding);
    }
}
