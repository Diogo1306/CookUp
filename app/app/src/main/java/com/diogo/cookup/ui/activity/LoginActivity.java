package com.diogo.cookup.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.diogo.cookup.R;
import com.diogo.cookup.utils.MessageUtils;
import com.diogo.cookup.utils.NavigationUtils;
import com.diogo.cookup.viewmodel.AuthViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private TextView text_logintosingup;
    private EditText edit_email, edit_password;
    private Button bt_login;
    private boolean isPasswordVisible = false;
    private AuthViewModel authViewModel;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        iniciarComponentes();

        NavigationUtils.setupBackButton(this, R.id.arrow_back);

        text_logintosingup.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignupActivity.class)));

        bt_login.setOnClickListener(this::login);

        edit_password.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (edit_password.getRight() - edit_password.getCompoundDrawables()[2].getBounds().width())) {
                    togglePasswordVisibility();
                    return true; // Consome o evento corretamente
                }
            }
            return false;
        });

        observeViewModel();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            ScreenMain();
        }
    }

    private void observeViewModel() {
        authViewModel.getUserLiveData().observe(this, user -> {
            if (user != null) {
                MessageUtils.showSnackbarWithDelay(
                        findViewById(android.R.id.content),
                        "Login efetuado com sucesso!",
                        Color.GREEN,
                        LoginActivity.this,
                        MainActivity.class,
                        2500
                );
            }
        });

        authViewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null) {
                MessageUtils.showSnackbar(findViewById(android.R.id.content), errorMessage, Color.RED);
            }
        });
    }

    private void login(View v) {
        String email = edit_email.getText().toString().trim();
        String password = edit_password.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            MessageUtils.showSnackbar(v, "Preencha todos os campos.", Color.RED);
            return;
        }

        authViewModel.login(email, password);
    }

    private void ScreenMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
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

    private void iniciarComponentes() {
        text_logintosingup = findViewById(R.id.logintosingup);
        edit_email = findViewById(R.id.input_email);
        edit_password = findViewById(R.id.input_password);
        bt_login = findViewById(R.id.login_button);
    }
}
