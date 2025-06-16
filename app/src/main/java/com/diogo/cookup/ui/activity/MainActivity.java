package com.diogo.cookup.ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;

import com.diogo.cookup.R;
import com.diogo.cookup.data.model.UserData;
import com.diogo.cookup.network.ApiRetrofit;
import com.diogo.cookup.network.ApiService;
import com.diogo.cookup.utils.NoShowBottomNavigationBehavior;
import com.diogo.cookup.viewmodel.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseConnectivityActivity {

    private UserViewModel userViewModel;
    private FirebaseAuth auth;
    private SharedPreferences preferences;
    private final Map<String, NavHostFragment> navHostFragments = new HashMap<>();
    private String currentTag = null;
    private static final String TAG_HOME = "home";
    private static final String TAG_EXPLORE = "explore";
    private static final String TAG_SAVES = "saves";
    private static final String TAG_PROFILE = "profile";

    @Override
    public void onBackPressed() {
        NavHostFragment currentNavHost = (NavHostFragment) getSupportFragmentManager().findFragmentByTag(currentTag);
        if (currentNavHost != null) {
            NavController navController = currentNavHost.getNavController();
            if (!navController.popBackStack()) {
                if (!TAG_HOME.equals(currentTag)) {
                    switchToFragment(TAG_HOME);
                    BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
                    bottomNav.setSelectedItemId(R.id.navigation_home);
                } else {
                    super.onBackPressed();
                }
            }
        } else {
            super.onBackPressed();
        }
    }

    private final NavController.OnDestinationChangedListener bottomNavListener = (controller, destination, arguments) -> {
        if (destination.getId() == R.id.recipeDetailFragment) {
            setBottomNavigationVisible(false);
        } else {
            setBottomNavigationVisible(true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        com.diogo.cookup.utils.ThemeManager.applySavedTheme(this);

        if (savedInstanceState != null) {
            currentTag = savedInstanceState.getString("current_nav_tag", TAG_HOME);
        } else {
            currentTag = TAG_HOME;
        }

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);

        View layoutNetworkError = findViewById(R.id.layout_network_error);
        View layoutServerError = findViewById(R.id.layout_server_error);
        View layoutContent = findViewById(R.id.nav_host_container);

        ApiService apiService = ApiRetrofit.getApiService();
        initConnectivityLayouts(layoutNetworkError, layoutServerError, layoutContent, apiService);

        View tryAgainNetwork = layoutNetworkError.findViewById(R.id.button_try_again);
        if (tryAgainNetwork != null) {
            tryAgainNetwork.setOnClickListener(v -> {
                showCheckingNetwork(layoutNetworkError);
                new Handler().postDelayed(this::recheckConnection, 1200);
            });
        }

        View tryAgainServer = layoutServerError.findViewById(R.id.button_try_again);
        if (tryAgainServer != null) {
            tryAgainServer.setOnClickListener(v -> recheckConnection());
        }

        setupBottomNavigation();

        auth = FirebaseAuth.getInstance();
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        handleEmailVerification(getIntent());
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        Map<String, Integer> graphMap = new HashMap<>();
        graphMap.put(TAG_HOME, R.navigation.nav_graph_home);
        graphMap.put(TAG_EXPLORE, R.navigation.nav_graph_explore);
        graphMap.put(TAG_SAVES, R.navigation.nav_graph_saves);
        graphMap.put(TAG_PROFILE, R.navigation.nav_graph_profile);

        FragmentManager fragmentManager = getSupportFragmentManager();

        for (Map.Entry<String, Integer> entry : graphMap.entrySet()) {
            NavHostFragment navHostFragment = (NavHostFragment) fragmentManager.findFragmentByTag(entry.getKey());
            if (navHostFragment == null) {
                navHostFragment = NavHostFragment.create(entry.getValue());
                fragmentManager.beginTransaction()
                        .add(R.id.nav_host_container, navHostFragment, entry.getKey())
                        .hide(navHostFragment)
                        .commitNow();
            }
            navHostFragments.put(entry.getKey(), navHostFragment);
        }

        switchToFragment(currentTag);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                switchToFragment(TAG_HOME);
                return true;
            } else if (itemId == R.id.navigation_explore) {
                switchToFragment(TAG_EXPLORE);
                return true;
            } else if (itemId == R.id.navigation_saves) {
                switchToFragment(TAG_SAVES);
                return true;
            } else if (itemId == R.id.navigation_profile) {
                switchToFragment(TAG_PROFILE);
                return true;
            }
            return false;
        });
    }

    private void checkAndApplyBottomNavVisibility() {
        NavHostFragment currentHost = navHostFragments.get(currentTag);
        if (currentHost == null) return;

        Fragment currentFragment = currentHost.getChildFragmentManager().getPrimaryNavigationFragment();
        if (currentFragment != null && currentFragment.getClass().getSimpleName().equals("RecipeDetailFragment")) {
            setBottomNavigationVisible(false);
        } else {
            setBottomNavigationVisible(true);
        }
    }

    private void switchToFragment(String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        for (Map.Entry<String, NavHostFragment> entry : navHostFragments.entrySet()) {
            fragmentManager.beginTransaction()
                    .hide(entry.getValue())
                    .commitNow();
        }

        NavHostFragment selectedNavHost = navHostFragments.get(tag);
        if (selectedNavHost != null) {
            fragmentManager.beginTransaction()
                    .show(selectedNavHost)
                    .commitNow();
            currentTag = tag;

            NavController navController = selectedNavHost.getNavController();
            navController.removeOnDestinationChangedListener(bottomNavListener);
            navController.addOnDestinationChangedListener(bottomNavListener);
        }
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

    public void setBottomNavigationVisible(boolean visible) {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        if (bottomNav != null) {
            if (visible) {
                NoShowBottomNavigationBehavior.showAgain();
                bottomNav.setVisibility(View.VISIBLE);

                bottomNav.post(() -> {
                    bottomNav.requestLayout();
                    bottomNav.invalidate();
                });

            } else {
                NoShowBottomNavigationBehavior.hideForever();
                bottomNav.setVisibility(View.GONE);
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

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("current_nav_tag", currentTag);
    }
}
