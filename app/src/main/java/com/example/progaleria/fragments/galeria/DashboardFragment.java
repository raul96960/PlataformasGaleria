package com.example.progaleria.fragments.galeria;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.progaleria.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private GaleriaAdapter mGaleriaAdapter;

    private DatabaseReference mDatabaseRef;
    private ArrayList<FotoGaleria> mFotos;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_galeria, container, false);


        mRecyclerView = root.findViewById(R.id.recycler_view_ver_Galeria);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        mFotos = new ArrayList<>();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("FOTOS");

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

                mGaleriaAdapter = new GaleriaAdapter(getContext(),mFotos);
                mRecyclerView.setAdapter(mGaleriaAdapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }
}