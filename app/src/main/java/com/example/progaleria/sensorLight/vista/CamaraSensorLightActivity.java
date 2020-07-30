package com.example.progaleria.sensorLight.vista;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.progaleria.R;
import com.example.progaleria.deteccionDeMovimiento.modelo.Localizacion;
import com.example.progaleria.deteccionDeMovimiento.presentador.PresentadorImp;
import com.example.progaleria.deteccionDeMovimiento.presentador.PresentadorView;
import com.example.progaleria.deteccionDeMovimiento.vista.Vista;
import com.example.progaleria.sensorLight.modelo.SensorLight;
import com.example.progaleria.sensorLight.modelo.SurfaceCamera;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CamaraSensorLightActivity extends AppCompatActivity implements ICamaraSensorLight, Vista {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_EXTERNAL_CAMERA = 2;
    private static final int REQUEST_EXTERNAL_LOCATION = 3;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static String[] PERMISSIONS_CAMERA = {
            Manifest.permission.CAMERA
    };
    private static String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
    };

    private final float LIGHT_LIMITE = 2f; //

    private String TAG = "LightActivity";
    private SensorManager sensorManager;
    private SensorLight sensorLight;

    private LocationManager locationManager;
    private Localizacion localizacion;

    private Camera camera;
    private FrameLayout frameLayout;
    private SurfaceCamera surfaceCamera;
    private boolean flashMode = false;

    private PresentadorView presentador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camara_sensor_light);

        verifyStoragePermissions(this);
        verifyCameraPermissions(this);
        verifyLocationPermissions(this);
        
        initCamera();
        initSensorLight();
        initSensorGPS();
        presentador = new PresentadorImp(this);

    }

    public void initCamera() {
        frameLayout = findViewById(R.id.frameLayoutCamera);
        camera = Camera.open();
        surfaceCamera = new SurfaceCamera(this, camera);
        frameLayout.addView(surfaceCamera);

        Button btnTakePicture = (Button) findViewById(R.id.btnTomarFoto);
        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Foto Tomada");
                if (camera != null) {
                    camera.takePicture(null, null, mPictureCallBack);
                }
            }
        });
    }

    public void initSensorLight() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorLight = new SensorLight(this);
    }

    public void initSensorGPS() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        localizacion = new Localizacion();
    }

    public void listenerSensorLight() {
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener(
                sensorLight,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unregisterListenerLight() {
        sensorManager.unregisterListener(sensorLight);
    }

    public void listenerGPS() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i("TAG","dsadsaasd");
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
        listenerSensorLight();
        listenerGPS();

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterListenerLight();
        unregisterListenerGPS();
    }
    /*
     * Recibe la cantidad de luz del ambiente
     * A partir de esa informacion si el ambiente es oscuro HABILITA FLASH
     * Caso contrario DESABILITA EL FLASH
     */
    @Override
    public void handleSensorLight(float light) {
        if (light <= LIGHT_LIMITE) {
            if (!flashMode) {
                flashMode = true;
                activarFlashModeCamera();
                Toast.makeText(this, "Habilitando Flash", Toast.LENGTH_LONG).show();
            }
        } else {
            if (flashMode) {
                flashMode = false;
                desactivarFlashModeCamera();
                Toast.makeText(this, "Desabilitando Flash", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void activarFlashModeCamera() {
        setFlashMode(Camera.Parameters.FLASH_MODE_ON);
    }

    public void desactivarFlashModeCamera() {
        setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
    }

    private void setFlashMode(String flashMode) {
        Camera.Parameters parameters = camera.getParameters();
        parameters.setFlashMode(flashMode);
        camera.setParameters(parameters);
    }

    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public static void verifyCameraPermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_CAMERA,
                    REQUEST_EXTERNAL_CAMERA
            );
        }
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

    private PictureCallback mPictureCallBack = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            Uri image = saveFoto(bytes);
            alertDialogPreviewImage(image);
        }
    };

    private Uri saveFoto(byte[] data) {
        Uri imageUri = null;
        try {
            File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File progaleria = new File(pictureDirectory, "progaleria");

            if (!progaleria.exists()) {
                Log.i(TAG, "Creando carpeta progaleria"+progaleria.getAbsolutePath());
                progaleria.mkdir();
            }

            String fileName = fileNameUnique();
            File imageFile = new File(progaleria, fileName);

            FileOutputStream outStream = new FileOutputStream(imageFile);
            outStream.write(data);
            outStream.close();

            Log.i(TAG, "Creando la foto en" + imageFile.getAbsolutePath());

            imageUri = Uri.fromFile(imageFile);

        } catch (FileNotFoundException e) {
            Log.e(TAG, "File Not Found", e);
        } catch (IOException e) {
            Log.e(TAG, "IO Exception", e);
        }
        return imageUri;
    }

    private String fileNameUnique(){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = timeStamp + ".jpg";

        return fileName;
    }

    private void alertDialogPreviewImage(final Uri image){

        AlertDialog.Builder popupDialogBuilder = new AlertDialog.Builder(this);
        ImageView popupImv = new ImageView(this);
        popupImv.setAdjustViewBounds(true);
        popupImv.setImageURI(image);
        popupDialogBuilder.setView(popupImv);
        popupDialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.i(TAG, "Subiendo Foto ...." + i);
                LatLng ubicacion = localizacion.getLastLatLng();
                presentador.guardarFoto(ubicacion, image);
            }
        });

        popupDialogBuilder.setNegativeButton("Cancelar", null);
        popupDialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Log.i(TAG, "Cerrando Modal");
              //  showModal = false;
            }
        });
        AlertDialog alertDialog = popupDialogBuilder.create();

        alertDialog.show();
    }

    @Override
    public void onSuccess() {
        Log.i(TAG,"Foto Subida Exitosamente");
      //  message.cancel();
        Toast.makeText(getApplicationContext(),"Foto subida exitosamente", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String message) {
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
    }
}