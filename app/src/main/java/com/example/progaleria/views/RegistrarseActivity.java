package com.example.progaleria.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.progaleria.MainActivity;
import com.example.progaleria.R;
import com.example.progaleria.presenters.interfaces.PresenterViewRegistrarse;
import com.example.progaleria.presenters.PresenterRegistrarseImp;
import com.example.progaleria.views.interfaces.ViewRegistrarse;


public class RegistrarseActivity extends AppCompatActivity implements View.OnClickListener, ViewRegistrarse {

    private EditText nombre, apellido, email, password;
    private ProgressBar progressBar;
    private PresenterViewRegistrarse presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);


        nombre = (EditText) findViewById(R.id.inputNombres);
        apellido = (EditText)findViewById(R.id.inputApellidos);
        email = (EditText)findViewById(R.id.inputEmail);
        password = (EditText)findViewById(R.id.inputPassword);
        progressBar = findViewById(R.id.progressBarRegistrarse);
        presenter = new PresenterRegistrarseImp(this);

        Button registrarse = (Button)findViewById(R.id.btnRegistrarse);
        TextView ir_a_login = (TextView)findViewById(R.id.ir_a_login);
        ir_a_login.setOnClickListener(this);
        registrarse.setOnClickListener(this);

    }

    private void eventoRegistrar(){
        presenter.registrarUsuario(getNombre(), getApellido(), getEmail(), getPassword());
    }

    private void toastShow(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    private String getNombre(){
        return nombre.getText().toString();
    }
    private String getApellido(){
        return apellido.getText().toString();
    }
    private String getEmail(){
        return email.getText().toString();
    }
    private String getPassword(){
        return password.getText().toString();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRegistrarse:
                eventoRegistrar();
                break;

            case R.id.ir_a_login:
                Log.i("dsa","DATOS");
                finish();//finishing activity
                break;
        }
    }

    @Override
    public void mostrarProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void ocultarProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void setEmailError(String error) {
        email.setError(error);
    }

    @Override
    public void setPassworError(String error) {
        password.setError(error);
    }

    @Override
    public void setNombreError(String error) {
        nombre.setError(error);
    }

    @Override
    public void setApellidoError(String error) {
        apellido.setError(error);
    }

    @Override
    public void onErrorRegistrarse(String error) {
        toastShow(error);
    }

    @Override
    public void redirecToHome() {
        toastShow("REGISTRO EXITO");
        startActivity(new Intent(RegistrarseActivity.this, MainActivity.class));
        finish();
    }
}
