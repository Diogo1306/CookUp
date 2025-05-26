package com.diogo.cookup.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.fragment.app.Fragment;

import com.diogo.cookup.R;
import com.diogo.cookup.data.model.UserData;
import com.diogo.cookup.ui.fragment.HomeFragment;
import com.diogo.cookup.ui.fragment.ExploreFragment;
import com.diogo.cookup.ui.fragment.SavesFragment;
import com.diogo.cookup.ui.fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.diogo.cookup.viewmodel.UserViewModel;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;


public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private UserViewModel userViewModel;
    private FirebaseAuth auth;
    private SharedPreferences preferences;

    @Override
    protected void onStart() {
        super.onStart();
        checkUserAuthentication();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        int savedTheme = preferences.getInt("selected_theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        AppCompatDelegate.setDefaultNightMode(savedTheme);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        setupBottomNavigation();

        auth = FirebaseAuth.getInstance();
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        handleEmailVerification(getIntent());

        if (savedInstanceState == null) {
            openInitialFragment();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Uri data = getIntent().getData();
        if (data != null && data.toString().contains("verify")) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser user = auth.getCurrentUser();
            if (user != null) {
                user.reload().addOnCompleteListener(task -> {
                    if (user.isEmailVerified()) {
                        Toast.makeText(this, "Email verificado com sucesso!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "O email ainda não foi verificado.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    private void checkUserAuthentication() {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            navigateToLogin();
        }
    }

    private void setupBottomNavigation() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }


    public void setBottomNavVisibility(boolean visible) {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        if (visible) {
            bottomNav.setVisibility(View.VISIBLE);
        } else {
            bottomNav.setVisibility(View.GONE);
        }
    }

    private void openInitialFragment() {
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }

    private void handleEmailVerification(Intent intent) {
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

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void saveEmailToPreferences(String email) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("pending_email", email);
        editor.apply();
    }

    private String getEmailFromPreferences() {
        return preferences.getString("pending_email", "");
    }
}
