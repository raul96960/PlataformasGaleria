package com.example.progaleria.fragments.ubicacion.modelo;

import android.widget.Toast;

import com.example.progaleria.fragments.galeria.FotoGaleria;
import com.example.progaleria.fragments.ubicacion.presentador.PresentadorModel;
import com.example.progaleria.fragments.ubicacion.presentador.PresentadorView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class ModeloImp  implements  Modelo{
    private final String PATH = "FOTOS";
    private DatabaseReference mDatabaseRef;
    private final PresentadorModel presentador;

    public ModeloImp(PresentadorModel presentadorModel){
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
                    String url = postSnapshot.child("url").getValue().toString();
                    foto.setUrlIMG(url);
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
