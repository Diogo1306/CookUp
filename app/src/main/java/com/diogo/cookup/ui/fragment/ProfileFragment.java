package com.diogo.cookup.ui.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.diogo.cookup.R;
import com.diogo.cookup.data.model.RecipeData;
import com.diogo.cookup.data.model.UserData;
import com.diogo.cookup.ui.adapter.ProfileRecipeAdapter;
import com.diogo.cookup.utils.NumberFormatUtils;
import com.diogo.cookup.utils.SharedPrefHelper;
import com.diogo.cookup.viewmodel.ProfileViewModel;
import com.diogo.cookup.viewmodel.UserViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private ProfileRecipeAdapter adapter;
    ImageView profile_image;
    TextView user_name, user_rating, user_total_views, total_finished_my_recipes, total_recipes;
    RecyclerView UserRecipesRecyclerView;
    FloatingActionButton fab_add_recipe;
    ImageButton btnSettings, btnEditProfile;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileViewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
        UserViewModel userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        userViewModel.getUserLiveData().observe(getViewLifecycleOwner(), userData -> {
            if (userData != null) {
                user_name.setText(userData.getUsername());
                String photoUrl = userData.getProfilePicture();
                if (photoUrl != null && !photoUrl.isEmpty()) {
                    Glide.with(this)
                            .load(photoUrl)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(profile_image);
                } else {
                    profile_image.setImageResource(R.drawable.placeholder);
                }
            }
        });

        UserData user = SharedPrefHelper.getInstance(requireContext()).getUser();
        if (user != null) {
            profileViewModel.loadProfileSummary(user.getUserId());
            profileViewModel.loadProfileRecipes(user.getUserId());
        }

        setupViews(view);
        setupListeners(view);
        loadUserFromLocalStorage();
        observeViewModels();

        return view;
    }


    private void setupViews(View view) {
        btnSettings = view.findViewById(R.id.btn_settings);
        btnEditProfile = view.findViewById(R.id.btn_editprofile);
        profile_image = view.findViewById(R.id.profile_image);
        user_name = view.findViewById(R.id.user_name);
        user_rating = view.findViewById(R.id.user_rating);
        user_total_views = view.findViewById(R.id.user_total_views);
        total_finished_my_recipes = view.findViewById(R.id.total_finished_my_recipes);
        UserRecipesRecyclerView = view.findViewById(R.id.UserRecipesRecyclerView);
        fab_add_recipe = view.findViewById(R.id.fab_add_recipe);
        total_recipes = view.findViewById(R.id.total_recipes);
    }

    private void setupListeners(View view) {
        btnSettings.setOnClickListener(v -> openSettingsFragment());
        btnEditProfile.setOnClickListener(v -> openProfileSettingsFragment());
        fab_add_recipe.setOnClickListener(v -> openSaveRecipesFragment());

        UserRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ProfileRecipeAdapter(
                getContext(),
                new ArrayList<>(),
                new ProfileRecipeAdapter.OnRecipeActionListener() {
                    @Override
                    public void onEdit(RecipeData recipe) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("recipe_id", recipe.getRecipeId());
                        NavHostFragment.findNavController(ProfileFragment.this)
                                .navigate(R.id.action_profileFragment_to_recipeSaveOrUpdateFragment, bundle);
                    }
                    @Override
                    public void onDelete(RecipeData recipe) {
                        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_confirm_delete, null);

                        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                                .setView(dialogView)
                                .create();

                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                        TextView title = dialogView.findViewById(R.id.dialog_title);
                        TextView message = dialogView.findViewById(R.id.dialog_message);
                        TextView cancel = dialogView.findViewById(R.id.button_cancel);
                        TextView delete = dialogView.findViewById(R.id.button_delete);

                        title.setText("Excluir Receita");
                        message.setText("Tem certeza que deseja excluir esta receita?");

                        cancel.setOnClickListener(v1 -> dialog.dismiss());

                        delete.setOnClickListener(v1 -> {
                            profileViewModel.deleteRecipe(recipe.getRecipeId());
                            dialog.dismiss();
                        });

                        dialog.show();

                    }
                    @Override
                    public void onRecipeClick(RecipeData recipe) {
                        Bundle args = new Bundle();
                        args.putInt("recipe_id", recipe.getRecipeId());
                        NavHostFragment.findNavController(ProfileFragment.this)
                                .navigate(R.id.action_profileFragment_to_recipeDetailFragment, args);
                    }
                }
        );
        UserRecipesRecyclerView.setAdapter(adapter);
    }

    private void loadUserFromLocalStorage() {
        UserData user = SharedPrefHelper.getInstance(requireContext()).getUser();
        if (user != null) {
            user_name.setText(user.getUsername());
            Log.d("ProfileFragment", "Nome: " + user.getUsername() + " | Foto: " + user.getProfilePicture());
            String photoUrl = user.getProfilePicture();
            if (photoUrl != null && !photoUrl.isEmpty()) {
                Glide.with(this)
                        .load(photoUrl)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(profile_image);
            } else {
                profile_image.setImageResource(R.drawable.placeholder);
            }
        }
    }


    private void observeViewModels() {

        profileViewModel.getProfileSummary().observe(getViewLifecycleOwner(), profileData -> {
            if (profileData != null) {
                user_rating.setText(String.valueOf(profileData.getAverageRating()));
                user_total_views.setText(NumberFormatUtils.formatCompact(profileData.getTotalViews()));
                total_finished_my_recipes.setText(NumberFormatUtils.formatCompact(profileData.getFinishedCount()));
                TextView totalRecipesTextView = requireView().findViewById(R.id.total_recipes);
                totalRecipesTextView.setText(NumberFormatUtils.formatCompact(profileData.getTotalRecipes()));
            }
        });

        profileViewModel.getUserRecipes().observe(getViewLifecycleOwner(), recipes -> {
            if (recipes != null) {
                adapter.setRecipeList(recipes);
            }
        });

        profileViewModel.getDeleteSuccess().observe(getViewLifecycleOwner(), success -> {
            if (Boolean.TRUE.equals(success)) {
                Toast.makeText(requireContext(), "Receita excluída com sucesso", Toast.LENGTH_SHORT).show();
                UserData user = SharedPrefHelper.getInstance(requireContext()).getUser();
                if (user != null) {
                    profileViewModel.loadProfileRecipes(user.getUserId());
                    profileViewModel.loadProfileSummary(user.getUserId());
                }
            }
        });
        profileViewModel.getDeleteError().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show();
            }
        });

    }

    private void openSaveRecipesFragment() {
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_profileFragment_to_saveRecipesFragment);
    }

    private void openProfileSettingsFragment() {
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_profileFragment_to_settingsProfileFragment);
    }

    private void openSettingsFragment() {
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_profileFragment_to_settingsFragment);
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d("ProfileFragment", "onResume chamado");

        FirebaseUser currentUser = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            UserViewModel userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
            userViewModel.loadUser(currentUser.getUid());
        }
        loadUserFromLocalStorage();
    }

}