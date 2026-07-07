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

        view.findViewById(R.id.arrow_back).setOnClickListener(v -> {
            androidx.navigation.fragment.NavHostFragment.findNavController(this).popBackStack();
        });

        return view;
    }

    private void reauthenticateAndSendVerification(View view) {
        String newEmail = editTextNewEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(newEmail) || !android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
            editTextNewEmail.setError(getString(R.string.email_incorrect));            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError(getString(R.string.hintPassowrd));
            return;
        }

        user.reload().addOnCompleteListener(reloadTask -> {
            if (!reloadTask.isSuccessful()) {
                MessageUtils.showSnackbar(view, getString(R.string.error_updating_user_state));
                return;
            }

            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), password);
            user.reauthenticate(credential).addOnCompleteListener(authTask -> {
                if (authTask.isSuccessful()) {
                    user.verifyBeforeUpdateEmail(newEmail)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    MessageUtils.showSnackbar(view, getString(R.string.verification_email_sent));
                                    startVerificationCountdown();
                                } else {
                                    MessageUtils.showSnackbar(view, getString(R.string.error_sending_verification, task.getException().getMessage()));
                                }
                            });
                } else {
                    MessageUtils.showSnackbar(view, getString(R.string.password_incorrect_try_again));
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
            editTextNewEmail.setError(getString(R.string.error_invalid_email));
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError(getString(R.string.error_enter_password));
            return;
        }

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            MessageUtils.showSnackbar(view, getString(R.string.error_user_not_authenticated));
            return;
        }

        AuthCredential credential = EmailAuthProvider.getCredential(confirmedEmail, password);
        currentUser.reauthenticate(credential).addOnCompleteListener(authTask -> {
            if (!authTask.isSuccessful()) {
                MessageUtils.showSnackbar(view, getString(R.string.error_authentication_failed));
                return;
            }

            currentUser.reload().addOnCompleteListener(reloadTask -> {
                if (!reloadTask.isSuccessful()) {
                    MessageUtils.showSnackbar(view, getString(R.string.error_checking_email_state));
                    return;
                }

                if (!currentUser.isEmailVerified()) {
                    MessageUtils.showSnackbar(view, getString(R.string.error_email_not_verified));
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

                        MessageUtils.showSnackbar(view, getString(R.string.email_updated_successfully));

                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            if (isAdded()) {
                                androidx.navigation.fragment.NavHostFragment.findNavController(this).popBackStack();
                            }
                        }, 2000);
                    } else {
                        MessageUtils.showSnackbar(view, getString(R.string.error_loading_your_data));
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