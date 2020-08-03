package com.example.progaleria.views;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.progaleria.R;
import com.example.progaleria.models.sensores.Localizacion;
import com.example.progaleria.presenters.PresenterSeleccionarFotoImp;
import com.example.progaleria.presenters.interfaces.PresentadorViewSeleccionarFoto;
import com.example.progaleria.views.interfaces.ViewSeleccionarFoto;
import com.google.android.gms.maps.model.LatLng;

public class SeleccionarFotoActivity extends AppCompatActivity implements View.OnClickListener, ViewSeleccionarFoto {

    private ImageView ivFoto;
    private ImageButton btnSeleccionarImagen;
    private ImageButton btnSubirData;
    private ProgressDialog dialog;

    private static final int REQUEST_EXTERNAL_LOCATION = 3;
    private static String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
    };
    private static final String TAG = "SeleccionarFotoActivity";

    private LocationManager locationManager;
    private Localizacion localizacion;

    private TextView txtlatitudd;
    private TextView txtlongitudd;

    private Uri imagenUri;
    private int SELEC_IMAGEN = 200;

    private PresentadorViewSeleccionarFoto presentador;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_foto);


        verifyLocationPermissions(this);
        initSensorGPS();
        initProgressDialog();

        presentador = new PresenterSeleccionarFotoImp(this);
        txtlatitudd = findViewById(R.id.txtLatitud);
        txtlongitudd = findViewById(R.id.txtLongitud);
        ivFoto = findViewById(R.id.imgFoto);



        btnSeleccionarImagen = findViewById(R.id.btnSeleccionarImagen);
        btnSubirData = findViewById(R.id.btnSubirData);

        btnSeleccionarImagen.setOnClickListener(this);
        btnSubirData.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSeleccionarImagen:
                seleccionarImagen();
                break;
            case R.id.btnSubirData:
                if(imagenUri!=null){
                    LatLng ubicacion = localizacion.getLastLatLng();
                    presentador.guardarFoto(ubicacion, imagenUri);
                    dialog.show();
                }
                break;

        }
    }

    public void seleccionarImagen() {
        Intent galeria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(galeria, SELEC_IMAGEN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == SELEC_IMAGEN) {
            imagenUri = data.getData();
            ivFoto.setImageURI(imagenUri);
        }
    }


    /*************************************************/

    private void initProgressDialog(){
        dialog=new ProgressDialog(this);
        dialog.setMessage("Subiendo foto");
        dialog.setCancelable(false);
    }


    public static void verifyLocationPermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        int permission2 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (permission != PackageManager.PERMISSION_GRANTED && permission2 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_LOCATION,
                    REQUEST_EXTERNAL_LOCATION
            );
        }
    }

    public void initSensorGPS() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        localizacion = new Localizacion();
        /*
        txtlatitudd.setText(String.valueOf(localizacion.getLastLatLng().latitude));
        txtlongitudd.setText(String.valueOf(localizacion.getLastLatLng().longitude));
         */
    }

    public void listenerGPS() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i("TAG","actualizando GPS");
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, localizacion);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, localizacion);

    }

    public void unregisterListenerGPS(){
        locationManager.removeUpdates(localizacion);
    }

    @Override
    protected void onResume() {
        super.onResume();
        listenerGPS();

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterListenerGPS();
    }


    @Override
    public void onSuccess() {
        Log.i(TAG,"Foto Subida Exitosamente");
        dialog.dismiss();
        Toast.makeText(getApplicationContext(),"Foto subida exitosamente", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onError(String message) {
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
    }
}
