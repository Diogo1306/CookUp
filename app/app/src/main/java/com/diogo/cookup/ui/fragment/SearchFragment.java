package com.diogo.cookup.ui.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diogo.cookup.R;
import com.diogo.cookup.data.model.RecipeCategoryData;
import com.diogo.cookup.data.model.RecipeData;
import com.diogo.cookup.ui.adapter.CategoryAdapter;
import com.diogo.cookup.ui.adapter.ExploreSearchAdapter;
import com.diogo.cookup.ui.adapter.RecipeAdapterDefault;
import com.diogo.cookup.viewmodel.ExploreViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private ExploreViewModel exploreViewModel;

    private EditText editTextSearch;
    private RecyclerView recyclerViewCategories;
    private RecyclerView recyclerViewRecipes;

    private CategoryAdapter categoryAdapter;
    private RecipeAdapterDefault recipeAdapter;
    private ExploreSearchAdapter suggestionAdapter;

    private boolean showingSuggestions = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        exploreViewModel = new ViewModelProvider(this).get(ExploreViewModel.class);

        editTextSearch = view.findViewById(R.id.editTextSearch);
        recyclerViewCategories = view.findViewById(R.id.recyclerViewCategories);
        recyclerViewRecipes = view.findViewById(R.id.recyclerViewRecipes);

        setupCategorySection();
        setupRecipeSection();
        setupSearchSuggestions();

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (!query.isEmpty()) {
                    exploreViewModel.search(query);
                    showingSuggestions = true;
                } else {
                    showingSuggestions = false;
                    restoreInitialView();
                }
            }
        });

        exploreViewModel.getSearchResult().observe(getViewLifecycleOwner(), response -> {
            if (showingSuggestions && response != null && response.getData() != null) {
                suggestionAdapter.setSuggestions(response.getData());
                recyclerViewRecipes.setAdapter(suggestionAdapter);
            }
        });

        editTextSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                String query = editTextSearch.getText().toString().trim();
                if (!query.isEmpty()) {
                    openSearchResultFragment(query);
                }
                return true;
            }
            return false;
        });

        loadInitialContent();

        return view;
    }

    private void setupCategorySection() {
        recyclerViewCategories.setLayoutManager(new GridLayoutManager(getContext(), 3));
        categoryAdapter = new CategoryAdapter(new ArrayList<>(), category -> {
            openSearchResultFragment(category.getCategoryName());
        });
        recyclerViewCategories.setAdapter(categoryAdapter);
    }

    private void setupRecipeSection() {
        recipeAdapter = new RecipeAdapterDefault(
                new ArrayList<>(), new ArrayList<>(),
                recipe -> openRecipeDetail(recipe),
                recipeId -> {

                }
        );
        recyclerViewRecipes.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewRecipes.setAdapter(recipeAdapter);
    }

    private void setupSearchSuggestions() {
        suggestionAdapter = new ExploreSearchAdapter(requireContext(), (selectedText, recipe) -> {
            openSearchResultFragment(selectedText);
        });
    }

    private void openSearchResultFragment(String query) {
        Bundle bundle = new Bundle();
        bundle.putString("query", query);

        SearchResultFragment fragment = new SearchResultFragment();
        fragment.setArguments(bundle);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void openRecipeDetail(RecipeData recipe) {
        Bundle bundle = new Bundle();
        bundle.putInt("recipe_id", recipe.getRecipeId());

        RecipeDetailFragment fragment = new RecipeDetailFragment();
        fragment.setArguments(bundle);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void restoreInitialView() {
        recyclerViewRecipes.setAdapter(recipeAdapter);
    }

    private void loadInitialContent() {
        List<RecipeCategoryData> mockCategories = new ArrayList<>();
        categoryAdapter.setSkeletonMode(false);
        categoryAdapter.setData(mockCategories);

        List<RecipeData> mockRecipes = new ArrayList<>();
        recipeAdapter.setSkeletonMode(false);
        recipeAdapter.setData(mockRecipes);
    }
}
