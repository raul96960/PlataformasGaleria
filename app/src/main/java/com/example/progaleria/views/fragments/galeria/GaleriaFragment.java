package com.example.progaleria.views.fragments.galeria;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.progaleria.R;
import com.example.progaleria.models.FotoGaleria;
import com.example.progaleria.models.GaleriaAdapter;
import com.example.progaleria.presenters.PresenterGaleriaImp;
import com.example.progaleria.presenters.interfaces.PresenterViewGaleria;
import com.example.progaleria.views.interfaces.ViewGaleria;

import java.util.ArrayList;
import java.util.List;

public class GaleriaFragment extends Fragment implements ViewGaleria {

    private RecyclerView mRecyclerView;
    private GaleriaAdapter mGaleriaAdapter;
    private PresenterViewGaleria presentador;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_galeria, container, false);

        mRecyclerView = root.findViewById(R.id.recycler_view_ver_Galeria);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        presentador = new PresenterGaleriaImp(this);
        presentador.obtenerFotos();

        return root;
    }

    @Override
    public void showFotos(List<FotoGaleria> fotos) {

        try {
            Geocoder a = new Geocoder(getContext());
            for(FotoGaleria foto: fotos){
                double latitud = Double.parseDouble(foto.getLatitud());
                double longitud = Double.parseDouble(foto.getLongitud());
                List<Address> address = a.getFromLocation(latitud, longitud, 1);
                String lugar = address.get(0).getCountryName();
                foto.setLugarDescripcion(lugar);
            }

        }
        catch(Exception e){
            Log.e("GaleriaFragment",e.getMessage());
        }

        mGaleriaAdapter = new GaleriaAdapter(getContext(), (ArrayList<FotoGaleria>) fotos);
        mRecyclerView.setAdapter(mGaleriaAdapter);
    }

    @Override
    public void onError(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();

    }
}