package com.example.progaleria;

import android.content.Intent;
import android.os.Bundle;
import com.example.progaleria.login.View.IniciarSesion;
import com.example.progaleria.fragments.NavigationMain;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity{
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        iniciarAplicacion();

    }
    public void iniciarAplicacion(){
        if(usuarioLogeado()){
            startActivityAplicacion();
        } else {
            startActivityLogin();
        }
    }
    private void startActivityAplicacion(){
        Intent intent = new Intent(this, NavigationMain.class);
        startActivity(intent);
        finish();
    }

    private void startActivityLogin(){
        Intent intent = new Intent(this, IniciarSesion.class);
        startActivity(intent);
        finish();
    }

    private boolean usuarioLogeado(){
        return mAuth.getCurrentUser() != null;
    }
}