package com.diogo.cookup.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.diogo.cookup.data.model.UserData;
import com.diogo.cookup.ui.adapter.RecipeAdapterLarge;
import com.diogo.cookup.utils.SharedPrefHelper;
import com.diogo.cookup.viewmodel.SavedListViewModel;
import com.diogo.cookup.viewmodel.SearchViewModel;

import java.util.ArrayList;

public class SearchResultFragment extends Fragment {

    private SearchViewModel searchViewModel;
    private SavedListViewModel savedListViewModel;
    private RecipeAdapterLarge recipeAdapter;
    private RecyclerView recyclerView;

    private EditText editTextSearch;
    private ImageView buttonBack;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);

        editTextSearch = view.findViewById(R.id.editTextSearch);
        buttonBack = view.findViewById(R.id.buttonBack);
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

        savedListViewModel.getSavedRecipeIds().observe(getViewLifecycleOwner(), recipeAdapter::updateSavedIds);

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
            if (NavHostFragment.findNavController(this).getPreviousBackStackEntry() != null) {
                NavHostFragment.findNavController(this).popBackStack();
            } else {
                requireActivity().onBackPressed();
            }
        });

        editTextSearch.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                final int DRAWABLE_END = 2;

                Drawable drawable = editTextSearch.getCompoundDrawables()[DRAWABLE_END];
                if (drawable != null) {
                    int drawableWidth = drawable.getBounds().width();

                    int[] location = new int[2];
                    editTextSearch.getLocationOnScreen(location);
                    int editTextRight = location[0] + editTextSearch.getWidth();

                    int touchX = (int) event.getRawX();

                    if (touchX >= (editTextRight - drawableWidth - editTextSearch.getPaddingEnd())) {
                        editTextSearch.setText("");
                        editTextSearch.requestFocus();
                        v.performClick();
                        return true;
                    }
                }
            }
            return false;
        });

        editTextSearch.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                String currentText = editTextSearch.getText().toString();
                NavHostFragment.findNavController(this)
                        .navigate(SearchResultFragmentDirections
                                .actionSearchResultFragmentToSearchSuggestionsFragment()
                                .setQuery(currentText));
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
                .navigate(SearchResultFragmentDirections
                        .actionSearchResultFragmentToRecipeDetailFragment(recipe.getRecipeId()));
    }
}
