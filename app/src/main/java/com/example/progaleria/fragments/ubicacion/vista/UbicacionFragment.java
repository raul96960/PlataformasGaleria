package com.example.progaleria.fragments.ubicacion.vista;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.progaleria.R;
import com.example.progaleria.fragments.ubicacion.modelo.ClusterManagerRenderer;
import com.example.progaleria.fragments.ubicacion.modelo.ClusterMarker;
import com.example.progaleria.fragments.ubicacion.presentador.PresentadorImp;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

import java.util.List;

public class UbicacionFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, Vista {

    private GoogleMap mMap;
    private ClusterManager<ClusterMarker> mClusterManager;
    private ClusterManagerRenderer mClusterManagerRenderer;

    private PresentadorImp presentador;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_ubicacion, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);


        presentador = new PresentadorImp(this);
        return root;
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this.getContext(), "Ubicacion Aproximada", Toast.LENGTH_SHORT).show();
        int zoom = 17;

        LatLng ubicacion = new LatLng(-16.30 , -71.62);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, zoom));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(ubicacion)      // Sets the center of the map to location user
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        return true;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        int zoom = 17;
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                presentador.obtenerFotos();
            }
        });

    }

    @Override
    public void showMarkersFotosMap(List<ClusterMarker> markers) {
        if(mClusterManager == null){
            mClusterManager = new ClusterManager<ClusterMarker>(getActivity().getApplicationContext(), mMap);
        }
        if(mClusterManagerRenderer == null){
            mClusterManagerRenderer = new ClusterManagerRenderer(getActivity(), mMap, mClusterManager);
            mClusterManager.setRenderer(mClusterManagerRenderer);
        }

        for(ClusterMarker marker: markers){
                mClusterManager.addItem(marker);
        }

        onMarkerItemListener();
        mClusterManager.cluster();
    }

    private void onMarkerItemListener(){
        mClusterManager.setOnClusterItemInfoWindowClickListener(new ClusterManager.OnClusterItemInfoWindowClickListener<ClusterMarker>() {
            @Override
            public void onClusterItemInfoWindowClick(ClusterMarker item) {
                modalDialogImage(item);
            }
        });
    }

    private void modalDialogImage(ClusterMarker item){
        AlertDialog.Builder popupDialogBuilder = new AlertDialog.Builder(getContext());
        final ImageView popupImv = new ImageView(getContext());
        popupImv.setAdjustViewBounds(true);
        Glide.with(getContext())
                .load(item.getImageUrl())
                .centerCrop()
                .error(R.drawable.image_marker_default)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        popupImv.setImageDrawable(resource);
                    }
                });
        popupDialogBuilder.setView(popupImv);
        AlertDialog alertDialog = popupDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void toastError(String mensaje) {
        Toast.makeText(getContext(), mensaje,Toast.LENGTH_LONG).show();
    }

}