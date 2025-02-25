package com.diogo.cookup.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.diogo.cookup.R;
import com.diogo.cookup.utils.MessageUtils;
import com.diogo.cookup.utils.NavigationUtils;
import com.diogo.cookup.viewmodel.AuthViewModel;

public class SignupActivity extends AppCompatActivity {

    private EditText editUsername, editEmail, editPassword, editConfirmPassword;
    private Button btnSignUp;
    private ImageButton arrow_back;
    private TextView btnGoToLogin;
    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        setupViews();
        setupObservers();
        setupListeners();

        NavigationUtils.setupBackButton(this, R.id.arrow_back);

    }

    private void setupViews() {
        editUsername = findViewById(R.id.input_username);
        editEmail = findViewById(R.id.input_email);
        editPassword = findViewById(R.id.input_password);
        editConfirmPassword = findViewById(R.id.input_confirm_password);
        btnSignUp = findViewById(R.id.signup_button);
        btnGoToLogin = findViewById(R.id.singuptologin);
        arrow_back = findViewById(R.id.arrow_back);
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
        editPassword.setOnTouchListener((v, event) -> onPasswordToggleTouch(editPassword, event));
        editConfirmPassword.setOnTouchListener((v, event) -> onPasswordToggleTouch(editConfirmPassword, event));
        btnSignUp.setOnClickListener(this::Signup);
        btnGoToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            intent.putExtra("show_welcome", false);
            startActivity(intent);
            finish();
        });
    }

    private boolean onPasswordToggleTouch(EditText editText, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int drawableEndPosition = editText.getRight() - editText.getCompoundDrawables()[2].getBounds().width();
            if (event.getRawX() >= drawableEndPosition) {
                togglePasswordVisibility(editText);
                return true;
            }
        }
        return false;
    }

    private void togglePasswordVisibility(EditText editText) {
        if (editText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_eye_on, 0);
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_eye_off, 0);
        }
        editText.setSelection(editText.getText().length());
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
