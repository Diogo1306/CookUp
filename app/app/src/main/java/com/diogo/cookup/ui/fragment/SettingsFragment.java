package com.diogo.cookup.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.diogo.cookup.R;
import com.diogo.cookup.utils.MessageUtils;
import com.diogo.cookup.viewmodel.AuthViewModel;
import com.diogo.cookup.viewmodel.UserViewModel;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import androidx.navigation.fragment.NavHostFragment;

public class SettingsFragment extends Fragment {

    private AuthViewModel authViewModel;
    private UserViewModel userViewModel;
    private TextView txtName, txtEmail;
    private MaterialCardView btn_profile, btn_account, btn_appearance;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        setupViews(view);
        setupViewModels(view);
        loadUserData(view);
        setupListeners(view);

        return view;
    }

    private void setupViews(View view) {
        txtName = view.findViewById(R.id.settings_name);
        txtEmail = view.findViewById(R.id.settings_email);
        btn_profile = view.findViewById(R.id.button_profile);
        btn_account = view.findViewById(R.id.button_account);
        btn_appearance = view.findViewById(R.id.button_appearance);
    }

    private void setupViewModels(View view) {
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        userViewModel.getUserLiveData().observe(getViewLifecycleOwner(), userData -> {
            if (userData != null) {
                txtName.setText(userData.getUsername());
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
            MessageUtils.showSnackbar(view, getString(R.string.user_not_logged_in), Color.RED);
        }
    }

    private void setupListeners(View view) {
        view.findViewById(R.id.arrow_back).setOnClickListener(v -> {
            androidx.navigation.fragment.NavHostFragment.findNavController(this).popBackStack();
        });
        btn_profile.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_settingsFragment_to_settingsProfileFragment));
        btn_account.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_settingsFragment_to_settingsAccountFragment));
        btn_appearance.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_settingsFragment_to_settingsAppearanceFragment));
    }
}