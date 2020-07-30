package com.example.progaleria.deteccionDeMovimiento.vista;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

class Localizacion implements LocationListener {

    Location locationLast;

    @Override
    public void onLocationChanged(Location location) {
        locationLast = location;

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }

    public LatLng getLastLatLng(){
            LatLng latLng = new LatLng(locationLast.getLatitude(), locationLast.getLongitude());
            return  latLng;
    }
}
