package com.example.progaleria.SensorGPS;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;

public class Ubicacion implements LocationListener {
    private FotoGeoLocalizadaActivity mActivity;
    private Context mcontex;
    private String latitud;
    private String longitud;

    public Ubicacion(){

    }

    public Ubicacion(Context mcontex, String latitud, String longitud) {
        this.mcontex = mcontex;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
    // debido a la deteccion de un cambio de ubicacion
    @Override
    public void onLocationChanged(Location location) {
        this.latitud = String.valueOf(location.getLatitude());
        this.longitud = String.valueOf(location.getLongitude());

    }

    // Este metodo se ejecuta cuando el GPS es desactivado
    @Override
    public void onProviderEnabled(String provider) {
        this.latitud = "GPS Activado";
    }

    // Este metodo se ejecuta cuando el GPS es activado
    @Override
    public void onProviderDisabled(String provider) {
        this.latitud = "GPS desactivado";
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status) {
            case LocationProvider.AVAILABLE:
                Log.d("debug", "LocationProvider.AVAILABLE");
                break;
            case LocationProvider.OUT_OF_SERVICE:
                Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                break;
        }
    }

    public Activity getmActivity(FotoGeoLocalizadaActivity fotoGeoLocalizadaActivity) {
        return mActivity;
    }

    public void setmActivity(FotoGeoLocalizadaActivity mActivity) {
        this.mActivity = mActivity;
    }

}
