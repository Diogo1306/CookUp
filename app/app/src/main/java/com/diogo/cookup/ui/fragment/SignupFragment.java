package com.diogo.cookup.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.diogo.cookup.R;
import com.diogo.cookup.ui.activity.MainActivity;
import com.diogo.cookup.utils.MessageUtils;
import com.diogo.cookup.viewmodel.AuthViewModel;
import com.diogo.cookup.viewmodel.UserViewModel;

import java.nio.InvalidMarkException;

public class SignupFragment extends Fragment {

    private EditText editUsername, editEmail, editPassword, editConfirmPassword;
    private Button btnSignUp;

    private ImageButton arrowBack;
    private TextView btnGoToLogin;
    private AuthViewModel authViewModel;
    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        setupViews(view);
        setupListeners();
        setupObservers();

        return view;
    }

    private void setupViews(View view) {
        editUsername = view.findViewById(R.id.input_username);
        editEmail = view.findViewById(R.id.input_email);
        editPassword = view.findViewById(R.id.input_password);
        editConfirmPassword = view.findViewById(R.id.input_confirm_password);
        btnSignUp = view.findViewById(R.id.signup_button);
        btnGoToLogin = view.findViewById(R.id.singuptologin);
        arrowBack = view.findViewById(R.id.arrow_back);

    }

    private void setupListeners() {
        editPassword.setOnTouchListener((v, event) -> onPasswordToggleTouch(editPassword, event, true));
        editConfirmPassword.setOnTouchListener((v, event) -> onPasswordToggleTouch(editConfirmPassword, event, false));
        arrowBack.setOnClickListener(v ->
                NavHostFragment.findNavController(this).popBackStack());        btnSignUp.setOnClickListener(this::performSignUp);
        btnGoToLogin.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.action_signupFragment_to_loginFragment));
    }

    private void setupObservers() {
        authViewModel.getUserLiveData().observe(getViewLifecycleOwner(), firebaseUser -> {
            if (firebaseUser != null) {
                new ViewModelProvider(this).get(UserViewModel.class).loadUser(firebaseUser.getUid());
                new Handler().postDelayed(this::navigateToMainActivity, 2000);
            }
        });

        authViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                MessageUtils.showSnackbar(requireView(), errorMessage, Color.RED);
            }
        });

        authViewModel.getSuccessMessage().observe(getViewLifecycleOwner(), successMessage -> {
            if (successMessage != null && !successMessage.isEmpty()) {
                MessageUtils.showSnackbar(requireView(), successMessage, Color.GREEN);
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
            MessageUtils.showSnackbar(view, getString(R.string.fill_all_fields), Color.RED);
            return;
        }

        if (!password.equals(confirmPassword)) {
            MessageUtils.showSnackbar(view, getString(R.string.passwords_do_not_match), Color.RED);
            return;
        }

        authViewModel.signup(email, password, username);
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(requireContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }
}
