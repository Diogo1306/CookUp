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
import com.diogo.cookup.utils.NavigationUtils;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private TextView text_logintosingup;
    private EditText edit_email, edit_password;
    private Button bt_login;
    private boolean isPasswordVisible = false;

    private final String[] message = {"Preencha todos os campos.", "Login efetuado com sucesso"};

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        iniciarComponentes();

        NavigationUtils.setupBackButton(this, R.id.arrow_back);

        text_logintosingup.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        bt_login.setOnClickListener(v -> {
            String email = edit_email.getText().toString().trim();
            String password = edit_password.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Snackbar snackbar = Snackbar.make(v, message[0], Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            } else {
                authUser(v);
            }
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

    private void authUser(View view) {
        String email = edit_email.getText().toString().trim();
        String password = edit_password.getText().toString().trim();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Snackbar snackbar = Snackbar.make(view, message[1], Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.GREEN);
                        snackbar.setTextColor(Color.WHITE);
                        snackbar.show();

                        new Handler().postDelayed(this::ScreenMain, 2000);
                    } else {
                        String erro = getErrorMessage(task);

                        Snackbar snackbar = Snackbar.make(view, erro, Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.RED);
                        snackbar.setTextColor(Color.WHITE);
                        snackbar.show();
                    }
                });
    }

    @NonNull
    private static String getErrorMessage(Task<AuthResult> task) {
        String erro;
        try {
            throw Objects.requireNonNull(task.getException());
        } catch (FirebaseAuthInvalidUserException e) {
            erro = "Utilizador não encontrado.";
        } catch (FirebaseAuthInvalidCredentialsException e) {
            erro = "Credenciais inválidas. Verifique o email e a palavra-passe.";
        } catch (Exception e) {
            erro = "Erro ao fazer login. Tente novamente.";
        }
        return erro;
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            ScreenMain();
        }
    }

    private void ScreenMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void iniciarComponentes() {
        text_logintosingup = findViewById(R.id.logintosingup);
        edit_email = findViewById(R.id.input_email);
        edit_password = findViewById(R.id.input_password);
        bt_login = findViewById(R.id.login_button);
    }
}
