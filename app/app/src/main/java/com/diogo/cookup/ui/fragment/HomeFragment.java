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

import com.diogo.cookup.R;
import com.diogo.cookup.data.model.CategoryData;
import com.diogo.cookup.data.model.RecipeData;
import com.diogo.cookup.data.model.UserData;
import com.diogo.cookup.ui.adapter.CategoryAdapter;
import com.diogo.cookup.ui.adapter.RecipeAdapterDefault;
import com.diogo.cookup.ui.adapter.RecipeSkeletonAdapter;
import com.diogo.cookup.utils.SharedPrefHelper;
import com.diogo.cookup.viewmodel.CategoryViewModel;
import com.diogo.cookup.viewmodel.HomeFeedViewModel;
import com.diogo.cookup.viewmodel.RecipeViewModel;
import com.diogo.cookup.viewmodel.SavedListViewModel;
import com.diogo.cookup.viewmodel.UserStatsViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecipeAdapterDefault recipeAdapter, adapterWeekly, adapterPopular, adapterCat1, adapterCat2, adapterCat3;
    private CategoryAdapter categoryAdapter;

    private CategoryViewModel categoryViewModel;
    private RecipeViewModel recipeViewModel;
    private SavedListViewModel savedListViewModel;
    private UserStatsViewModel userStatsViewModel;
    private HomeFeedViewModel homeFeedViewModel;
    private boolean firstLoad = true;

    private RecyclerView recyclerCategories, recyclerRecommended, recyclerWeekly, recyclerPopular, recyclerCat1, recyclerCat2, recyclerCat3;
    private TextView tvNameHome , tvCategoryTitle1, tvCategoryTitle2, tvCategoryTitle3, seeMoreCat1, seeMoreCat2, seeMoreCat3;

    private EditText searchEditText;

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
        setupSearchBar(view);
        showSkeletons();

        new Handler().postDelayed(() -> {
            if (!isAdded()) return;
            initAdapters();
            setAdapters();
            observeViewModels();
            loadInitialData();
        }, 400);
    }

    private void setupSearchBar(View view) {
        if (searchEditText != null) {
            View.OnClickListener navigateToSearch = v ->
                    NavHostFragment.findNavController(this)
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
        userStatsViewModel = new ViewModelProvider(this).get(UserStatsViewModel.class);
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

        UserData user = SharedPrefHelper.getInstance(requireContext()).getUser();
        if (user != null) {
            String name = user.getUsername();
            String greeting = "Olá, " + name + " 👋";
            tvNameHome.setText(greeting);
        }

        recyclerCategories.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerRecommended.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerWeekly.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerPopular.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerCat1.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerCat2.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerCat3.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    private void showSkeletons() {
        RecipeSkeletonAdapter recipeSkeleton = new RecipeSkeletonAdapter(5);
        recyclerRecommended.setAdapter(recipeSkeleton);
        recyclerWeekly.setAdapter(recipeSkeleton);
        recyclerPopular.setAdapter(recipeSkeleton);
        recyclerCat1.setAdapter(recipeSkeleton);
        recyclerCat2.setAdapter(recipeSkeleton);
        recyclerCat3.setAdapter(recipeSkeleton);

        categoryAdapter = new CategoryAdapter(categoryList, category -> {
            recipeViewModel.loadRecipesByCategory(category.getCategoryId());
        });
        categoryAdapter.setSkeletonMode(true);
        recyclerCategories.setAdapter(categoryAdapter);
    }

    private void initAdapters() {
        recipeAdapter = createRecipeAdapter();
        adapterWeekly = createRecipeAdapter();
        adapterPopular = createRecipeAdapter();
        adapterCat1 = createRecipeAdapter();
        adapterCat2 = createRecipeAdapter();
        adapterCat3 = createRecipeAdapter();
    }

    private RecipeAdapterDefault createRecipeAdapter() {
        return new RecipeAdapterDefault(
                new ArrayList<>(),
                savedRecipeIds,
                this::openRecipeDetail,
                recipeId -> SaveRecipeBottomSheet.newInstance(recipeId, HomeFragment.this)
                        .show(requireActivity().getSupportFragmentManager(), "save_sheet")
        );
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

        homeFeedViewModel.getRecommendedRecipes().observe(getViewLifecycleOwner(), list -> {
            if (list != null && !list.isEmpty()) {
                recipeAdapter.setSkeletonMode(false);
                recipeList.clear();
                recipeList.addAll(list);
                recipeAdapter.updateData(list, savedRecipeIds);
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
                    savedListViewModel.reloadSavedRecipeData(userId);
                    homeFeedViewModel.loadHomeFeed(userId);
                }
            }
        });

        homeFeedViewModel.getWeeklyRecipes().observe(getViewLifecycleOwner(), list -> {
            if (list != null) {
                adapterWeekly.setSkeletonMode(false);
                adapterWeekly.updateData(list, savedRecipeIds);
            }
        });

        homeFeedViewModel.getPopularRecipes().observe(getViewLifecycleOwner(), list -> {
            if (list != null) {
                adapterPopular.setSkeletonMode(false);
                adapterPopular.updateData(list, savedRecipeIds);
            }
        });

        homeFeedViewModel.getCategoryRecipes1().observe(getViewLifecycleOwner(), list -> {
            boolean hasData = list != null && !list.isEmpty();
            tvCategoryTitle1.setVisibility(hasData ? View.VISIBLE : View.GONE);
            recyclerCat1.setVisibility(hasData ? View.VISIBLE : View.GONE);
            seeMoreCat1.setVisibility(hasData ? View.VISIBLE : View.GONE);

            if (hasData) {
                adapterCat1.setSkeletonMode(false);
                adapterCat1.updateData(list, savedRecipeIds);
            }
        });

        homeFeedViewModel.getCategoryRecipes2().observe(getViewLifecycleOwner(), list -> {
            boolean hasData = list != null && !list.isEmpty();
            tvCategoryTitle2.setVisibility(hasData ? View.VISIBLE : View.GONE);
            recyclerCat2.setVisibility(hasData ? View.VISIBLE : View.GONE);
            seeMoreCat2.setVisibility(hasData ? View.VISIBLE : View.GONE);

            if (hasData) {
                adapterCat2.setSkeletonMode(false);
                adapterCat2.updateData(list, savedRecipeIds);
            }
        });

        homeFeedViewModel.getCategoryRecipes3().observe(getViewLifecycleOwner(), list -> {
            boolean hasData = list != null && !list.isEmpty();
            tvCategoryTitle3.setVisibility(hasData ? View.VISIBLE : View.GONE);
            recyclerCat3.setVisibility(hasData ? View.VISIBLE : View.GONE);
            seeMoreCat3.setVisibility(hasData ? View.VISIBLE : View.GONE);

            if (hasData) {
                adapterCat3.setSkeletonMode(false);
                adapterCat3.updateData(list, savedRecipeIds);
            }
        });

        homeFeedViewModel.getCategoryTitle1().observe(getViewLifecycleOwner(), tvCategoryTitle1::setText);
        homeFeedViewModel.getCategoryTitle2().observe(getViewLifecycleOwner(), tvCategoryTitle2::setText);
        homeFeedViewModel.getCategoryTitle3().observe(getViewLifecycleOwner(), tvCategoryTitle3::setText);

        categoryViewModel.getCategoriesLiveData().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null && isAdded()) {
                if (categoryAdapter == null) {
                    categoryAdapter = new CategoryAdapter(categoryList, category -> {
                        recipeViewModel.loadRecipesByCategory(category.getCategoryId());
                    });
                    recyclerCategories.setAdapter(categoryAdapter);
                }

                categoryAdapter.setSkeletonMode(false);
                categoryList.clear();
                categoryList.addAll(categories);
                categoryAdapter.notifyDataSetChanged();
            }
        });
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
        HomeFragmentDirections.ActionHomeFragmentToRecipeDetailFragment action = HomeFragmentDirections.actionHomeFragmentToRecipeDetailFragment(recipe.getRecipeId());
        Navigation.findNavController(requireView()).navigate(action);
    }

    private void animateRecycler(RecyclerView recyclerView) {
        if (recyclerView.getAdapter() != null && recyclerView.getItemAnimator() == null) {
            recyclerView.setLayoutAnimation(
                    AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_fade_slide_up)
            );
            recyclerView.scheduleLayoutAnimation();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        UserData user = SharedPrefHelper.getInstance(requireContext()).getUser();
        if (user != null && user.getUserId() > 0) {
            int userId = user.getUserId();
            savedListViewModel.reloadSavedRecipeData(userId);
            homeFeedViewModel.loadHomeFeed(userId);
        }
    }
}
