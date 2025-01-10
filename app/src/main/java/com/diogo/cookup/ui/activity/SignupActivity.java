package com.diogo.cookup.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.diogo.cookup.R;
import com.diogo.cookup.data.model.ApiResponse;
import com.diogo.cookup.data.model.UserData;
import com.diogo.cookup.network.ApiRetrofit;
import com.diogo.cookup.network.ApiService;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    private EditText edit_name, edit_email, edit_password;
    private Button bt_singup;
    String[] message = {"Preencha todos os campos.", "Cadastro feito com sucesso"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        iniciarComponente();

        bt_singup.setOnClickListener(v -> {
            String nome = edit_name.getText().toString().trim();
            String email = edit_email.getText().toString().trim();
            String password = edit_password.getText().toString().trim();

            if (nome.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Snackbar snackbar = Snackbar.make(v, message[0], Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            } else {
                signupUser(v, nome, email, password);
            }
        });
    }

    private void signupUser(View v, String nome, String email, String password) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        if(auth.getCurrentUser() != null) {
                            String uid = auth.getCurrentUser().getUid();
                            String userName = nome;
                            String userEmail = email;

                            UserData userData = new UserData(userName, userEmail, uid);

                            ApiService apiService = ApiRetrofit.getClient().create(ApiService.class);
                            Call<ApiResponse> call = apiService.sendUserData(userData);

                            call.enqueue(new Callback<ApiResponse>() {
                                @Override
                                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                                    if(response.isSuccessful() && response.body() != null) {
                                        String msg = response.body().getMensagem();
                                    }
                                }
                                @Override
                                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                                }
                            });
                        }

                        Snackbar snackbar = Snackbar.make(v, message[1], Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.WHITE);
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.show();

                        new Handler().postDelayed(this::ScreenLogin, 2000);

                    } else {
                        String erro = getErrorString(task);
                        Snackbar snackbar = Snackbar.make(v, erro, Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.WHITE);
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.show();
                    }
                });
    }

    @NonNull
    private static String getErrorString(Task<AuthResult> task) {
        String erro;
        try {
            throw Objects.requireNonNull(task.getException());
        } catch (FirebaseAuthWeakPasswordException e) {
            erro = "Digite uma senha com no mínimo 6 caracteres.";
        } catch (FirebaseAuthUserCollisionException e) {
            erro = "Este email já está sendo usado.";
        } catch (FirebaseAuthInvalidCredentialsException e) {
            erro = "Email inválido.";
        } catch(Exception e) {
            erro = "Erro ao cadastrar, tente novamente mais tarde.";
        }
        return erro;
    }

    private void ScreenLogin() {
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void iniciarComponente() {
        edit_name = findViewById(R.id.input_name);
        edit_email = findViewById(R.id.input_email);
        edit_password = findViewById(R.id.input_password);
        bt_singup = findViewById(R.id.signup_button);
    }
}
