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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText editEmail, editPassword;
    private Button btnLogin;
    private TextView btnGoToSignup;
    private AuthViewModel authViewModel;

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        setupViews();

        authViewModel.getUserLiveData().observe(this, firebaseUser -> {
            if (firebaseUser != null) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        authViewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                MessageUtils.showSnackbar(findViewById(android.R.id.content), error, Color.RED);
            }
        });

        btnLogin.setOnClickListener(this::Login);

        btnGoToSignup.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        });
    }

    private void Login(View view) {
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            MessageUtils.showSnackbar(view, "Preencha todos os campos.", Color.RED);
            return;
        }

        authViewModel.login(email, password);
    }

    private void setupViews() {
        editEmail = findViewById(R.id.input_email);
        editPassword = findViewById(R.id.input_password);
        btnLogin = findViewById(R.id.login_button);
        btnGoToSignup = findViewById(R.id.logintosingup);
    }
}