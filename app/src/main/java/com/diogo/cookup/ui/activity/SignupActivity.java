package com.diogo.cookup.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.diogo.cookup.R;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity {

    private EditText edit_name,edit_email,edit_password;
    private Button bt_singup;
    String[] message = {"Preencha todos os campos.","cadastro feito com sucesso"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        iniciarComponente();

        bt_singup.setOnClickListener(v -> {

            String nome = edit_name.getText().toString();
            String email = edit_email.getText().toString();
            String password = edit_password.getText().toString();

            if (nome.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Snackbar snackbar = Snackbar.make(v,message[0],Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            }else{
                SingupUser(v);
            }

        });
    }

    private void SingupUser(View v) {

        String email = edit_email.getText().toString();
        String password = edit_password.getText().toString();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                Snackbar snackbar = Snackbar.make(v,message[1],Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            }else{
                String erro = getString(task);

                Snackbar snackbar = Snackbar.make(v,erro,Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
                }
            });
    }

    @NonNull
    private static String getString(Task<AuthResult> task) {
        String erro;
        try {
            throw Objects.requireNonNull(task.getException());

        }catch (FirebaseAuthWeakPasswordException e){
            erro = "digite passowrd no minimo 6 caractersss";
        }catch(FirebaseAuthUserCollisionException e){
            erro = "este email ja esta sendo usado";
        }catch(FirebaseAuthInvalidCredentialsException e){
            erro = "email invalido";
        }catch(Exception e){
            erro = "erro ao cadastra, tente novamente mais trade";
        }
        return erro;
    }

    private void iniciarComponente() {

        edit_name = findViewById(R.id.input_name);
        edit_email = findViewById(R.id.input_email);
        edit_password = findViewById(R.id.input_password);
        bt_singup = findViewById(R.id.signup_button);

    }

}