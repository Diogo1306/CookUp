package com.diogo.cookup.ui.fragment;

import android.os.Bundle;
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
import com.diogo.cookup.ui.adapter.RecipeAdapterDefault;
import com.diogo.cookup.viewmodel.SearchViewModel;

import java.util.ArrayList;

public class SearchResultFragment extends Fragment {

    private SearchViewModel searchViewModel;
    private RecipeAdapterDefault adapter;
    private RecyclerView recyclerView;

    private EditText editTextSearch;
    private ImageView buttonBack;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);

        editTextSearch = view.findViewById(R.id.editTextSearch);
        buttonBack = view.findViewById(R.id.buttonBack);
        recyclerView = view.findViewById(R.id.recycler_results);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecipeAdapterDefault(new ArrayList<>(), new ArrayList<>(), this::openRecipeDetail, recipeId -> {});
        recyclerView.setAdapter(adapter);

        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        buttonBack.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

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
        adapter.setSkeletonMode(true);
        adapter.setData(new ArrayList<>());

        searchViewModel.fetchSearchResult(query).observe(getViewLifecycleOwner(), response -> {
            adapter.setSkeletonMode(false);
            if (response != null && response.getData() != null && response.getData().getRecipes() != null) {
                adapter.setData(response.getData().getRecipes());
            } else {
                adapter.setData(new ArrayList<>());
                Toast.makeText(getContext(), "Nenhuma receita encontrada.", Toast.LENGTH_SHORT).show();
            }
        });
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
}
