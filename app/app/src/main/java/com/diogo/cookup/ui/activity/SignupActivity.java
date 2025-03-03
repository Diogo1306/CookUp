package com.diogo.cookup.ui.activity;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.diogo.cookup.R;
import com.diogo.cookup.utils.MessageUtils;
import com.diogo.cookup.utils.NavigationUtils;
import com.diogo.cookup.viewmodel.AuthViewModel;

public class SignupActivity extends AppCompatActivity {

    private EditText editUsername, editEmail, editPassword, editConfirmPassword;
    private Button btnSignUp;
    private TextView btnGoToLogin;
    private AuthViewModel authViewModel;
    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        setupViews();
        setupListeners();
        setupObservers();
        NavigationUtils.setupBackButton(this, R.id.arrow_back);
    }

    private void setupViews() {
        editUsername = findViewById(R.id.input_username);
        editEmail = findViewById(R.id.input_email);
        editPassword = findViewById(R.id.input_password);
        editConfirmPassword = findViewById(R.id.input_confirm_password);
        btnSignUp = findViewById(R.id.signup_button);
        btnGoToLogin = findViewById(R.id.singuptologin);
    }

    private void setupListeners() {
        editPassword.setOnTouchListener((v, event) -> onPasswordToggleTouch(editPassword, event, true));
        editConfirmPassword.setOnTouchListener((v, event) -> onPasswordToggleTouch(editConfirmPassword, event, false));

        btnSignUp.setOnClickListener(this::performSignUp);
        btnGoToLogin.setOnClickListener(v -> navigateToLogin());
    }

    private void setupObservers() {
        authViewModel.getUserLiveData().observe(this, firebaseUser -> {
            if (firebaseUser != null) {
                new Handler().postDelayed(this::navigateToMainActivity, 2000);
            }
        });

        authViewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                MessageUtils.showSnackbar(findViewById(android.R.id.content), errorMessage, Color.RED);
            }
        });

        authViewModel.getSuccessMessage().observe(this, successMessage -> {
            if (successMessage != null && !successMessage.isEmpty()) {
                MessageUtils.showSnackbar(findViewById(android.R.id.content), successMessage, Color.GREEN);
            }
        });
    }

    private boolean onPasswordToggleTouch(EditText editText, MotionEvent event, boolean isMainPassword) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int drawableEndPosition = editText.getRight() - editText.getCompoundDrawables()[2].getBounds().width();
            if (event.getRawX() >= drawableEndPosition) {
                togglePasswordVisibility(editText, isMainPassword);
                return true;
            }
        }
        return false;
    }

    private void togglePasswordVisibility(EditText editText, boolean isMainPassword) {
        boolean visibilityFlag = isMainPassword ? isPasswordVisible : isConfirmPasswordVisible;

        editText.setInputType(visibilityFlag ?
                (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD) :
                (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD));

        editText.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_lock, 0,
                visibilityFlag ? R.drawable.ic_eye_off : R.drawable.ic_eye_on, 0);

        editText.setSelection(editText.getText().length());

        if (isMainPassword) {
            isPasswordVisible = !isPasswordVisible;
        } else {
            isConfirmPasswordVisible = !isConfirmPasswordVisible;
        }
    }

    private void performSignUp(View view) {
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
    }

    private void navigateToLogin() {
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        intent.putExtra("show_welcome", false);
        startActivity(intent);
        finish();
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
