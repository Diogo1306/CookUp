package com.diogo.cookup.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.diogo.cookup.R;
import com.diogo.cookup.ui.activity.AuthActivity;
import com.diogo.cookup.utils.MessageUtils;
import com.diogo.cookup.viewmodel.AuthViewModel;
import com.diogo.cookup.viewmodel.UserViewModel;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsAccountFragment extends Fragment {

    private AuthViewModel authViewModel;
    private UserViewModel userViewModel;
    private TextView txtEmail;
    private MaterialCardView btn_Email, btn_password, btn_logout, btn_deleteAccount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings_account, container, false);

        setupViews(view);
        setupViewModels(view);
        loadUserData(view);
        setupLisners(view);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        authViewModel.checkLoginProvider();


        authViewModel.getLoginProvider().observe(getViewLifecycleOwner(), provider -> {
            boolean isGoogle = "google.com".equals(provider);

            if (isGoogle) {
                btn_password.setVisibility(View.GONE);

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    txtEmail.setText(currentUser.getEmail());
                }

                btn_Email.setClickable(false);
                btn_Email.setFocusable(false);
                btn_Email.setForeground(null);

                ImageView accountIcon = requireView().findViewById(R.id.account_icon);
                accountIcon.setImageResource(R.drawable.ic_google);
                accountIcon.setContentDescription("Login com Google");
            }
        });

        return view;
    }

    private void setupViews(View view) {
        txtEmail = view.findViewById(R.id.account_email);
        btn_Email = view.findViewById(R.id.button_email);
        btn_password = view.findViewById(R.id.button_password);
        btn_logout = view.findViewById(R.id.button_logout);
        btn_deleteAccount = view.findViewById(R.id.button_delete);
    }

    private void setupViewModels(View view) {
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        userViewModel.getUserLiveData().observe(getViewLifecycleOwner(), userData -> {
            if (userData != null) {
                txtEmail.setText(userData.getEmail());
            }
        });

        userViewModel.getErrorMessageLiveData().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                MessageUtils.showSnackbar(view, error, Color.RED);
            }
        });
    }

    private void loadUserData(View view) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            userViewModel.loadUser(currentUser.getUid());
        } else {
            MessageUtils.showSnackbar(requireView(), "Usuário não logado", Color.RED);
        }
    }

    private void setupLisners(View view)  {
        view.findViewById(R.id.arrow_back).setOnClickListener(v ->
                NavHostFragment.findNavController(this).popBackStack()
        );
        btn_Email.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_settingsAccountFragment_to_changeEmailFragment)
        );
        btn_password.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_settingsAccountFragment_to_changePasswordFragment)
        );
        btn_logout.setOnClickListener(v -> {
            authViewModel.logout();

            Intent intent = new Intent(requireActivity(), AuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

    }
}