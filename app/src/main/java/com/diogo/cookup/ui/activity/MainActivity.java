package com.diogo.cookup.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import com.diogo.cookup.R;
import com.diogo.cookup.ui.fragment.HomeFragment;
import com.diogo.cookup.ui.fragment.SearchFragment;
import com.diogo.cookup.ui.fragment.SavesFragment;
import com.diogo.cookup.ui.fragment.ProfileFragment;
import com.diogo.cookup.utils.NavigationUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            navigateToLogin();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        int savedTheme = preferences.getInt("selected_theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        AppCompatDelegate.setDefaultNightMode(savedTheme);

        super.onCreate(savedInstanceState);

        boolean themeChanged = preferences.getBoolean("theme_changed", false);
        if (themeChanged) {
            preferences.edit().putBoolean("theme_changed", false).apply();
        }

        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        setupBottomNavigation();

        if (!themeChanged) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        }
    }



    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                NavigationUtils.openFragment(this, new HomeFragment());
                return true;
            } else if (itemId == R.id.navigation_search) {
                NavigationUtils.openFragment(this, new SearchFragment());
                return true;
            } else if (itemId == R.id.navigation_saves) {
                NavigationUtils.openFragment(this, new SavesFragment());
                return true;
            } else if (itemId == R.id.navigation_profile) {
                NavigationUtils.openFragment(this, new ProfileFragment());
                return true;
            }

            if (selectedFragment != null) {
                NavigationUtils.openFragment(this, selectedFragment);
                return true;
            }
            return false;
        });
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}