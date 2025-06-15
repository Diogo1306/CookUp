package com.diogo.cookup.ui.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.diogo.cookup.R;
import com.diogo.cookup.ui.activity.AuthActivity;
import com.diogo.cookup.utils.MessageUtils;
import com.diogo.cookup.utils.SharedPrefHelper;
import com.diogo.cookup.viewmodel.AuthViewModel;
import com.diogo.cookup.viewmodel.UserViewModel;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
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
        setupListeners(view);
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

    private void setupListeners(View view) {
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

        btn_deleteAccount.setOnClickListener(v -> showDeleteAccountDialog());
    }
    
    private void showDeleteAccountDialog() {
        View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_confirm_delete, null);

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        TextView cancelButton = dialogView.findViewById(R.id.button_cancel);
        TextView deleteButton = dialogView.findViewById(R.id.button_delete);
        TextView title = dialogView.findViewById(R.id.dialog_title);
        TextView message = dialogView.findViewById(R.id.dialog_message);

        title.setText("Eliminar conta");
        message.setText("Tem certeza que deseja eliminar a sua conta? Esta ação é irreversível.");

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        deleteButton.setOnClickListener(v -> {
            dialog.dismiss();
            deleteFromFirebaseAndBackend();
        });

        dialog.show();
    }

    private void deleteFromFirebaseAndBackend() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Firebase Auth deleted, now delete in backend
                    deleteUserOnBackend();
                } else {
                    if (task.getException() instanceof FirebaseAuthRecentLoginRequiredException) {
                        MessageUtils.showSnackbar(requireView(), "É necessário relogar para eliminar a conta.");
                        // Aqui pode mostrar tela para reautenticar, se quiser.
                    } else {
                        MessageUtils.showSnackbar(requireView(), "Erro ao eliminar do Firebase: " + task.getException().getMessage());
                    }
                }
            });
        }
    }

    private void deleteUserOnBackend() {
        if (userViewModel.getUserLiveData().getValue() == null) {
            MessageUtils.showSnackbar(requireView(), "Erro: dados do usuário não encontrados.");
            return;
        }
        int userId = userViewModel.getUserLiveData().getValue().getUserId();
        userViewModel.deleteUser(userId, new com.diogo.cookup.data.repository.UserRepository.UserCallback() {
            @Override
            public void onSuccess(com.diogo.cookup.data.model.UserData user, String message) {
                FirebaseAuth.getInstance().signOut();
                SharedPrefHelper.getInstance(requireContext()).clearUser();
                MessageUtils.showSnackbar(requireView(), "Conta eliminada com sucesso!");
                Intent intent = new Intent(requireActivity(), AuthActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

            @Override
            public void onError(String message) {
                MessageUtils.showSnackbar(requireView(), "Erro ao eliminar no backend: " + message);
            }
        });
    }
}
