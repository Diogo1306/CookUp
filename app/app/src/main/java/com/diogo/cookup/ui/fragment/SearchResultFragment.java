package com.diogo.cookup.ui.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diogo.cookup.R;
import com.diogo.cookup.data.model.RecipeData;
import com.diogo.cookup.ui.adapter.RecipeAdapterLarge;
import com.diogo.cookup.viewmodel.SavedListViewModel;
import com.diogo.cookup.viewmodel.SearchViewModel;
import com.diogo.cookup.data.model.UserData;
import com.diogo.cookup.utils.SharedPrefHelper;

import java.util.ArrayList;

public class SearchResultFragment extends Fragment {

    private SearchViewModel searchViewModel;
    private SavedListViewModel savedListViewModel;
    private RecipeAdapterLarge recipeAdapter;
    private RecyclerView recyclerView;

    private EditText editTextSearch;
    private ImageView buttonBack, buttonClear;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);

        editTextSearch = view.findViewById(R.id.editTextSearch);
        buttonBack = view.findViewById(R.id.buttonBack);
        buttonClear = view.findViewById(R.id.buttonClear);
        recyclerView = view.findViewById(R.id.recycler_results);

        recipeAdapter = new RecipeAdapterLarge(
                requireContext(),
                this::openRecipeDetail,
                recipeId -> SaveRecipeBottomSheet.newInstance(recipeId, this)
                        .show(requireActivity().getSupportFragmentManager(), "save_sheet")
        );
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(recipeAdapter);

        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        savedListViewModel = new ViewModelProvider(requireActivity()).get(SavedListViewModel.class);

        savedListViewModel.getSavedRecipeIds().observe(getViewLifecycleOwner(), savedIds -> {
            recipeAdapter.updateSavedIds(savedIds);
        });

        UserData user = SharedPrefHelper.getInstance(requireContext()).getUser();
        if (user != null) {
            savedListViewModel.loadUserSavedRecipeIds(user.getUserId());
        }

        searchViewModel.getSearchResult().observe(getViewLifecycleOwner(), response -> {
            recipeAdapter.setSkeletonMode(false);
            if (response != null && response.getData() != null && response.getData().getRecipes() != null) {
                recipeAdapter.setData(response.getData().getRecipes());
            } else {
                recipeAdapter.setData(new ArrayList<>());
                Toast.makeText(getContext(), "Nenhuma receita encontrada.", Toast.LENGTH_SHORT).show();
            }
        });

        buttonBack.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).popBackStack(R.id.exploreFragment, false);
        });

        buttonClear.setOnClickListener(v -> {
            editTextSearch.setText("");
            editTextSearch.clearFocus();
            Log.d("DEBUG", "Botão X clicado. Indo para sugestões com texto vazio.");
            NavHostFragment.findNavController(this)
                    .navigate(SearchResultFragmentDirections
                            .actionSearchResultFragmentToSearchSuggestionsFragment()
                            .setQuery(""));
        });


        editTextSearch.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                String currentText = editTextSearch.getText().toString();
                Log.d("DEBUG", "Abrindo sugestões com texto: " + currentText);
                NavHostFragment.findNavController(this)
                        .navigate(SearchResultFragmentDirections
                                .actionSearchResultFragmentToSearchSuggestionsFragment()
                                .setQuery(currentText));
            }
        });

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buttonClear.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
            }
        });

        editTextSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                String newQuery = editTextSearch.getText().toString().trim();
                if (!newQuery.isEmpty()) performSearch(newQuery);
                return true;
            }
            return false;
        });

        SearchResultFragmentArgs args = SearchResultFragmentArgs.fromBundle(getArguments());
        String query = args.getQuery();

        editTextSearch.setText(query);
        editTextSearch.setSelection(query.length());

        if (!query.isEmpty()) {
            performSearch(query);
        }

        return view;
    }

    private void performSearch(String query) {
        recipeAdapter.setSkeletonMode(true);
        recipeAdapter.setData(new ArrayList<>());
        searchViewModel.searchRecipes(query);
    }

    private void openRecipeDetail(RecipeData recipe) {
        NavHostFragment.findNavController(this)
                .navigate(SearchResultFragmentDirections.actionSearchResultFragmentToRecipeDetailFragment(recipe.getRecipeId()));
    }
}
