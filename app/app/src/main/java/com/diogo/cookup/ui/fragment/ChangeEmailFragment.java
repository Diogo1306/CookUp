package com.diogo.cookup.ui.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.diogo.cookup.R;
import com.diogo.cookup.data.model.UserData;
import com.diogo.cookup.utils.LiveDataUtils;
import com.diogo.cookup.utils.MessageUtils;
import com.diogo.cookup.utils.NavigationUtils;
import com.diogo.cookup.viewmodel.UserViewModel;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangeEmailFragment extends Fragment {

    private EditText editTextNewEmail, editTextPassword;
    private Button buttonSendVerification, buttonConfirm;
    private FirebaseUser user;
    private UserViewModel userViewModel;
    private boolean isPasswordVisible = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_email, container, false);

        editTextNewEmail = view.findViewById(R.id.editTextNewEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        buttonSendVerification = view.findViewById(R.id.buttonSendVerification);
        buttonConfirm = view.findViewById(R.id.buttonConfirm);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        user = FirebaseAuth.getInstance().getCurrentUser();

        setupPasswordVisibilityToggle(editTextPassword);

        buttonSendVerification.setOnClickListener(v -> reauthenticateAndSendVerification(view));
        buttonConfirm.setOnClickListener(v -> confirmEmailChange(view));

        NavigationUtils.setupBackButton(this, view, R.id.arrow_back);

        return view;
    }

    private void reauthenticateAndSendVerification(View view) {
        String newEmail = editTextNewEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(newEmail) || !android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
            editTextNewEmail.setError("Por favor, introduz um email válido.");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Introduz a tua palavra-passe.");
            return;
        }

        user.reload().addOnCompleteListener(reloadTask -> {
            if (!reloadTask.isSuccessful()) {
                MessageUtils.showSnackbar(view, "Erro ao atualizar estado do utilizador.");
                return;
            }

            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), password);
            user.reauthenticate(credential).addOnCompleteListener(authTask -> {
                if (authTask.isSuccessful()) {
                    user.verifyBeforeUpdateEmail(newEmail)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    MessageUtils.showSnackbar(view, "Foi enviado um email de verificação. Verifica a tua caixa de entrada.");
                                    startVerificationCountdown();
                                } else {
                                    MessageUtils.showSnackbar(view, "Erro ao enviar verificação: " + task.getException().getMessage());
                                }
                            });
                } else {
                    MessageUtils.showSnackbar(view, "Palavra-passe incorreta. Tenta novamente.");
                }
            });
        });
    }

    private void startVerificationCountdown() {
        buttonSendVerification.setEnabled(false);

        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                if (!isAdded()) return;
                long seconds = millisUntilFinished / 1000;
                buttonSendVerification.setText(getString(R.string.waiting_seconds, seconds));
            }

            public void onFinish() {
                if (!isAdded()) return;
                buttonSendVerification.setEnabled(true);
                buttonSendVerification.setText(getString(R.string.resend_verification));
            }
        }.start();
    }

    private void confirmEmailChange(View view) {
        String password = editTextPassword.getText().toString().trim();
        String confirmedEmail = editTextNewEmail.getText().toString().trim();

        if (TextUtils.isEmpty(confirmedEmail) || !android.util.Patterns.EMAIL_ADDRESS.matcher(confirmedEmail).matches()) {
            editTextNewEmail.setError("Por favor, introduz um email válido.");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Introduz a tua palavra-passe.");
            return;
        }

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            MessageUtils.showSnackbar(view, "Erro: utilizador não autenticado.");
            return;
        }

        AuthCredential credential = EmailAuthProvider.getCredential(confirmedEmail, password);
        currentUser.reauthenticate(credential).addOnCompleteListener(authTask -> {
            if (!authTask.isSuccessful()) {
                MessageUtils.showSnackbar(view, "A autenticação falhou. Tenta novamente.");
                return;
            }

            currentUser.reload().addOnCompleteListener(reloadTask -> {
                if (!reloadTask.isSuccessful()) {
                    MessageUtils.showSnackbar(view, "Erro ao verificar o estado do email.");
                    return;
                }

                if (!currentUser.isEmailVerified()) {
                    MessageUtils.showSnackbar(view, "Ainda não verificaste o novo email.");
                    return;
                }

                String firebaseUid = currentUser.getUid();

                userViewModel.loadUser(firebaseUid);
                LiveDataUtils.observeOnce(userViewModel.getUserLiveData(), getViewLifecycleOwner(), userData -> {
                    if (userData != null) {
                        UserData updatedData = new UserData(
                                userData.getFirebaseUid(),
                                userData.getUsername(),
                                confirmedEmail,
                                userData.getProfilePicture()
                        );

                        userViewModel.updateUser(updatedData);

                        MessageUtils.showSnackbar(view, "Email atualizado com sucesso!");

                        new Handler(Looper.getMainLooper()).postDelayed(() -> requireActivity().onBackPressed(), 2000);
                    } else {
                        MessageUtils.showSnackbar(view, "Erro ao carregar os teus dados.");
                    }
                });
            });
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