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
import com.diogo.cookup.ui.fragment.WelcomeFragment;
import com.diogo.cookup.utils.MessageUtils;
import com.diogo.cookup.utils.NavigationUtils;
import com.diogo.cookup.viewmodel.AuthViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText editEmail, inputPassword;
    private Button btnLogin;
    private TextView btnGoToSignup;
    private ImageButton arrowBack;
    private boolean isPasswordVisible = false;
    private AuthViewModel authViewModel;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            navigateToMainActivity();
        } else if (getIntent().getBooleanExtra("show_welcome", true)) {
            showWelcomeFragment();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        NavigationUtils.setupBackButton(this, R.id.arrow_back);

        setupViews();
        setupObservers();
        setupListeners();
    }

    private void setupViews() {
        editEmail = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_password);
        btnLogin = findViewById(R.id.login_button);
        btnGoToSignup = findViewById(R.id.logintosingup);
        arrowBack = findViewById(R.id.arrow_back);
    }

    private void setupObservers() {
        authViewModel.getUserLiveData().observe(this, firebaseUser -> {
            if (firebaseUser != null) {
                navigateToMainActivity();
            }
        });

        authViewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                MessageUtils.showSnackbar(findViewById(android.R.id.content), error, Color.RED);
            }
        });
    }

    private void setupListeners() {
        inputPassword.setOnTouchListener(this::onPasswordToggleTouch);
        btnLogin.setOnClickListener(this::Login);
        btnGoToSignup.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignupActivity.class)));

        arrowBack.setOnClickListener(v -> showWelcomeFragment());
    }

    private boolean onPasswordToggleTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int drawableEndPosition = inputPassword.getRight() - inputPassword.getPaddingEnd();
            if (event.getRawX() >= drawableEndPosition) {
                togglePasswordVisibility();
                return true;
            }
        }
        return false;
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            inputPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_eye_off, 0);
        } else {
            inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            inputPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_eye_on, 0);
        }
        inputPassword.setSelection(inputPassword.getText().length());
        isPasswordVisible = !isPasswordVisible;
    }

    private void Login(View view) {
        String email = editEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            MessageUtils.showSnackbar(view, "Preencha todos os campos.", Color.RED);
            return;
        }

        authViewModel.login(email, password);
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void showWelcomeFragment() {
        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new WelcomeFragment())
                    .commit();
        }
    }
}
