package com.diogo.cookup.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.diogo.cookup.R;
import com.diogo.cookup.viewmodel.AuthViewModel;

public class ProfileFragment extends Fragment {

    private AuthViewModel authViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        setupListeners(view);
        return view;
    }
    
    private void setupListeners(View view) {
        ImageButton btnSettings = view.findViewById(R.id.btn_settings);
        ImageButton btnEditProfile = view.findViewById(R.id.btn_editprofile);

        btnSettings.setOnClickListener(v -> openSettingsFragment());
        btnEditProfile.setOnClickListener( v ->openProfileSettingsFragment());
    }

    private void openProfileSettingsFragment() {
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_profileFragment_to_settingsProfileFragment);
    }
    private void openSettingsFragment() {
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_profileFragment_to_settingsFragment);
    }
}