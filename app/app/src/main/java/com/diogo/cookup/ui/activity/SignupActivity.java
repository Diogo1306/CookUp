package com.diogo.cookup.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.diogo.cookup.R;
import com.diogo.cookup.utils.MessageUtils;
import com.diogo.cookup.viewmodel.AuthViewModel;

public class SignupActivity extends AppCompatActivity {

    private EditText editUsername, editEmail, editPassword, editConfirmPassword;
    private Button btnSignUp;
    private TextView btnGoToLogin;  // Verificar se no XML esse ID está correto

    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        setupViews();
        setupObservers();
        setupListeners();
    }

    private void setupViews() {
        editUsername = findViewById(R.id.input_username);
        editEmail = findViewById(R.id.input_email);
        editPassword = findViewById(R.id.input_password);
        editConfirmPassword = findViewById(R.id.input_confirm_password);
        btnSignUp = findViewById(R.id.signup_button);
        btnGoToLogin = findViewById(R.id.singuptologin);
    }

    private void setupObservers() {
        authViewModel.getUserLiveData().observe(this, firebaseUser -> {
            if (firebaseUser != null) {
                startActivity(new Intent(SignupActivity.this, MainActivity.class));
                finish();
            }
        });

        authViewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                MessageUtils.showSnackbar(findViewById(android.R.id.content), error, Color.RED);
            }
        });
    }

    private void setupListeners() {
        btnSignUp.setOnClickListener(this::Signup);

        btnGoToLogin.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void Signup(View view) {
        String username = editUsername.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String confirmPassword = editConfirmPassword.getText().toString().trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            MessageUtils.showSnackbar(view, "Preencha todos os campos.", Color.RED);
            return;
        }
        if (!password.equals(confirmPassword)) {
            MessageUtils.showSnackbar(view, "As senhas não coincidem.", Color.RED);
            return;
        }

        authViewModel.signup(email, password, username);

        authViewModel.getUserLiveData().observe(this, firebaseUser -> {
            if (firebaseUser != null) {
                startActivity(new Intent(SignupActivity.this, MainActivity.class));
                finish();
            }
        });
    }

}
