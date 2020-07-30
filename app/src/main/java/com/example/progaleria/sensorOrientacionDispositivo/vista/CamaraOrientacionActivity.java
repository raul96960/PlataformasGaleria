package com.example.progaleria.sensorOrientacionDispositivo.vista;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.location.LocationManager;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.progaleria.R;
import com.example.progaleria.deteccionDeMovimiento.modelo.Localizacion;
import com.example.progaleria.deteccionDeMovimiento.presentador.PresentadorImp;
import com.example.progaleria.deteccionDeMovimiento.presentador.PresentadorView;
import com.example.progaleria.deteccionDeMovimiento.vista.Vista;
import com.example.progaleria.sensorOrientacionDispositivo.modelo.ICamaraSensorOrientation;
import com.example.progaleria.sensorOrientacionDispositivo.modelo.SensorOrientacion;
import com.google.android.gms.maps.model.LatLng;

public class CamaraOrientacionActivity extends AppCompatActivity implements ICamaraSensorOrientation, Vista {
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

    private static final String TOAST_MENSAJE = "Disponga el telefono en modo Horizontal";

    private Toast message;
    private static final String TAG = "OrientacionCamara";
    private static final int CAMERAID = 0;
    private String cameraId = null;

    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private ImageReader mImageReader;

    private CameraDevice mCamera;
    private CameraManager cameraManager;
    private CameraCaptureSession mCaptureSession;
    private CaptureRequest.Builder mPreviewCaptureRequest;

    private SensorManager sensorManager;
    private SensorOrientacion sensorOrientacion;

    private LocationManager locationManager;
    private Localizacion localizacion;

    private PresentadorView presentador;
    private boolean showModal = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camara_deteccion_movimiento);
        setTitle("Tomar Foto");

        verifyStoragePermissions(this);
        verifyCameraPermissions(this);
        verifyLocationPermissions(this);
        initCamara();
        initSensorOrientacion();
        initSensorGPS();
        initToast();
        presentador = new PresentadorImp(this);

    }

    private void initCamara() {
        mSurfaceView = findViewById(R.id.frameLayoutCamera);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setKeepScreenOn(true);
        mSurfaceHolder.addCallback(surfaceHolderCallback);
        mSurfaceHolder.setFixedSize(640, 480);

        Button btnTakePicture = (Button) findViewById(R.id.btnTomarFoto);
        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Foto Tomada");
                takePicture();
            }
        });
    }

    private void initSensorOrientacion(){
        sensorManager  = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorOrientacion = new SensorOrientacion(this);
    }

    private void initToast(){
        message = Toast.makeText(this, TOAST_MENSAJE, Toast.LENGTH_LONG);
        message.setGravity(Gravity.CENTER, 0, 0);
    }

    public void initSensorGPS() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        localizacion = new Localizacion();
    }

    public void listenerGPS() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, localizacion);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, localizacion);
    }

    public void unregisterListenerGPS(){
        locationManager.removeUpdates(localizacion);
    }

    public void listenerSensorOrientacion() {
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor sensor1 = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        sensorManager.registerListener(sensorOrientacion, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorOrientacion, sensor1, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unregisterListenerOrientacion() {
        sensorManager.unregisterListener(sensorOrientacion);
    }

    @Override
    protected void onResume() {
        super.onResume();
        listenerSensorOrientacion();
        listenerGPS();
    }

    @Override
    protected void onPause() {
        super.onPause();
        message.cancel();
        unregisterListenerOrientacion();
        unregisterListenerGPS();
    }

    private void startCameraCaptureSession() {

        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraId = cameraManager.getCameraIdList()[CAMERAID];
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            cameraManager.openCamera(cameraId, cameraDeviceCallback, null);
            Log.e(TAG, "Camera is open");
        } catch (Exception e) {
            Log.e(TAG, "unable to open the camera " + e);
        }


        int largestwidth = 640;
        int largestheight = 480;
        mImageReader = ImageReader.newInstance(largestwidth, largestheight, ImageFormat.JPEG, 1);
        mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() { //You can process the temporary photo taken here, for example, write local
            @Override
            public void onImageAvailable(ImageReader reader) {
                try (Image image = reader.acquireNextImage()) {
                    Image.Plane[] planes = image.getPlanes();
                    if (planes.length > 0) {
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.remaining()];
                        buffer.get(bytes);//Save the byte array from the buffer

                        Uri foto = saveFoto(bytes);
                        showModal = true;
                        message.cancel();
                        alertDialogPreviewImage(foto);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, null);
    }

    private void takePreview() {
        if (mCamera == null || mSurfaceHolder.isCreating()) {
            return;
        }

        try {
            Surface previewSurface = mSurfaceHolder.getSurface();
            mPreviewCaptureRequest = mCamera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mPreviewCaptureRequest.addTarget(previewSurface);
            mCamera.createCaptureSession(Arrays.asList(previewSurface, mImageReader.getSurface()),
                    captureSessionCallback, null);

        } catch (CameraAccessException e) {
            Log.e(TAG, "Camera Access Exception", e);
        }

    }

    private void takePicture() {
        if (mCamera == null) return;

        try {
            CaptureRequest.Builder takePictureBuilder = mCamera.createCaptureRequest(
                    CameraDevice.TEMPLATE_STILL_CAPTURE);

            takePictureBuilder.addTarget(mImageReader.getSurface());

            CaptureRequest mCaptureRequest = takePictureBuilder.build();
            mCaptureSession.capture(mCaptureRequest, null, null);

        } catch (CameraAccessException e) {
            Log.e(TAG, "Error capturing the photo", e);
        }
    }

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

            FileOutputStream  outStream = new FileOutputStream(imageFile);
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
                showModal = false;
            }
        });
        AlertDialog alertDialog = popupDialogBuilder.create();

        alertDialog.show();
    }

    @Override
    public void handleOrientationDevice(int orientacion) {
        if(!showModal) {
            if (isVerticalDevice(orientacion)) {
                message.show();
            } else {
                message.cancel();
            }
        }
    }

    @Override
    public void onSuccess() {
        Log.i(TAG,"Foto Subida Exitosamente");
        message.cancel();
        Toast.makeText(getApplicationContext(),"Foto subida exitosamente", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String message) {
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
    }

    private boolean isVerticalDevice(int orientation) {
        return orientation == SensorOrientacion.ORIENTATION_PORTRAIT || orientation == SensorOrientacion.ORIENTATION_PORTRAIT_REVERSE;
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

    private SurfaceHolder.Callback surfaceHolderCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            startCameraCaptureSession();
        }
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format,
                                   int width, int height) {
        }
    };

    private CameraDevice.StateCallback cameraDeviceCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            mCamera = camera;
            takePreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            camera.close();
            mCamera = null;
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            camera.close();
            mCamera = null;
            Log.e(TAG, "Camera Error: " + error);
        }
    };

    private CameraCaptureSession.StateCallback captureSessionCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {
            mCaptureSession = session;
            try {
                mCaptureSession.setRepeatingRequest(mPreviewCaptureRequest.build(), null, null);
            } catch (CameraAccessException | IllegalStateException e) {
                Log.e(TAG, "Capture Session Exception", e);
            }
        }
        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {

        }
    };


}