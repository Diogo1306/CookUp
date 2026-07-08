package com.diogo.cookup.ui.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.diogo.cookup.R;
import com.diogo.cookup.data.model.UserData;
import com.diogo.cookup.data.repository.UserRepository;
import com.diogo.cookup.ui.activity.AuthActivity;
import com.diogo.cookup.utils.MessageUtils;
import com.diogo.cookup.utils.SharedPrefHelper;
import com.diogo.cookup.utils.ThemeManager;
import com.diogo.cookup.viewmodel.AuthViewModel;
import com.diogo.cookup.viewmodel.UserViewModel;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseUser;

public class SettingsFragment extends Fragment {

    private AuthViewModel authViewModel;
    private UserViewModel userViewModel;
    private TextView txtName, txtEmail, txtAccountEmail;
    private TextView segThemeLight, segThemeDark, segThemeSystem;
    private MaterialCardView btn_profile, btn_account, btn_password;
    private View account_divider;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        setupViews(view);
        setupViewModels(view);
        loadUserData(view);
        setupListeners(view);
        setupLoginProvider();

        return view;
    }

    private void setupViews(View view) {
        txtName = view.findViewById(R.id.settings_name);
        txtEmail = view.findViewById(R.id.settings_email);
        txtAccountEmail = view.findViewById(R.id.settings_account_email);
        btn_profile = view.findViewById(R.id.button_profile);
        btn_account = view.findViewById(R.id.button_account);
        btn_password = view.findViewById(R.id.button_password);
        account_divider = view.findViewById(R.id.account_divider);
        segThemeLight = view.findViewById(R.id.seg_theme_light);
        segThemeDark = view.findViewById(R.id.seg_theme_dark);
        segThemeSystem = view.findViewById(R.id.seg_theme_system);
    }

    private void setupViewModels(View view) {
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        userViewModel.getUserLiveData().observe(getViewLifecycleOwner(), userData -> {
            if (userData != null) {
                txtName.setText(userData.getUsername());
                txtEmail.setText(userData.getEmail());
                if (txtAccountEmail != null) txtAccountEmail.setText(userData.getEmail());
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
        view.findViewById(R.id.arrow_back).setOnClickListener(v ->
                NavHostFragment.findNavController(this).popBackStack());

        btn_profile.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_settingsFragment_to_settingsProfileFragment));

        // Email e palavra-passe abrem DIRETAMENTE o respetivo formulario (sem ecra intermedio de Conta).
        btn_account.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_settingsFragment_to_changeEmailFragment));

        if (btn_password != null) btn_password.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_settingsFragment_to_changePasswordFragment));

        // Aparencia: escolher o tema aplica-o LOGO, aqui mesmo (sem abrir outro ecra).
        segThemeLight.setOnClickListener(v -> applyTheme(AppCompatDelegate.MODE_NIGHT_NO));
        segThemeDark.setOnClickListener(v -> applyTheme(AppCompatDelegate.MODE_NIGHT_YES));
        segThemeSystem.setOnClickListener(v -> applyTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM));
        updateThemeSelection(ThemeManager.getSavedTheme(requireContext()));

        View btnDelete = view.findViewById(R.id.button_delete_account);
        if (btnDelete != null) btnDelete.setOnClickListener(v -> showDeleteAccountDialog());

        View btnLogout = view.findViewById(R.id.button_logout);
        if (btnLogout != null) btnLogout.setOnClickListener(v -> {
            authViewModel.logout();
            Intent intent = new Intent(requireActivity(), AuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void applyTheme(int mode) {
        updateThemeSelection(mode);
        ThemeManager.setTheme(requireActivity(), mode);
    }

    // Realca o segmento do tema atual (pilula clara) e apaga o destaque dos outros.
    private void updateThemeSelection(int mode) {
        styleThemeSegment(segThemeLight, mode == AppCompatDelegate.MODE_NIGHT_NO);
        styleThemeSegment(segThemeDark, mode == AppCompatDelegate.MODE_NIGHT_YES);
        styleThemeSegment(segThemeSystem, mode == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }

    private void styleThemeSegment(TextView seg, boolean selected) {
        if (seg == null) return;
        if (selected) {
            seg.setBackgroundResource(R.drawable.bg_container);
            seg.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_primary));
            seg.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.poppins_semibold));
        } else {
            seg.setBackground(null);
            seg.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary));
            seg.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.poppins_regular));
        }
    }

    // Contas Google nao usam email/password do Firebase: esconde a palavra-passe e bloqueia a edicao de email.
    private void setupLoginProvider() {
        authViewModel.checkLoginProvider();
        authViewModel.getLoginProvider().observe(getViewLifecycleOwner(), provider -> {
            boolean isGoogle = "google.com".equals(provider);
            if (isGoogle) {
                if (btn_password != null) btn_password.setVisibility(View.GONE);
                if (account_divider != null) account_divider.setVisibility(View.GONE);
                if (btn_account != null) {
                    btn_account.setClickable(false);
                    btn_account.setFocusable(false);
                    btn_account.setForeground(null);
                }
            }
        });
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

        title.setText(getString(R.string.delete_account_title));
        message.setText(getString(R.string.delete_account_message));

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
                    deleteUserOnBackend(user.getUid());
                } else {
                    if (task.getException() instanceof FirebaseAuthRecentLoginRequiredException) {
                        MessageUtils.showSnackbar(requireView(), getString(R.string.must_relogin_to_delete));
                    } else {
                        MessageUtils.showSnackbar(requireView(), getString(R.string.firebase_delete_error, task.getException().getMessage()));
                    }
                }
            });
        }
    }

    private void deleteUserOnBackend(String firebaseUid) {
        if (userViewModel.getUserLiveData().getValue() == null) {
            MessageUtils.showSnackbar(requireView(), getString(R.string.user_data_not_found));
            return;
        }

        int userId = userViewModel.getUserLiveData().getValue().getUserId();

        userViewModel.deleteUser(userId, new UserRepository.UserCallback() {
            @Override
            public void onSuccess(UserData user, String message) {
                FirebaseAuth.getInstance().signOut();
                SharedPrefHelper.getInstance(requireContext()).clearUser();
                MessageUtils.showSnackbar(requireView(), getString(R.string.account_deleted_success));
                Intent intent = new Intent(requireActivity(), AuthActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

            @Override
            public void onError(String message) {
                FirebaseAuth.getInstance().signOut();
                SharedPrefHelper.getInstance(requireContext()).clearUser();
                MessageUtils.showSnackbar(requireView(), getString(R.string.backend_delete_error, message));
                Intent intent = new Intent(requireActivity(), AuthActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
}
