package com.diogo.cookup.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.diogo.cookup.R;
import com.diogo.cookup.utils.MessageUtils;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordFragment extends Fragment {
    private EditText currentPasswordEditText, newPasswordEditText, confirmNewPasswordEditText;
    private Button changePasswordButton;
    private FirebaseAuth firebaseAuth;
    private boolean isPasswordVisible = false;

    public ChangePasswordFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        setupViews(view);
        setupListeners(view);

        return view;
    }

    private void setupViews(View view) {
        currentPasswordEditText = view.findViewById(R.id.currentPasswordEditText);
        newPasswordEditText = view.findViewById(R.id.newPasswordEditText);
        confirmNewPasswordEditText = view.findViewById(R.id.confirmNewPasswordEditText);
        changePasswordButton = view.findViewById(R.id.changePasswordButton);
        firebaseAuth = FirebaseAuth.getInstance();

        setupPasswordVisibilityToggle(currentPasswordEditText);
        setupPasswordVisibilityToggle(newPasswordEditText);
        setupPasswordVisibilityToggle(confirmNewPasswordEditText);
    }

    private void setupListeners(View view) {
        changePasswordButton.setOnClickListener(v -> changePassword(view));
        view.findViewById(R.id.arrow_back).setOnClickListener(v -> {
            androidx.navigation.fragment.NavHostFragment.findNavController(this).popBackStack();
        });
    }

    private void changePassword(View view) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
            MessageUtils.showSnackbar(view, "Erro: Utilizador não autenticado.", Color.RED);
            return;
        }

        String currentPassword = currentPasswordEditText.getText().toString().trim();
        String newPassword = newPasswordEditText.getText().toString().trim();
        String confirmNewPassword = confirmNewPasswordEditText.getText().toString().trim();

        if (!validateInputs(currentPassword, newPassword, confirmNewPassword, view)) {
            return;
        }

        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);
        user.reauthenticate(credential).addOnCompleteListener(authTask -> {
            if (authTask.isSuccessful()) {
                updatePassword(user, newPassword, view);
            } else {
                MessageUtils.showSnackbar(view, "Palavra-passe atual incorreta.", Color.RED);
            }
        });
    }

    private boolean validateInputs(String currentPassword, String newPassword, String confirmNewPassword, View view) {
        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
            MessageUtils.showSnackbar(view, "Preencha todos os campos.", Color.RED);
            return false;
        }

        if (!newPassword.equals(confirmNewPassword)) {
            MessageUtils.showSnackbar(view, "As novas palavras-passe não coincidem.", Color.RED);
            return false;
        }

        if (newPassword.length() < 6) {
            MessageUtils.showSnackbar(view, "A nova palavra-passe deve ter pelo menos 6 caracteres.", Color.RED);
            return false;
        }

        return true;
    }

    private void updatePassword(FirebaseUser user, String newPassword, View view) {
        user.updatePassword(newPassword).addOnCompleteListener(updateTask -> {
            if (updateTask.isSuccessful()) {
                MessageUtils.showSnackbar(view, "Palavra-passe alterada com sucesso!", Color.GREEN);

                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    requireActivity().runOnUiThread(() ->
                            androidx.navigation.fragment.NavHostFragment.findNavController(this).popBackStack()
                    );
                }, 2000);
            } else {
                MessageUtils.showSnackbar(view, "Erro ao atualizar palavra-passe: " + updateTask.getException().getMessage(), Color.RED);
            }
        });
    }

    private void setupPasswordVisibilityToggle(EditText passwordEditText) {
        passwordEditText.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (passwordEditText.getRight() - passwordEditText.getCompoundDrawables()[2].getBounds().width())) {
                    togglePasswordVisibility(passwordEditText);

                    return true;
                }
            }
            return false;
        });
    }

    private void togglePasswordVisibility(EditText passwordEditText) {
        if (isPasswordVisible) {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_eye_off, 0);
        } else {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_eye_on, 0);
        }

        passwordEditText.setSelection(passwordEditText.getText().length());

        vibrate();

        isPasswordVisible = !isPasswordVisible;
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) requireContext().getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(50);
            }
        }
    }
}