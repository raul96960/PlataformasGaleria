package com.example.progaleria.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.progaleria.presenters.PresenterLoginImp;
import com.example.progaleria.presenters.interfaces.PresenterViewLogin;
import com.example.progaleria.R;
import com.example.progaleria.views.fragments.NavigationMain;
import com.example.progaleria.views.interfaces.ViewLogin;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, ViewLogin {


    private EditText email, password;
    private ProgressBar progressBar;
    private PresenterViewLogin presenterLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);


        email = (EditText) findViewById(R.id.editTextEmail);
        password = (EditText) findViewById(R.id.editTextPassword);
        progressBar = findViewById(R.id.progressBarIniciarSesion);

        presenterLogin = new PresenterLoginImp(this);

        TextView registrarse = (TextView) findViewById(R.id.registrar);
        Button login = (Button) findViewById(R.id.button_login);
        login.setOnClickListener(this);
        registrarse.setOnClickListener(this);

        cargarUnUsuarioDePrueba();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_login:
                presenterLogin.login(getEmail(), getPassword());
                break;

            case R.id.registrar:
                redirecToRegistrarse();
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
    public void onErrorLogin(String error) {
        toastShow(error);
    }


    @Override
    public void redirecToHome() {
        toastShow("LOGIN EXITO");
        startActivity(new Intent(LoginActivity.this, NavigationMain.class));
        finish();
    }

    public void redirecToRegistrarse(){
          Intent intent = new Intent(LoginActivity.this, RegistrarseActivity.class);
          startActivity(intent);
          //finish();
          //startActivityForResult(intent, 200);
    }

    public String getEmail() {
        return email.getText().toString();
    }

    public String getPassword() {
        return password.getText().toString();
    }


    public void cargarUnUsuarioDePrueba() {
        email.setText("test@example.com");
        password.setText("123456");
    }

    private void toastShow(String error) {
        Toast.makeText(this,error,Toast.LENGTH_SHORT).show();
    }
}
