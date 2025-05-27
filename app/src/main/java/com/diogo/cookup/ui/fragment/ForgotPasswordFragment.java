package com.diogo.cookup.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.diogo.cookup.R;
import com.diogo.cookup.ui.activity.LoginActivity;
import com.diogo.cookup.utils.NavigationUtils;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.List;

public class ForgotPasswordFragment extends Fragment {
    private EditText emailEditText;
    private Button resetPasswordButton;
    private TextView resetPasswordMessage;
    private FirebaseAuth firebaseAuth;
    private boolean emailSent = false;

    public ForgotPasswordFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        setupViews(view);
        setupListeners(view);

        return view;
    }

    private void setupViews(View view) {
        emailEditText = view.findViewById(R.id.emailEditText);
        resetPasswordButton = view.findViewById(R.id.resetPasswordButton);
        resetPasswordMessage = view.findViewById(R.id.resetPasswordMessage);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void setupListeners(View view) {
        resetPasswordButton.setOnClickListener(v -> {
            if (!emailSent) {
                sendResetEmail();
            } else {
                navigateToLogin();
            }
        });
        NavigationUtils.setupBackButton(this, view, R.id.arrow_back);
    }

    private void sendResetEmail() {
        String email = emailEditText.getText().toString().trim();

        resetPasswordMessage.setVisibility(View.GONE);
        resetPasswordMessage.setText("");

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError(getString(R.string.invalid_email));
            return;
        }

        firebaseAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> methods = task.getResult().getSignInMethods();

                        if (methods != null && methods.contains(GoogleAuthProvider.GOOGLE_SIGN_IN_METHOD)) {
                            showMessage("Este email está associado a uma conta Google. Deves iniciar sessão com o botão \"Entrar com Google\".");
                        } else {
                            firebaseAuth.sendPasswordResetEmail(email)
                                    .addOnCompleteListener(resetTask -> {
                                        showMessage("Verifica o teu email. Caso exista uma conta associada que não esteja ligada ao Google, enviámos-te instruções para recuperares o acesso. Segue os passos indicados na mensagem para voltares a entrar na tua conta.");
                                        if (resetTask.isSuccessful()) {
                                            changeButtonToLogin();
                                        }
                                    });
                        }
                    } else {
                        showMessage("Verifica o teu email. Caso exista uma conta associada que não esteja ligada ao Google, enviámos-te instruções para recuperares o acesso. Segue os passos indicados na mensagem para voltares a entrar na tua conta.");
                    }
                });
    }

    private void changeButtonToLogin() {
        emailSent = true;
        resetPasswordButton.setText(getString(R.string.go_to_login));
        resetPasswordButton.setBackgroundResource(R.drawable.button_primary);
        resetPasswordButton.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.poppins_semibold));
    }

    private void styleResetPasswordMessage() {
        resetPasswordMessage.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.poppins_regular));
        resetPasswordMessage.setTextColor(getResources().getColor(R.color.text_primary, requireContext().getTheme()));
        float textSizeSp = getResources().getDimension(R.dimen.text_small) / getResources().getDisplayMetrics().scaledDensity;
        resetPasswordMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeSp);
    }

    private void navigateToLogin() {
        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }


    private void showMessage(String message) {
        resetPasswordMessage.setText(message);
        styleResetPasswordMessage();
        resetPasswordMessage.setVisibility(View.VISIBLE);
    }
}