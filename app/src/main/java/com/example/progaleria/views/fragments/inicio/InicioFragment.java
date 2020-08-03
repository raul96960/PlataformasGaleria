package com.example.progaleria.views.fragments.inicio;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.progaleria.R;
import com.example.progaleria.views.MiCamara2Activity;
import com.example.progaleria.views.SeleccionarFotoActivity;

public class InicioFragment extends Fragment implements  View.OnClickListener{

    private Button btnCamara2;
    private Button btnSeleccionarImagenDeGaleria;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_inicio, container, false);


        btnCamara2 = root.findViewById(R.id.btnCamara2);
        btnCamara2.setOnClickListener(this);

        btnSeleccionarImagenDeGaleria = root.findViewById(R.id.btnSeleccioneFotoGaleria);
        btnSeleccionarImagenDeGaleria.setOnClickListener(this);

        return root;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCamara2:
                startActivityCamara2();
                break;
            case R.id.btnSeleccioneFotoGaleria:
                startActivitySeleccioneFotoGaleria();
                break;
        }
    }

    public void startActivityCamara2(){
        Intent intent = new Intent(getContext(), MiCamara2Activity.class);
        startActivity(intent);
    }
    public void startActivitySeleccioneFotoGaleria(){
        Intent intent = new Intent(getContext(), SeleccionarFotoActivity.class);
        startActivity(intent);
    }


}
