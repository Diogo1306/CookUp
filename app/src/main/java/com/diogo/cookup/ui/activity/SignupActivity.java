package com.diogo.cookup.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.diogo.cookup.R;
import com.diogo.cookup.data.model.ApiResponse;
import com.diogo.cookup.data.model.UserData;
import com.diogo.cookup.network.ApiRetrofit;
import com.diogo.cookup.network.ApiService;
import com.diogo.cookup.utils.NavigationUtils;
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

    private TextView singuptologin;
    private EditText edit_name, edit_email, edit_password;
    private Button bt_singup;
    private boolean isPasswordVisible = false; // Estado da visibilidade da senha

    private final String[] message = {"Preencha todos os campos.", "Cadastro feito com sucesso"};

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        iniciarComponente();

        NavigationUtils.setupBackButton(this, R.id.arrow_back);

        singuptologin.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        edit_password.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (edit_password.getRight() - edit_password.getCompoundDrawables()[2].getBounds().width())) {
                    togglePasswordVisibility();
                    return true;
                }
            }
            return false;
        });

        bt_singup.setOnClickListener(v -> {
            String nome = edit_name.getText().toString().trim();
            String email = edit_email.getText().toString().trim();
            String password = edit_password.getText().toString().trim();

            if (nome.isEmpty() || email.isEmpty() || password.isEmpty()) {
                mostrarSnackbar(v, message[0], Color.WHITE, Color.BLACK);
            } else {
                signupUser(v, nome, email, password);
            }
        });
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            edit_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            edit_password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_eye, 0);
        } else {

            edit_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            edit_password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_eye_off, 0);
        }
        edit_password.setSelection(edit_password.getText().length());
        isPasswordVisible = !isPasswordVisible;
    }

    private void signupUser(View v, String nome, String email, String password) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        if (auth.getCurrentUser() != null) {
                            String uid = auth.getCurrentUser().getUid();
                            UserData userData = new UserData(nome, email, uid);

                            ApiService apiService = ApiRetrofit.getClient().create(ApiService.class);
                            Call<ApiResponse> call = apiService.sendUserData(userData);

                            call.enqueue(new Callback<>() {
                                @Override
                                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        // Mensagem de sucesso na API (opcional)
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                                    // Tratar falha da API (opcional)
                                }
                            });
                        }

                        mostrarSnackbar(v, message[1], Color.WHITE, Color.BLACK);

                        new Handler().postDelayed(this::ScreenLogin, 2000);
                    } else {
                        String erro = getErrorString(task);
                        mostrarSnackbar(v, erro, Color.WHITE, Color.BLACK);
                    }
                });
    }

    @NonNull
    private static String getErrorString(Task<AuthResult> task) {
        try {
            throw Objects.requireNonNull(task.getException());
        } catch (FirebaseAuthWeakPasswordException e) {
            return "Digite uma senha com no mínimo 6 caracteres.";
        } catch (FirebaseAuthUserCollisionException e) {
            return "Este email já está sendo usado.";
        } catch (FirebaseAuthInvalidCredentialsException e) {
            return "Email inválido.";
        } catch (Exception e) {
            return "Erro ao cadastrar, tente novamente mais tarde.";
        }
    }

    private void ScreenLogin() {
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void iniciarComponente() {
        singuptologin = findViewById(R.id.singuptologin);
        edit_name = findViewById(R.id.input_name);
        edit_email = findViewById(R.id.input_email);
        edit_password = findViewById(R.id.input_password);
        bt_singup = findViewById(R.id.signup_button);
    }

    private void mostrarSnackbar(View view, String mensagem, int bgColor, int textColor) {
        Snackbar snackbar = Snackbar.make(view, mensagem, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(bgColor);
        snackbar.setTextColor(textColor);
        snackbar.show();
    }
}
