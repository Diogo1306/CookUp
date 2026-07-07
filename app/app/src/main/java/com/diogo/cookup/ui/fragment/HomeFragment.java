package com.diogo.cookup.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.diogo.cookup.R;
import com.diogo.cookup.data.model.CategoryData;
import com.diogo.cookup.data.model.RecipeData;
import com.diogo.cookup.data.model.UserData;
import com.diogo.cookup.ui.adapter.CategoryAdapter;
import com.diogo.cookup.ui.adapter.RecipeAdapterDefault;
import com.diogo.cookup.ui.dialog.SaveRecipeBottomSheet;
import com.diogo.cookup.utils.SharedPrefHelper;
import com.diogo.cookup.viewmodel.CategoryViewModel;
import com.diogo.cookup.viewmodel.HomeFeedViewModel;
import com.diogo.cookup.viewmodel.RecipeViewModel;
import com.diogo.cookup.viewmodel.SavedListViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecipeAdapterDefault recipeAdapter, adapterWeekly, adapterPopular, adapterCat1, adapterCat2, adapterCat3;
    private CategoryAdapter categoryAdapter;

    private CategoryViewModel categoryViewModel;
    private RecipeViewModel recipeViewModel;
    private SavedListViewModel savedListViewModel;
    private HomeFeedViewModel homeFeedViewModel;

    private RecyclerView recyclerCategories, recyclerRecommended, recyclerWeekly, recyclerPopular, recyclerCat1, recyclerCat2, recyclerCat3;
    private TextView tvNameHome, tvCategoryTitle1, tvCategoryTitle2, tvCategoryTitle3, seeMoreCat1, seeMoreCat2, seeMoreCat3;
    private EditText searchEditText;
    private SwipeRefreshLayout swipeRefreshLayout;

    private String categoryName1 = "";
    private String categoryName2 = "";
    private String categoryName3 = "";

    private final List<RecipeData> recipeList = new ArrayList<>();
    private final List<CategoryData> categoryList = new ArrayList<>();
    private final List<Integer> savedRecipeIds = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModels();
        initViews(view);
        initSeeMore(view);
        setupSearchBar(view);

        new Handler().postDelayed(() -> {
            if (!isAdded()) return;
            initAdapters();
            setAdapters();
            observeViewModels();
            loadInitialData();
        }, 200);
    }

    private void setupSearchBar(View view) {
        if (searchEditText != null) {
            View.OnClickListener navigateToSearch = v -> NavHostFragment.findNavController(this)
                    .navigate(R.id.action_homeFragment_to_searchSuggestionsFragment);

            searchEditText.setOnClickListener(navigateToSearch);
            searchEditText.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) navigateToSearch.onClick(v);
            });
        }
    }

    private void initViewModels() {
        savedListViewModel = new ViewModelProvider(requireActivity()).get(SavedListViewModel.class);
        recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        homeFeedViewModel = new ViewModelProvider(this).get(HomeFeedViewModel.class);
    }

    private void initViews(View view) {
        recyclerCategories = view.findViewById(R.id.recyclerCategories);
        recyclerRecommended = view.findViewById(R.id.recyclerRecommended);
        recyclerWeekly = view.findViewById(R.id.recyclerWeekly);
        recyclerPopular = view.findViewById(R.id.recyclerPopular);
        recyclerCat1 = view.findViewById(R.id.recyclerCat1);
        recyclerCat2 = view.findViewById(R.id.recyclerCat2);
        recyclerCat3 = view.findViewById(R.id.recyclerCat3);
        tvCategoryTitle1 = view.findViewById(R.id.tvCategoryTitle1);
        tvCategoryTitle2 = view.findViewById(R.id.tvCategoryTitle2);
        tvCategoryTitle3 = view.findViewById(R.id.tvCategoryTitle3);
        seeMoreCat1 = view.findViewById(R.id.see_more_cat1);
        seeMoreCat2 = view.findViewById(R.id.see_more_cat2);
        seeMoreCat3 = view.findViewById(R.id.see_more_cat3);
        tvNameHome = view.findViewById(R.id.tvNameHome);
        searchEditText = view.findViewById(R.id.searchEditText);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            UserData user = SharedPrefHelper.getInstance(requireContext()).getUser();
            if (user != null) {
                int userId = user.getUserId();
                savedListViewModel.reloadAll(userId);
                homeFeedViewModel.loadHomeFeed(userId);
            }
            new Handler().postDelayed(() -> swipeRefreshLayout.setRefreshing(false), 800);
        });

        UserData user = SharedPrefHelper.getInstance(requireContext()).getUser();
        if (user != null) {
            tvNameHome.setText(getString(R.string.home_greeting, user.getUsername()));
        }

        recyclerCategories.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerRecommended.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerWeekly.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerPopular.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerCat1.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerCat2.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerCat3.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    private void initSeeMore(View view) {
        view.findViewById(R.id.see_more_recommended).setOnClickListener(v -> {
            UserData user = SharedPrefHelper.getInstance(requireContext()).getUser();
            int userId = user != null ? user.getUserId() : 0;
            navigateToFilteredSearch("", "recommended", "", 0, 0, userId);
        });

        view.findViewById(R.id.see_more_week).setOnClickListener(v -> {
            UserData user = SharedPrefHelper.getInstance(requireContext()).getUser();
            int userId = user != null ? user.getUserId() : 0;
            navigateToFilteredSearch("", "week", "", 0, 0, userId);
        });

        view.findViewById(R.id.see_more_popular).setOnClickListener(v -> {
            UserData user = SharedPrefHelper.getInstance(requireContext()).getUser();
            int userId = user != null ? user.getUserId() : 0;
            navigateToFilteredSearch("", "popular", "", 0, 0, userId);
        });


        seeMoreCat1.setOnClickListener(v -> navigateToFilteredSearch(categoryName1, "", "", 0, 0));
        seeMoreCat2.setOnClickListener(v -> navigateToFilteredSearch(categoryName2, "", "", 0, 0));
        seeMoreCat3.setOnClickListener(v -> navigateToFilteredSearch(categoryName3, "", "", 0, 0));

        homeFeedViewModel.getCategoryName1().observe(getViewLifecycleOwner(), name -> categoryName1 = name);
        homeFeedViewModel.getCategoryName2().observe(getViewLifecycleOwner(), name -> categoryName2 = name);
        homeFeedViewModel.getCategoryName3().observe(getViewLifecycleOwner(), name -> categoryName3 = name);

    }


    private void fadeInWithAdapter(RecyclerView recyclerView, RecyclerView.Adapter<?> adapter) {
        if (recyclerView.getAdapter() != adapter) {
            recyclerView.setAlpha(0f);
            recyclerView.setAdapter(adapter);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.animate().alpha(1f).setDuration(300).withEndAction(() -> animateRecycler(recyclerView)).start();
        }
    }

    private void initAdapters() {
        recipeAdapter = createRecipeAdapter(R.layout.item_recipe_default);
        adapterWeekly = createRecipeAdapter(R.layout.item_recipe_meduim);
        adapterPopular = createRecipeAdapter(R.layout.item_recipe_default);
        adapterCat1 = createRecipeAdapter(R.layout.item_recipe_default);
        adapterCat2 = createRecipeAdapter(R.layout.item_recipe_default);
        adapterCat3 = createRecipeAdapter(R.layout.item_recipe_default);
        categoryAdapter = new CategoryAdapter(categoryList, category -> {
            HomeFragmentDirections.ActionHomeFragmentToSearchResultFragment action =
                    HomeFragmentDirections.actionHomeFragmentToSearchResultFragment(category.getCategoryName());
            Navigation.findNavController(requireView()).navigate(action);
        });
        recyclerCategories.setAdapter(categoryAdapter);
    }

    private RecipeAdapterDefault createRecipeAdapter(int layoutResId) {
        return new RecipeAdapterDefault(new ArrayList<>(), savedRecipeIds,
                this::openRecipeDetail,
                recipeId -> SaveRecipeBottomSheet.newInstance(recipeId, this)
                        .show(requireActivity().getSupportFragmentManager(), "save_sheet"),
                layoutResId);
    }

    private void setAdapters() {
        recyclerRecommended.setAdapter(recipeAdapter);
        recyclerWeekly.setAdapter(adapterWeekly);
        recyclerPopular.setAdapter(adapterPopular);
        recyclerCat1.setAdapter(adapterCat1);
        recyclerCat2.setAdapter(adapterCat2);
        recyclerCat3.setAdapter(adapterCat3);
    }

    private void observeViewModels() {
        if (!isAdded()) return;

        categoryViewModel.getCategoriesLiveData().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null && !categories.isEmpty()) {
                categoryAdapter.setData(categories);
            }
        });

        homeFeedViewModel.getRecommendedRecipes().observe(getViewLifecycleOwner(), list -> {
            if (list != null && !list.isEmpty()) {
                recipeList.clear();
                recipeList.addAll(list);
                recipeAdapter.updateData(list, savedRecipeIds);
                fadeInWithAdapter(recyclerRecommended, recipeAdapter);
            }
        });

        savedListViewModel.getSavedRecipeIds().observe(getViewLifecycleOwner(), savedIds -> {
            if (savedIds != null) {
                setupAllAdapters(savedIds);
            }
        });

        savedListViewModel.getChangedRecipeId().observe(getViewLifecycleOwner(), recipeId -> {
            if (recipeId != null) {
                UserData user = SharedPrefHelper.getInstance(requireContext()).getUser();
                if (user != null) {
                    int userId = user.getUserId();
                    savedListViewModel.reloadAll(userId);
                    homeFeedViewModel.loadHomeFeed(userId);
                }
            }
        });

        homeFeedViewModel.getWeeklyRecipes().observe(getViewLifecycleOwner(), list -> {
            if (list != null && !list.isEmpty()) {
                adapterWeekly.updateData(list, savedRecipeIds);
                fadeInWithAdapter(recyclerWeekly, adapterWeekly);
            }
        });

        homeFeedViewModel.getPopularRecipes().observe(getViewLifecycleOwner(), list -> {
            if (list != null && !list.isEmpty()) {
                adapterPopular.updateData(list, savedRecipeIds);
                fadeInWithAdapter(recyclerPopular, adapterPopular);
            }
        });

        homeFeedViewModel.getCategoryRecipes1().observe(getViewLifecycleOwner(), list -> {
            boolean hasData = list != null && !list.isEmpty();
            tvCategoryTitle1.setVisibility(hasData ? View.VISIBLE : View.GONE);
            recyclerCat1.setVisibility(hasData ? View.VISIBLE : View.GONE);
            seeMoreCat1.setVisibility(hasData ? View.VISIBLE : View.GONE);
            if (hasData) {
                adapterCat1.updateData(list, savedRecipeIds);
                fadeInWithAdapter(recyclerCat1, adapterCat1);
            }
        });

        homeFeedViewModel.getCategoryRecipes2().observe(getViewLifecycleOwner(), list -> {
            boolean hasData = list != null && !list.isEmpty();
            tvCategoryTitle2.setVisibility(hasData ? View.VISIBLE : View.GONE);
            recyclerCat2.setVisibility(hasData ? View.VISIBLE : View.GONE);
            seeMoreCat2.setVisibility(hasData ? View.VISIBLE : View.GONE);
            if (hasData) {
                adapterCat2.updateData(list, savedRecipeIds);
                fadeInWithAdapter(recyclerCat2, adapterCat2);
            }
        });

        homeFeedViewModel.getCategoryRecipes3().observe(getViewLifecycleOwner(), list -> {
            boolean hasData = list != null && !list.isEmpty();
            tvCategoryTitle3.setVisibility(hasData ? View.VISIBLE : View.GONE);
            recyclerCat3.setVisibility(hasData ? View.VISIBLE : View.GONE);
            seeMoreCat3.setVisibility(hasData ? View.VISIBLE : View.GONE);
            if (hasData) {
                adapterCat3.updateData(list, savedRecipeIds);
                fadeInWithAdapter(recyclerCat3, adapterCat3);
            }
        });

        homeFeedViewModel.getCategoryTitle1().observe(getViewLifecycleOwner(), tvCategoryTitle1::setText);
        homeFeedViewModel.getCategoryTitle2().observe(getViewLifecycleOwner(), tvCategoryTitle2::setText);
        homeFeedViewModel.getCategoryTitle3().observe(getViewLifecycleOwner(), tvCategoryTitle3::setText);
    }

    private void setupAllAdapters(List<Integer> savedIds) {
        savedRecipeIds.clear();
        savedRecipeIds.addAll(savedIds);
        recipeAdapter.updateData(recipeList, savedRecipeIds);
        updateAllAdapters();
    }

    private void updateAllAdapters() {
        adapterWeekly.updateSavedIds(savedRecipeIds);
        adapterPopular.updateSavedIds(savedRecipeIds);
        adapterCat1.updateSavedIds(savedRecipeIds);
        adapterCat2.updateSavedIds(savedRecipeIds);
        adapterCat3.updateSavedIds(savedRecipeIds);
        recipeAdapter.updateSavedIds(savedRecipeIds);
    }

    private void loadInitialData() {
        UserData user = SharedPrefHelper.getInstance(requireContext()).getUser();
        if (user != null && user.getUserId() > 0) {
            int userId = user.getUserId();
            Log.d("HOME_USER", "➡️ userId detectado: " + userId);
            categoryViewModel.loadUserCategories(userId);
            homeFeedViewModel.loadHomeFeed(userId);
            savedListViewModel.setUser(user);
            savedListViewModel.loadUserSavedRecipeIds(userId);
        } else {
            Log.e("HOME_USER", "⚠️ Utilizador não encontrado ou inválido.");
        }
    }

    private void openRecipeDetail(RecipeData recipe) {
        Navigation.findNavController(requireView()).navigate(
                HomeFragmentDirections.actionHomeFragmentToRecipeDetailFragment(recipe.getRecipeId())
        );
    }

    private void animateRecycler(RecyclerView recyclerView) {
        if (recyclerView.getAdapter() != null && recyclerView.getItemAnimator() == null) {
            recyclerView.setLayoutAnimation(
                    AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_fade_slide_up)
            );
            recyclerView.scheduleLayoutAnimation();
        }
    }

    private void navigateToFilteredSearch(String query, String filter, String difficulty, int maxTime, int maxIngredients) {
        navigateToFilteredSearch(query, filter, difficulty, maxTime, maxIngredients, 0);
    }

    private void navigateToFilteredSearch(String query, String filter, String difficulty, int maxTime, int maxIngredients, int userId) {
        HomeFragmentDirections.ActionHomeFragmentToSearchResultFragment action =
                HomeFragmentDirections.actionHomeFragmentToSearchResultFragment(query);
        action.setFilter(filter);
        action.setDifficulty(difficulty);
        action.setMaxTime(maxTime);
        action.setMaxIngredients(maxIngredients);
        action.setUserId(userId);

        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public void onResume() {
        super.onResume();
        UserData user = SharedPrefHelper.getInstance(requireContext()).getUser();
        if (user != null && user.getUserId() > 0) {
            int userId = user.getUserId();
            savedListViewModel.reloadAll(userId);
            homeFeedViewModel.loadHomeFeed(userId);
            categoryViewModel.loadUserCategories(userId);
        }
    }
}