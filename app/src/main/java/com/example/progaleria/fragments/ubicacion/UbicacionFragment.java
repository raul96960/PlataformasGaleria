package com.example.progaleria.fragments.ubicacion;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.progaleria.R;
import com.example.progaleria.fragments.galeria.FotoGaleria;
import com.example.progaleria.fragments.galeria.GaleriaAdapter;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

public class UbicacionFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    private GoogleMap mMap;
    private LocationManager ubicacion;
    private ArrayList<FotoGaleria> mFotos;
    private DatabaseReference mDatabaseRef;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_ubicacion, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);

        mFotos = new ArrayList<>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("FOTOS");





        return root;
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this.getContext(), "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        int zoom = 17;

        LatLng ubicacion = new LatLng(-16.30 , -71.62);
        MarkerOptions positionMarker = new MarkerOptions().position(ubicacion).title("Mi Ubicacion").snippet("Descripcion Breve");
        mMap.addMarker(positionMarker);

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

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this.getContext(), "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        int zoom = 17;
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mDatabaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            FotoGaleria lasFotos = postSnapshot.getValue(FotoGaleria.class);

                            String nombre = postSnapshot.child("latitud").getValue().toString();
                            String url = postSnapshot.child("url").getValue().toString();
                            lasFotos.setUrlIMG(url);
                            mFotos.add(lasFotos);
                        }

                        if (dataSnapshot.exists()) {
                            final LatLngBounds.Builder builder = new LatLngBounds.Builder();
                            for (FotoGaleria aviso : mFotos) {

                                Log.e("DATO BASe DE DATOS", aviso.getLatitud() + " "+aviso.getLongitud());
                                double latitud = Double.parseDouble(aviso.getLatitud());
                                double longitud = Double.parseDouble(aviso.getLongitud());
                                //LatLng ubicacion = new LatLng(latitud, longitud);
                                //MarkerOptions positionMarker = new MarkerOptions().position(ubicacion).title("Foto").snippet("Imagen");
                                //mMap.addMarker(positionMarker);
                            }
                           // LatLngBounds bounds = builder.build();
                           // CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 200);
                            //mMap.moveCamera(cu);
                            //mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }


}