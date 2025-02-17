package com.diogo.cookup.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.diogo.cookup.ui.fragment.HomeFragment;
import com.diogo.cookup.ui.fragment.SearchFragment;
import com.diogo.cookup.ui.fragment.SavesFragment;
import com.diogo.cookup.ui.fragment.ProfileFragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.diogo.cookup.R;
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
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
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
            return false;
        });
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }

    private void openFragment (Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
