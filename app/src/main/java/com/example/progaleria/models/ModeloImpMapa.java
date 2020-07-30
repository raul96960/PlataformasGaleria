package com.example.progaleria.models;

import com.example.progaleria.models.interfaces.ModelMapa;
import com.example.progaleria.presenters.interfaces.PresentadorModelMapa;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class ModeloImpMapa implements ModelMapa {
    private final String PATH = "FOTOS";
    private DatabaseReference mDatabaseRef;
    private final PresentadorModelMapa presentador;

    public ModeloImpMapa(PresentadorModelMapa presentadorModel){
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(PATH);
        presentador = presentadorModel;
    }

    @Override
    public void obtenerFotos() {
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<FotoGaleria> fotos = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    FotoGaleria foto = postSnapshot.getValue(FotoGaleria.class);
                    fotos.add(foto);
                }
                presentador.onSuccessFotos(fotos);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                String errorMensaje = databaseError.getMessage();
                presentador.onError(errorMensaje);
            }
        });
    }
}
