package com.diogo.cookup.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.diogo.cookup.ui.dialog.FiltersBottomSheet;
import com.diogo.cookup.ui.dialog.SaveRecipeBottomSheet;
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

    private String currentFilter = "";
    private String currentDifficulty = "";
    private int currentMaxTime = 0;
    private int currentMaxIngredients = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);

        editTextSearch = view.findViewById(R.id.editTextSearch);
        buttonBack = view.findViewById(R.id.buttonBack);
        recyclerView = view.findViewById(R.id.recycler_results);
        ImageButton buttonFilters = view.findViewById(R.id.buttonFilters);

        buttonFilters.setOnClickListener(v -> {
            FiltersBottomSheet dialog = new FiltersBottomSheet();

            Bundle args = new Bundle();
            args.putString("filter", currentFilter);
            args.putString("difficulty", currentDifficulty);
            args.putInt("maxTime", currentMaxTime);
            args.putInt("maxIngredients", currentMaxIngredients);
            dialog.setArguments(args);

            dialog.setOnFiltersAppliedListener((filter, difficulty, maxTime, maxIngredients) -> {
                currentFilter = filter;
                currentDifficulty = difficulty;
                currentMaxTime = maxTime;
                currentMaxIngredients = maxIngredients;
                performSearch(editTextSearch.getText().toString().trim());
            });

            dialog.show(getChildFragmentManager(), "filters_bottom_sheet");
        });

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

        searchViewModel.getFilteredRecipesResult().observe(getViewLifecycleOwner(), response -> {
            recipeAdapter.setSkeletonMode(false);
            if (response != null && response.getData() != null) {
                recipeAdapter.setData(response.getData());
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
            if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                String newQuery = editTextSearch.getText().toString().trim();
                if (!newQuery.isEmpty()) performSearch(newQuery);
                return true;
            }
            return false;
        });

        SearchResultFragmentArgs args = SearchResultFragmentArgs.fromBundle(getArguments());
        String query = args.getQuery();
        currentFilter = args.getFilter();
        currentDifficulty = args.getDifficulty();
        currentMaxTime = args.getMaxTime();
        currentMaxIngredients = args.getMaxIngredients();

        editTextSearch.setText(query);
        editTextSearch.setSelection(query.length());

        if (!query.isEmpty() || !currentFilter.isEmpty()) {
            performSearch(query);
        }

        return view;
    }

    private void performSearch(String query) {
        recipeAdapter.setSkeletonMode(true);
        recipeAdapter.setData(new ArrayList<>());

        UserData user = SharedPrefHelper.getInstance(requireContext()).getUser();
        int userId = (user != null) ? user.getUserId() : 0;

        searchViewModel.searchRecipesWithFilters(
                query,
                currentFilter,
                userId,
                currentDifficulty,
                null,
                currentMaxTime > 0 ? currentMaxTime : null,
                currentMaxIngredients > 0 ? currentMaxIngredients : null,
                12,
                0
        );
    }

    private void openRecipeDetail(RecipeData recipe) {
        NavHostFragment.findNavController(this)
                .navigate(SearchResultFragmentDirections
                        .actionSearchResultFragmentToRecipeDetailFragment(recipe.getRecipeId()));
    }
}
