package com.example.progaleria.presenters;

import android.util.Patterns;

import com.example.progaleria.models.interfaces.ModelLogin;
import com.example.progaleria.models.ModelLoginImp;
import com.example.progaleria.presenters.interfaces.PresenterModelLogin;
import com.example.progaleria.presenters.interfaces.PresenterViewLogin;
import com.example.progaleria.views.interfaces.ViewLogin;

public class PresenterLoginImp implements PresenterViewLogin, PresenterModelLogin {

    private final String REQUIRED = "required";

    private ModelLogin modelo;
    private ViewLogin vista;


    public PresenterLoginImp(ViewLogin vista) {
        this.vista = vista;
        modelo = new ModelLoginImp(this);
    }

    @Override
    public void onSuccess() {
        vista.ocultarProgressBar();
        vista.redirecToHome();
    }

    @Override
    public void onError(String error) {
        vista.ocultarProgressBar();
        vista.onErrorLogin(error);
    }

    @Override
    public void login(String email, String password) {
        if(validarEmailAndPassword(email, password)){
            vista.mostrarProgressBar();
            modelo.login(email, password);
        }
    }


    private boolean validarEmailAndPassword(String email, String password) {
        return validarEmail(email) & validarPassword(password);
    }

    private boolean validarEmail(String email) {
        boolean valido = true;

        if (isEmpty(email)) {
            vista.setEmailError(REQUIRED);
            valido = false;
        } else if (! isEmailValid(email)) {
            vista.setEmailError("email no valido");
            valido = false;
        }
        return valido;
    }

    private boolean validarPassword(String password) {
        boolean valido = true;

        if (isEmpty(password)) {
            vista.setPassworError(REQUIRED);
            valido = false;
        } else if (password.length() < 6) {
            vista.setPassworError("password > 6");
            valido = false;
        }
        return valido;
    }

    private boolean isEmpty(String cadena){
        if("".equals(cadena))
            return true;
        return false;
    }

    private boolean isEmailValid(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
