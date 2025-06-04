package com.diogo.cookup.ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;

import com.diogo.cookup.R;
import com.diogo.cookup.data.model.UserData;
import com.diogo.cookup.network.ApiRetrofit;
import com.diogo.cookup.network.ApiService;
import com.diogo.cookup.viewmodel.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends BaseConnectivityActivity {

    private UserViewModel userViewModel;
    private FirebaseAuth auth;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        int savedTheme = preferences.getInt("selected_theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        AppCompatDelegate.setDefaultNightMode(savedTheme);

        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);

        View layoutNetworkError = findViewById(R.id.layout_network_error);
        View layoutServerError = findViewById(R.id.layout_server_error);
        View layoutContent = findViewById(R.id.nav_host_fragment);

        ApiService apiService = ApiRetrofit.getApiService();

        initConnectivityLayouts(layoutNetworkError, layoutServerError, layoutContent, apiService);

        View tryAgainNetwork = layoutNetworkError.findViewById(R.id.button_try_again);
        if (tryAgainNetwork != null) {
            tryAgainNetwork.setOnClickListener(v -> {
                showCheckingNetwork(layoutNetworkError);
                new Handler().postDelayed(() -> {
                    recheckConnection();
                }, 1200);
            });
        }

        View tryAgainServer = layoutServerError.findViewById(R.id.button_try_again);
        if (tryAgainServer != null) {
            tryAgainServer.setOnClickListener(v -> recheckConnection());
        }

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment == null) {
            throw new IllegalStateException("NavHostFragment não encontrado! Verifique o id/nav_host_fragment no layout.");
        }
        NavController navController = navHostFragment.getNavController();

        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        auth = FirebaseAuth.getInstance();
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        handleEmailVerification(getIntent());
    }


    public void setBottomNavVisibility(boolean visible) {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void handleEmailVerification(android.content.Intent intent) {
        if (intent != null && intent.getData() != null) {
            String email = getEmailFromPreferences();
            if (auth.isSignInWithEmailLink(intent.getData().toString())) {
                auth.signInWithEmailLink(email, intent.getData().toString())
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = auth.getCurrentUser();
                                if (user != null) {
                                    updateUserData(user, email, null, null, null);
                                }
                            } else {
                                Toast.makeText(this, "Erro ao verificar email.", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        }
    }

    public void updateUserData(FirebaseUser user, String newEmail, String newUsername, String newPassword, String newProfilePicture) {
        if (user == null) return;

        UserData updatedUserData = new UserData(
                user.getUid(),
                newUsername != null ? newUsername : user.getDisplayName(),
                newEmail != null ? newEmail : user.getEmail(),
                newProfilePicture != null ? newProfilePicture : (user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : "")
        );

        if (newEmail != null) {
            user.updateEmail(newEmail).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Email atualizado com sucesso!", Toast.LENGTH_LONG).show();
                    userViewModel.updateUser(updatedUserData);
                } else {
                    Toast.makeText(getApplicationContext(), "Erro ao atualizar email.", Toast.LENGTH_LONG).show();
                }
            });
        }

        if (newPassword != null) {
            user.updatePassword(newPassword).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Senha atualizada com sucesso!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Erro ao atualizar senha.", Toast.LENGTH_LONG).show();
                }
            });
        }

        userViewModel.updateUser(updatedUserData);
    }

    private String getEmailFromPreferences() {
        return preferences.getString("pending_email", "");
    }
}
