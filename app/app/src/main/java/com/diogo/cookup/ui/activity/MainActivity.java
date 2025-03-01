package com.diogo.cookup.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.diogo.cookup.R;
import com.diogo.cookup.ui.fragment.HomeFragment;
import com.diogo.cookup.ui.fragment.SearchFragment;
import com.diogo.cookup.ui.fragment.SavesFragment;
import com.diogo.cookup.ui.fragment.ProfileFragment;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        setupBottomNavigation();
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                openFragment(new HomeFragment());
                return true;
            } else if (itemId == R.id.navigation_search) {
                openFragment(new SearchFragment());
                return true;
            } else if (itemId == R.id.navigation_saves) {
                openFragment(new SavesFragment());
                return true;
            } else if (itemId == R.id.navigation_profile) {
                openFragment(new ProfileFragment());
                return true;
            }

            if (selectedFragment != null) {
                openFragment(selectedFragment);
                return true;
            }
            return false;
        });
    }

    private void openFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}