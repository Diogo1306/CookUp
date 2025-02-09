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

public class SignupActivity extends AppCompatActivity {

    private EditText edit_username, edit_email, edit_password, edit_confirmPassword;
    private Button bt_signup;
    private TextView text_singuptologin;
    private AuthViewModel authViewModel;
    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        iniciarComponentes();

        NavigationUtils.setupBackButton(this, R.id.arrow_back);

        text_singuptologin.setOnClickListener(v -> startActivity(new Intent(SignupActivity.this, LoginActivity.class)));

        bt_signup.setOnClickListener(this::signUp);

        edit_password.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (edit_password.getRight() - edit_password.getCompoundDrawables()[2].getBounds().width())) {
                    togglePasswordVisibility(edit_password, true);
                    return true;
                }
            }
            return false;
        });

        edit_confirmPassword.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (edit_confirmPassword.getRight() - edit_confirmPassword.getCompoundDrawables()[2].getBounds().width())) {
                    togglePasswordVisibility(edit_confirmPassword, false);
                    return true;
                }
            }
            return false;
        });

        observeViewModel();
    }

    private void observeViewModel() {
        authViewModel.getUserLiveData().observe(this, user -> {
            if (user != null) {
                MessageUtils.showSnackbarWithDelay(
                        findViewById(android.R.id.content),
                        "Conta criada com sucesso!",
                        Color.GREEN,
                        SignupActivity.this,
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

    private void togglePasswordVisibility(EditText editText, boolean isMainPassword) {
        if (isMainPassword) {
            isPasswordVisible = !isPasswordVisible;
            editText.setInputType(isPasswordVisible ?
                    InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD :
                    (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD));
            editText.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_lock, 0,
                    isPasswordVisible ? R.drawable.ic_eye_off : R.drawable.ic_eye, 0);
        } else {
            isConfirmPasswordVisible = !isConfirmPasswordVisible;
            editText.setInputType(isConfirmPasswordVisible ?
                    InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD :
                    (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD));
            editText.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_lock, 0,
                    isConfirmPasswordVisible ? R.drawable.ic_eye_off : R.drawable.ic_eye, 0);
        }
        editText.setSelection(editText.getText().length());
    }

    private void signUp(View v) {
        String username = edit_username.getText().toString().trim();
        String email = edit_email.getText().toString().trim();
        String password = edit_password.getText().toString().trim();
        String confirmPassword = edit_confirmPassword.getText().toString().trim();

        if (username.isEmpty() ||  email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            MessageUtils.showSnackbar(v, "Preencha todos os campos.", Color.RED);
            return;
        }

        if (!password.equals(confirmPassword)) {
            MessageUtils.showSnackbar(v, "As senhas não coincidem.", Color.RED);
            return;
        }

        authViewModel.signup(email, password, username);
    }

    private void iniciarComponentes() {
        edit_username = findViewById(R.id.input_username);
        edit_email = findViewById(R.id.input_email);
        edit_password = findViewById(R.id.input_password);
        edit_confirmPassword = findViewById(R.id.input_confirm_password);
        bt_signup = findViewById(R.id.signup_button);
        text_singuptologin = findViewById(R.id.signuptologin);
    }

}