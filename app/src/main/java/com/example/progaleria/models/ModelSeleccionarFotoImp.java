package com.example.progaleria.models;

import android.net.Uri;

import androidx.annotation.NonNull;


import com.example.progaleria.models.interfaces.ModelSeleccionarFoto;
import com.example.progaleria.presenters.interfaces.PresentadorModelSeleccionarFoto;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

public class ModelSeleccionarFotoImp implements ModelSeleccionarFoto {

    private StorageReference storageFotos;
    private DatabaseReference referenceFotos;
    private PresentadorModelSeleccionarFoto presenter;

    public ModelSeleccionarFotoImp(PresentadorModelSeleccionarFoto presenter) {
        this.presenter = presenter;

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        storageFotos = storageRef.child("FOTOS");
        referenceFotos = database.getReference("FOTOS");
    }

    @Override
    public void guardarFoto(Uri imageUri, final LatLng latLng) {
        final StorageReference fotoRef = storageFotos.child(new Date().toString());

        fotoRef.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()){
                    throw task.getException();
                }
                return fotoRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {

                if (task.isSuccessful()){
                    Uri uri = task.getResult();

                    FotoGaleria foto = new FotoGaleria();
                    foto.setLatitud(String.valueOf(latLng.latitude));
                    foto.setLongitud(String.valueOf(latLng.longitude));
                    foto.setUrl(uri.toString());

                    referenceFotos.push().setValue(foto);
                    presenter.onSuccess();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                presenter.onError("Error Subir foto "+e.getMessage());
            }
        });
    }
}
