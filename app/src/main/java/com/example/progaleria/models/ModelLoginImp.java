package com.example.progaleria.models;

import com.example.progaleria.models.interfaces.ModelLogin;
import com.example.progaleria.presenters.interfaces.PresenterModelLogin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
//comentario
public class ModelLoginImp implements ModelLogin {
    private PresenterModelLogin listenerLogin;
    private FirebaseAuth mAuth;

    public ModelLoginImp(PresenterModelLogin listenerLogin) {
        this.listenerLogin = listenerLogin;
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void login(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    listenerLogin.onSuccess();
                } else {

                    listenerLogin.onError("Usuario o password incorrecto");
                }

            }
        });
    }
}
