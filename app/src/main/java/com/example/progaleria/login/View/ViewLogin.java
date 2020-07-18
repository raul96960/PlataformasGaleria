package com.example.progaleria.login.View;

public interface ViewLogin {

    public void mostrarProgressBar();
    public void ocultarProgressBar();
    public void setEmailError(String error);
    public void setPassworError(String error);
    public void onErrorLogin(String error);
    public void redirecToHome();

}
