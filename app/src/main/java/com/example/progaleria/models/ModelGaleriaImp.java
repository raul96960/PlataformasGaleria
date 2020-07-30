package com.example.progaleria.models;

import com.example.progaleria.models.interfaces.ModelGaleria;
import com.example.progaleria.presenters.interfaces.PresenterModelGaleria;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class ModelGaleriaImp implements ModelGaleria {

    private final PresenterModelGaleria presenter;
    private final DatabaseReference mDatabaseRef;

    public ModelGaleriaImp(PresenterModelGaleria presenter){
        this.presenter = presenter;
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("FOTOS");
    }
    @Override
    public void obtenerFotos() {
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<FotoGaleria> galeria = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    FotoGaleria foto = postSnapshot.getValue(FotoGaleria.class);
                    galeria.add(foto);
                }
                presenter.onSuccessFotos(galeria);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                presenter.onError(databaseError.getMessage());
            }
        });
    }
}
