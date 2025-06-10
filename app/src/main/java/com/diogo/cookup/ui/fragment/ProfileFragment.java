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
import com.diogo.cookup.utils.SharedPrefHelper;
import com.diogo.cookup.viewmodel.ProfileViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
                        // CORRIGIDO: Navega para a tela de edição passando o ID da receita
                        Bundle bundle = new Bundle();
                        bundle.putInt("recipe_id", recipe.getRecipeId());
                        NavHostFragment.findNavController(ProfileFragment.this)
                                .navigate(R.id.action_profileFragment_to_recipeSaveOrUpdateFragment, bundle);
                    }
                    @Override
                    public void onDelete(RecipeData recipe) {
                        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                                .setTitle("Excluir Receita")
                                .setMessage("Tem certeza que deseja excluir esta receita?")
                                .setPositiveButton("Sim", (dialog, which) -> {
                                    profileViewModel.deleteRecipe(recipe.getRecipeId());
                                })
                                .setNegativeButton("Cancelar", null)
                                .show();
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

            String photoUrl = user.getProfilePicture();
            Log.d("UserData", "URL da imagem: " + user.getProfilePicture());
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
                user_total_views.setText(String.valueOf(profileData.getTotalViews()));
                total_finished_my_recipes.setText(String.valueOf(profileData.getFinishedCount()));
                TextView totalRecipesTextView = requireView().findViewById(R.id.total_recipes);
                totalRecipesTextView.setText(String.valueOf(profileData.getTotalRecipes()));
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
}