package com.diogo.cookup.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.animation.AnimationUtils;
import com.diogo.cookup.R;
import com.diogo.cookup.data.model.RecipeData;
import com.diogo.cookup.ui.adapter.CategoryAdapter;
import com.diogo.cookup.ui.adapter.RecipeAdapterLarge;
import com.diogo.cookup.viewmodel.ExploreViewModel;
import com.diogo.cookup.viewmodel.SavedListViewModel;
import com.diogo.cookup.data.model.UserData;
import com.diogo.cookup.utils.SharedPrefHelper;

import java.util.ArrayList;

public class ExploreFragment extends Fragment {

    private ExploreViewModel viewModel;
    private SavedListViewModel savedListViewModel;
    private RecipeAdapterLarge recipeAdapter;
    private CategoryAdapter categoryAdapter;

    private RecyclerView recyclerCategories, recyclerRecipes;
    private TextView textEndMessage;
    private EditText searchEditText;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean isLoadingMore = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ExploreViewModel.class);
        savedListViewModel = new ViewModelProvider(requireActivity()).get(SavedListViewModel.class);

        searchEditText = view.findViewById(R.id.searchEditText);
        recyclerCategories = view.findViewById(R.id.recyclerCategories);
        recyclerRecipes = view.findViewById(R.id.recyclerRecipes);
        textEndMessage = view.findViewById(R.id.textEndMessage);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        setupSearchBar(view);
        setupCategoryRecycler(view);
        setupRecipeRecycler();

        textEndMessage.setVisibility(View.GONE);

        savedListViewModel.getSavedRecipeIds().observe(getViewLifecycleOwner(), savedIds -> {
            recipeAdapter.updateSavedIds(savedIds);
        });

        UserData user = SharedPrefHelper.getInstance(requireContext()).getUser();
        if (user != null) {
            savedListViewModel.loadUserSavedRecipeIds(user.getUserId());
        }

        swipeRefreshLayout.setOnRefreshListener(() -> {
            viewModel.resetPagination();
            viewModel.loadCategories();
            viewModel.loadNextPage();
            swipeRefreshLayout.setRefreshing(false);
        });

        observeViewModel();
        viewModel.resetPagination();
        viewModel.loadCategories();
        viewModel.loadNextPage();
    }

    private void setupSearchBar(View view) {
        if (searchEditText != null) {
            View.OnClickListener navigateToSearch = v ->
                    NavHostFragment.findNavController(this)
                            .navigate(R.id.action_exploreFragment_to_searchSuggestionsFragment);

            searchEditText.setOnClickListener(navigateToSearch);
            searchEditText.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) navigateToSearch.onClick(v);
            });
        }
    }

    private void setupCategoryRecycler(View view) {
        categoryAdapter = new CategoryAdapter(new ArrayList<>(), category -> {
            ExploreFragmentDirections.ActionExploreFragmentToSearchResultFragment action =
                    ExploreFragmentDirections.actionExploreFragmentToSearchResultFragment(category.getCategoryName());
            Navigation.findNavController(view).navigate(action);
        });

        recyclerCategories.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerCategories.setAdapter(categoryAdapter);
        recyclerCategories.setLayoutAnimation(
                AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_fade_slide_up));
    }

    private void setupRecipeRecycler() {
        recipeAdapter = new RecipeAdapterLarge(
                requireContext(),
                this::openRecipeDetail,
                recipeId -> SaveRecipeBottomSheet.newInstance(recipeId, this)
                        .show(requireActivity().getSupportFragmentManager(), "save_sheet")
        );
        recyclerRecipes.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerRecipes.setAdapter(recipeAdapter);
        recyclerRecipes.setLayoutAnimation(
                AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_fade_slide_up));

        recyclerRecipes.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy <= 0) return;

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager == null) return;

                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                if (!isLoadingMore && lastVisibleItem >= totalItemCount - 2
                        && Boolean.FALSE.equals(viewModel.getIsLastPage().getValue())) {
                    isLoadingMore = true;
                    recipeAdapter.setPaginating(true); // trigger footer
                    viewModel.loadNextPage();
                }
            }
        });
    }

    private void observeViewModel() {
        viewModel.getPopularCategories().observe(getViewLifecycleOwner(), response -> {
            if (response != null && response.getData() != null) {
                categoryAdapter.setData(response.getData());
                recyclerCategories.scheduleLayoutAnimation();
            }
        });

        viewModel.getAllRecipes().observe(getViewLifecycleOwner(), recipes -> {
            if (recipes != null) {
                recipeAdapter.setDataWithAnimation(recipes);
                if (recipeAdapter.getItemCount() <= 6) {
                    recyclerRecipes.scheduleLayoutAnimation();
                }
            }
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), loading -> {
            isLoadingMore = loading != null && loading;
        });

        viewModel.getIsLastPage().observe(getViewLifecycleOwner(), isLast -> {
            if (Boolean.TRUE.equals(isLast) && recipeAdapter.getItemCount() > 0) {
                textEndMessage.setVisibility(View.VISIBLE);
            } else {
                textEndMessage.setVisibility(View.GONE);
            }
        });
    }

    private void openRecipeDetail(RecipeData recipe) {
        ExploreFragmentDirections.ActionExploreFragmentToRecipeDetailFragment action =
                ExploreFragmentDirections.actionExploreFragmentToRecipeDetailFragment(recipe.getRecipeId());
        Navigation.findNavController(requireView()).navigate(action);
    }
}
