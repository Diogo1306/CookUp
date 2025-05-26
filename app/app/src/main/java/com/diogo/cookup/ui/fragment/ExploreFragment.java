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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diogo.cookup.R;
import com.diogo.cookup.data.model.CategoryData;
import com.diogo.cookup.data.model.RecipeData;
import com.diogo.cookup.ui.adapter.CategoryAdapter;
import com.diogo.cookup.ui.adapter.RecipeAdapterDefault;
import com.diogo.cookup.viewmodel.ExploreViewModel;

import java.util.ArrayList;
import java.util.List;

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

        setupCategorySection();
        setupRecipeSection();
        setupObservers();

        editTextSearch.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_exploreFragment_to_searchSuggestionsFragment);
        });

        loadInitialContent();

        return view;
    }

    private void setupCategorySection() {
        recyclerViewCategories.setLayoutManager(new GridLayoutManager(getContext(), 3));
        categoryAdapter = new CategoryAdapter(new ArrayList<>(), category -> {
            Bundle bundle = new Bundle();
            bundle.putString("query", category.getCategoryName());
            Navigation.findNavController(getView()).navigate(R.id.action_searchSuggestionsFragment_to_searchResultFragment, bundle);
        });
        recyclerViewCategories.setAdapter(categoryAdapter);
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

    private void setupRecipeSection() {
        recipeAdapter = new RecipeAdapterDefault(
                new ArrayList<>(), new ArrayList<>(),
                this::openRecipeDetail,
                recipeId -> {}
        );
        recyclerViewRecipes.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewRecipes.setAdapter(recipeAdapter);
    }

    private void loadInitialContent() {
        categoryAdapter.setSkeletonMode(false);
        categoryAdapter.setData(new ArrayList<>());

        recipeAdapter.setSkeletonMode(false);
        recipeAdapter.setData(new ArrayList<>());
    }

    private void openRecipeDetail(RecipeData recipe) {
        Bundle bundle = new Bundle();
        bundle.putInt("recipe_id", recipe.getRecipeId());

        Navigation.findNavController(getView()).navigate(R.id.action_searchResultFragment_to_recipeDetailFragment, bundle);
    }
}
