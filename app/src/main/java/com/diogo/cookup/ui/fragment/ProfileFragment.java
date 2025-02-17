package com.diogo.cookup.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.diogo.cookup.R;
import com.diogo.cookup.ui.activity.LoginActivity;
import com.diogo.cookup.viewmodel.AuthViewModel;

public class ProfileFragment extends Fragment {

    private AuthViewModel authViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        Button btnLogout = view.findViewById(R.id.btn_logout);

        btnLogout.setOnClickListener(v -> {
            Fragment secondFragment = new SettingsFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, secondFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }

    private void logoutUser() {
        authViewModel.logout();

        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            getActivity().finish();
        }

    }
}