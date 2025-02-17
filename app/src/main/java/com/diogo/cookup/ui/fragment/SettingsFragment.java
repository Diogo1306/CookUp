package com.diogo.cookup.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.diogo.cookup.R;
import com.diogo.cookup.ui.activity.LoginActivity;
import com.diogo.cookup.utils.MessageUtils;
import com.diogo.cookup.viewmodel.AuthViewModel;
import com.diogo.cookup.viewmodel.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsFragment extends Fragment {

    private AuthViewModel authViewModel;
    private UserViewModel userViewModel;
    private TextView txtName, txtEmail;
    private Button btnLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        txtName = view.findViewById(R.id.settings_name);
        txtEmail = view.findViewById(R.id.settings_email);
        btnLogout = view.findViewById(R.id.btn_logout);

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

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            userViewModel.loadUser(currentUser.getUid());
        } else {
            MessageUtils.showSnackbar(view, "Usuário não logado", Color.RED);
        }

        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();

            Intent intent = new Intent(requireActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish();
        });
        return view;
    }
}
