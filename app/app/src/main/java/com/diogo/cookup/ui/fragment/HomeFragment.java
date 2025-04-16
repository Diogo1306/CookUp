package com.diogo.cookup.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diogo.cookup.R;
import com.diogo.cookup.data.model.RecipeData;
import com.diogo.cookup.ui.adapter.RecipeAdapterDefault;
import com.diogo.cookup.utils.SharedPrefHelper;
import com.diogo.cookup.viewmodel.RecipeViewModel;
import com.diogo.cookup.viewmodel.SavedListViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecipeAdapterDefault recipeAdapter;
    private RecipeViewModel recipeViewModel;
    private SavedListViewModel savedListViewModel;

    private final List<RecipeData> recipeList = new ArrayList<>();
    private final List<Integer> savedRecipeIds = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("HOME_DEBUG", "HomeFragment carregou.");

        RecyclerView recyclerView = view.findViewById(R.id.recycler_recipes);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        savedListViewModel = new ViewModelProvider(requireActivity()).get(SavedListViewModel.class);

        recipeAdapter = new RecipeAdapterDefault(
                new ArrayList<>(),
                new ArrayList<>(),
                recipe -> openRecipeDetail(recipe),
                recipeId -> {
                    SaveRecipeBottomSheet.newInstance(recipeId)
                            .show(getChildFragmentManager(), "save_sheet");
                }
        );

        recyclerView.setAdapter(recipeAdapter);

        recipeViewModel.getRecipesLiveData().observe(getViewLifecycleOwner(), recipes -> {
            if (recipes != null) {
                Log.d("HOME_DEBUG", "Receitas carregadas: " + recipes.size());
                for (RecipeData recipe : recipes) {
                    Log.d("HOME_DEBUG", "→ " + recipe.getTitle());
                }

                recipeList.clear();
                recipeList.addAll(recipes);
                recipeAdapter.updateData(recipeList, savedRecipeIds);
            } else {
                Log.d("HOME_DEBUG", "Lista de receitas veio nula");
            }
        });

        savedListViewModel.getSavedRecipeIds().observe(getViewLifecycleOwner(), ids -> {
            savedRecipeIds.clear();
            savedRecipeIds.addAll(ids);
            recipeAdapter.updateData(recipeList, savedRecipeIds);
        });

        recipeViewModel.loadRecipes();

        if (SharedPrefHelper.getInstance(requireContext()).getUser() != null) {
            int userId = SharedPrefHelper.getInstance(requireContext()).getUser().getUserId();
            savedListViewModel.loadUserSavedRecipeIds(userId);
        }
    }

    private void openRecipeDetail(RecipeData recipe) {
        int id = recipe.getRecipeId();
        Log.d("RECIPE_CLICK", "Abrindo RecipeDetailFragment para ID: " + id);

        Fragment fragment = RecipeDetailFragment.newInstance(id);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();

        Log.d("RECIPE_CLICK", "Transação de fragmento enviada.");
    }

}