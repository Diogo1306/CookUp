package com.diogo.cookup.ui.fragment;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.*;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.diogo.cookup.R;
import com.diogo.cookup.data.model.RecipeCategoryData;
import com.diogo.cookup.data.model.CommentData;
import com.diogo.cookup.data.model.IngredientData;
import com.diogo.cookup.data.model.TrackRequest;
import com.diogo.cookup.data.repository.TrackingRepository;
import com.diogo.cookup.ui.activity.MainActivity;
import com.diogo.cookup.ui.adapter.CommentAdapter;
import com.diogo.cookup.ui.adapter.IngredientAdapter;
import com.diogo.cookup.ui.dialog.RatingBottomSheet;
import com.diogo.cookup.utils.SharedPrefHelper;
import com.diogo.cookup.viewmodel.RecipeViewModel;
import com.diogo.cookup.viewmodel.SavedListViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;
import java.util.Locale;

public class RecipeDetailFragment extends Fragment {

    private static final String ARG_RECIPE_ID = "recipe_id";
    private int recipeId = -1;
    private RecipeViewModel viewModel;
    private IngredientAdapter ingredientAdapter;
    private RecyclerView ingredientsRecyclerView;

    public static RecipeDetailFragment newInstance(int recipeId) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_RECIPE_ID, recipeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setBottomNavVisibility(false);
        }

        if (getArguments() != null && getArguments().containsKey(ARG_RECIPE_ID)) {
            recipeId = getArguments().getInt(ARG_RECIPE_ID);
        } else {
            return;
        }

        viewModel = new ViewModelProvider(this).get(RecipeViewModel.class);

        ImageView recipeImage = view.findViewById(R.id.recipe_image);
        TextView recipeTitle = view.findViewById(R.id.recipe_title);
        TextView recipeDescription = view.findViewById(R.id.recipe_description);
        TextView recipeInstructions = view.findViewById(R.id.recipe_instructions);
        TextView txtTime = view.findViewById(R.id.txt_time);
        TextView txtRating = view.findViewById(R.id.txt_rating);
        TextView txtDifficulty = view.findViewById(R.id.txt_difficulty);
        ChipGroup chipGroup = view.findViewById(R.id.chip_group_categories);
        ImageButton btnBack = view.findViewById(R.id.arrow_back);
        ImageButton btnFavorite = view.findViewById(R.id.button_favorite);

        Button ratingButton = view.findViewById(R.id.btn_finish_recipe);
        EditText inputComment = view.findViewById(R.id.input_comment_direct);
        Button btnSendComment = view.findViewById(R.id.btn_send_comment);

        ingredientsRecyclerView = view.findViewById(R.id.ingredients_recycler_view);
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ingredientsRecyclerView.setNestedScrollingEnabled(false);

        btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        RecyclerView commentsRecyclerView = view.findViewById(R.id.comments_recycler_view);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        btnSendComment.setOnClickListener(v -> {
            String comment = inputComment.getText().toString().trim();
            if (!comment.isEmpty()) {
                int userId = SharedPrefHelper.getInstance(requireContext()).getUser().getUserId();
                viewModel.submitRatingAndComment(userId, recipeId, 0, comment);
            } else {
                Toast.makeText(requireContext(), "Escreve algo antes de comentar", Toast.LENGTH_SHORT).show();
            }
        });

        ratingButton.setOnClickListener(v -> {
            RatingBottomSheet bottomSheet = new RatingBottomSheet((rating, comment) -> {
                int userId = SharedPrefHelper.getInstance(requireContext()).getUser().getUserId();
                viewModel.submitRatingAndComment(userId, recipeId, (int) rating, comment);

                TrackingRepository trackingRepository = new TrackingRepository();
                trackingRepository.sendTracking(new TrackRequest(userId, recipeId, "finish"), new MutableLiveData<>());

                Toast toast = Toast.makeText(requireContext(), "Receita finalizada com sucesso!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 100);
                toast.show();
            });
            bottomSheet.show(getParentFragmentManager(), "ratingBottomSheet");
        });

        viewModel.getRecipeDetailLiveData().observe(getViewLifecycleOwner(), recipe -> {
            if (recipe == null) return;

            Glide.with(requireContext()).load(recipe.getImage())
                    .placeholder(R.drawable.placeholder)
                    .into(recipeImage);

            recipeTitle.setText(recipe.getTitle());
            txtTime.setText(getString(R.string.preparation_time_minutes, recipe.getPreparationTime()));
            txtDifficulty.setText(recipe.getDifficulty());

            float average = recipe.getAverageRating();
            txtRating.setText(average > 0 ? String.format(Locale.getDefault(), "%.1f", average) : "—");

            chipGroup.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(requireContext());
            List<RecipeCategoryData> categories = recipe.getCategories();
            if (categories != null && !categories.isEmpty()) {
                for (RecipeCategoryData category : categories) {
                    Chip chip = (Chip) inflater.inflate(R.layout.item_category_tag, chipGroup, false);
                    chip.setText(category.getCategoryName());

                    String hexColor = category.getColor() != null ? category.getColor() : "#EEEEEE";
                    try {
                        chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(hexColor)));
                    } catch (IllegalArgumentException e) {
                        chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#EEEEEE")));
                    }
                    chip.setTextColor(Color.WHITE);
                    chip.setCloseIconVisible(false);
                    chip.setCloseIcon(null);
                    chipGroup.addView(chip);
                }
            }

            List<IngredientData> ingredients = recipe.getIngredients();
            if (ingredients != null && !ingredients.isEmpty()) {
                ingredientAdapter = new IngredientAdapter(ingredients, requireContext());
                ingredientsRecyclerView.setAdapter(ingredientAdapter);

                ingredientsRecyclerView.post(() -> {
                    ingredientAdapter.notifyDataSetChanged();
                    ingredientsRecyclerView.getLayoutParams().height = ingredientAdapter.getItemCount() * dpToPx(60);
                    ingredientsRecyclerView.requestLayout();
                });
            }

            recipeInstructions.setText(recipe.getInstructions());
            recipeInstructions.setVisibility(View.VISIBLE);

            String fullDescription = recipe.getDescription();
            if (fullDescription != null && !fullDescription.isEmpty()) {
                recipeDescription.setText(fullDescription);
                recipeDescription.setMaxLines(Integer.MAX_VALUE);
                recipeDescription.setEllipsize(null);

                recipeDescription.post(() -> {
                    Layout layout = recipeDescription.getLayout();
                    if (layout == null || layout.getLineCount() <= 2) return;

                    int lineEndIndex = layout.getLineEnd(1);
                    String visibleText = fullDescription.substring(0, Math.min(lineEndIndex, fullDescription.length())).trim();
                    String suffix = "... Ver mais";

                    while ((visibleText + suffix).length() > lineEndIndex && visibleText.length() > 0) {
                        visibleText = visibleText.substring(0, visibleText.length() - 1);
                    }

                    String finalText = visibleText + suffix;
                    SpannableString spannable = new SpannableString(finalText);
                    int start = finalText.indexOf("Ver mais");

                    spannable.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(@NonNull View widget) {
                            recipeDescription.setText(fullDescription);
                            recipeDescription.setMaxLines(Integer.MAX_VALUE);
                            recipeDescription.setEllipsize(null);
                            recipeDescription.setMovementMethod(null);
                        }
                    }, start, finalText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    spannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.primary)),
                            start, finalText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannable.setSpan(new StyleSpan(Typeface.BOLD),
                            start, finalText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    recipeDescription.setText(spannable);
                    recipeDescription.setMovementMethod(LinkMovementMethod.getInstance());
                    recipeDescription.setHighlightColor(Color.TRANSPARENT);
                });
            }

            List<CommentData> comments = recipe.getComments();
            if (comments != null && !comments.isEmpty()) {
                CommentAdapter commentAdapter = new CommentAdapter(comments);
                commentsRecyclerView.setAdapter(commentAdapter);
            }
        });

        SavedListViewModel savedListViewModel = new ViewModelProvider(requireActivity()).get(SavedListViewModel.class);
        LiveData<List<Integer>> savedIdsLiveData = savedListViewModel.getSavedRecipeIds();

        savedIdsLiveData.observe(getViewLifecycleOwner(), savedIds -> {
            if (savedIds != null) {
                boolean isSaved = savedIds.contains(recipeId);
                btnFavorite.setImageResource(isSaved ? R.drawable.ic_bookmark_selected : R.drawable.ic_bookmark);
            }
        });

        btnFavorite.setOnClickListener(v -> {
            SaveRecipeBottomSheet.newInstance(recipeId, this)
                    .show(requireActivity().getSupportFragmentManager(), "save_sheet");
        });

        viewModel.getCommentsLiveData().observe(getViewLifecycleOwner(), comments -> {
            if (comments != null && !comments.isEmpty()) {
                CommentAdapter commentAdapter = new CommentAdapter(comments);
                commentsRecyclerView.setAdapter(commentAdapter);
            }
        });

        viewModel.getRatingSuccessLiveData().observe(getViewLifecycleOwner(), success -> {
            if (success != null && success) {
                inputComment.setText("");
                viewModel.loadComments(recipeId);
                viewModel.loadAverageRating(recipeId);
            }
        });

        viewModel.loadRecipeDetail(recipeId);
        viewModel.loadComments(recipeId);
        viewModel.loadAverageRating(recipeId);

        if (SharedPrefHelper.getInstance(requireContext()).getUser() != null) {
            int userId = SharedPrefHelper.getInstance(requireContext()).getUser().getUserId();
            TrackingRepository trackingRepository = new TrackingRepository();
            trackingRepository.sendTracking(new TrackRequest(userId, recipeId, "view"), new MutableLiveData<>());
        }
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setBottomNavVisibility(true);
        }
    }
}
