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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginFragment extends Fragment {

    private EditText editEmail, inputPassword;
    private Button btnLogin, btnGoogleLogin;
    private TextView btnGoToSignup, forgotPassword;
    private ImageButton arrowBack;
    private boolean isPasswordVisible = false;
    private AuthViewModel authViewModel;
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;

    private final ActivityResultLauncher<IntentSenderRequest> googleLoginLauncher =
            registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), result -> {
                if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                    try {
                        SignInCredential credential = Identity.getSignInClient(requireContext())
                                .getSignInCredentialFromIntent(result.getData());
                        String idToken = credential.getGoogleIdToken();
                        if (idToken != null) {
                            firebaseAuthWithGoogle(idToken);
                        }
                    } catch (Exception e) {
                        MessageUtils.showSnackbar(requireView(), "Falha no login com Google", Color.RED);
                    }
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        oneTapClient = Identity.getSignInClient(requireContext());

        setupViews(view);
        setupObservers();
        setupListeners(view);

        return view;
    }

    private void setupViews(View view) {
        editEmail = view.findViewById(R.id.input_email);
        inputPassword = view.findViewById(R.id.input_password);
        btnLogin = view.findViewById(R.id.login_button);
        btnGoogleLogin = view.findViewById(R.id.google_Button);
        btnGoToSignup = view.findViewById(R.id.logintosingup);
        arrowBack = view.findViewById(R.id.arrow_back);
        forgotPassword = view.findViewById(R.id.forgot_password);
    }

    private void setupObservers() {
        authViewModel.getUserLiveData().observe(getViewLifecycleOwner(), firebaseUser -> {
            if (firebaseUser != null) {
                MessageUtils.showSnackbar(requireView(), "Login bem-sucedido!", Color.GREEN);
                new ViewModelProvider(this).get(UserViewModel.class).loadUser(firebaseUser.getUid());
                new Handler().postDelayed(this::navigateToMainActivity, 2000);
            }
        });

        authViewModel.getSuccessMessage().observe(getViewLifecycleOwner(), success -> {
            if (success != null) {
                MessageUtils.showSnackbar(requireView(), success, Color.GREEN);
            }
        });

        authViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                MessageUtils.showSnackbar(requireView(), error, Color.RED);
            }
        });
    }

    private void setupListeners(View view) {
        inputPassword.setOnTouchListener((v, event) -> onPasswordToggleTouch(event));
        btnLogin.setOnClickListener(this::performLogin);
        btnGoogleLogin.setOnClickListener(v -> signInWithGoogle());
        btnGoToSignup.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_signupFragment));
        arrowBack.setOnClickListener(v ->
                NavHostFragment.findNavController(this).popBackStack());
        forgotPassword.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_forgotPasswordFragment));
    }

    private boolean onPasswordToggleTouch(MotionEvent event) {
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

    private void performLogin(View view) {
        String email = editEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            MessageUtils.showSnackbar(view, "Preencha todos os campos.", Color.RED);
            return;
        }

        authViewModel.login(email, password);
    }

    private void signInWithGoogle() {
        signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(getString(R.string.default_web_client_id))
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .setAutoSelectEnabled(true)
                .build();

        oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(requireActivity(), result -> {
                    IntentSenderRequest intentSenderRequest =
                            new IntentSenderRequest.Builder(result.getPendingIntent().getIntentSender()).build();
                    googleLoginLauncher.launch(intentSenderRequest);
                })
                .addOnFailureListener(e -> {
                    MessageUtils.showSnackbar(requireView(), "Erro ao iniciar login: " + e.getMessage(), Color.RED);
                    e.printStackTrace();
                });
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        if (firebaseUser != null) {
                            authViewModel.saveGoogleUser(firebaseUser);
                        }
                    } else {
                        MessageUtils.showSnackbar(requireView(), "Falha ao autenticar com Google", Color.RED);
                    }
                });
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(requireContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }
}
