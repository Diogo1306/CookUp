package com.diogo.cookup.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diogo.cookup.R;
import com.diogo.cookup.data.model.RecipeData;
import com.diogo.cookup.ui.adapter.CategoryAdapter;
import com.diogo.cookup.ui.adapter.RecipeAdapterDefault;
import com.diogo.cookup.viewmodel.ExploreViewModel;

import java.util.ArrayList;

public class ExploreFragment extends Fragment {

    private ExploreViewModel exploreViewModel;

    private EditText editTextSearch;
    private RecyclerView recyclerViewCategories;
    private RecyclerView recyclerViewRecipes;

    private CategoryAdapter categoryAdapter;
    private RecipeAdapterDefault recipeAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        exploreViewModel = new ViewModelProvider(this).get(ExploreViewModel.class);

        editTextSearch = view.findViewById(R.id.editTextSearch);
        recyclerViewCategories = view.findViewById(R.id.recyclerViewCategories);
        recyclerViewRecipes = view.findViewById(R.id.recyclerViewRecipes);

        setupCategorySection(view);
        setupRecipeSection(view);
        setupObservers();

        editTextSearch.setFocusableInTouchMode(true);

        editTextSearch.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                NavHostFragment.findNavController(ExploreFragment.this)
                        .navigate(R.id.action_exploreFragment_to_searchSuggestionsFragment);
            }
        });

        editTextSearch.setOnClickListener(v -> {
            editTextSearch.requestFocus();
            NavHostFragment.findNavController(ExploreFragment.this)
                    .navigate(R.id.action_exploreFragment_to_searchSuggestionsFragment);
        });

        loadInitialContent();

        return view;
    }

    private void setupCategorySection(View view) {
        recyclerViewCategories.setLayoutManager(new GridLayoutManager(getContext(), 3));
        categoryAdapter = new CategoryAdapter(new ArrayList<>(), category -> {
            ExploreFragmentDirections.ActionExploreFragmentToSearchResultFragment action =
                    ExploreFragmentDirections.actionExploreFragmentToSearchResultFragment(category.getCategoryName());
            Navigation.findNavController(view).navigate(action);
        });
        recyclerViewCategories.setAdapter(categoryAdapter);
    }

    private void setupRecipeSection(View view) {
        recipeAdapter = new RecipeAdapterDefault(
                new ArrayList<>(), new ArrayList<>(),
                this::openRecipeDetail,
                recipeId -> {}
        );
        recyclerViewRecipes.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewRecipes.setAdapter(recipeAdapter);
    }

    private void setupObservers() {
        exploreViewModel.getPopularCategories().observe(getViewLifecycleOwner(), response -> {
            if (response != null && response.getData() != null) {
                categoryAdapter.setSkeletonMode(false);
                categoryAdapter.setData(response.getData());
            }
        });

        exploreViewModel.getPopularRecipes().observe(getViewLifecycleOwner(), response -> {
            if (response != null && response.getData() != null) {
                recipeAdapter.setSkeletonMode(false);
                recipeAdapter.setData(response.getData());
            }
        });
    }

    private void loadInitialContent() {
        categoryAdapter.setSkeletonMode(true);
        recipeAdapter.setSkeletonMode(true);
    }

    private void openRecipeDetail(RecipeData recipe) {
        ExploreFragmentDirections.ActionExploreFragmentToRecipeDetailFragment action =
                ExploreFragmentDirections.actionExploreFragmentToRecipeDetailFragment(recipe.getRecipeId());
        Navigation.findNavController(requireView()).navigate(action);
    }
}
