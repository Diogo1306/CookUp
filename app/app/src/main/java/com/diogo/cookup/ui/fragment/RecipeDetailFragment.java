package com.diogo.cookup.ui.fragment;

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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.diogo.cookup.R;
import com.diogo.cookup.data.model.CommentData;
import com.diogo.cookup.data.model.IngredientData;
import com.diogo.cookup.ui.adapter.CommentAdapter;
import com.diogo.cookup.ui.adapter.IngredientAdapter;
import com.diogo.cookup.ui.dialog.RatingBottomSheet;
import com.diogo.cookup.utils.SharedPrefHelper;
import com.diogo.cookup.viewmodel.RecipeViewModel;

import java.util.List;
import java.util.Locale;

public class RecipeDetailFragment extends Fragment {

    private static final String ARG_RECIPE_ID = "recipe_id";
    private int recipeId = -1;
    private RecipeViewModel viewModel;

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

        Button ratingButton = view.findViewById(R.id.btn_finish_recipe);
        EditText inputComment = view.findViewById(R.id.input_comment_direct);
        Button btnSendComment = view.findViewById(R.id.btn_send_comment);

        RecyclerView ingredientsRecyclerView = view.findViewById(R.id.ingredients_recycler_view);
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        RecyclerView commentsRecyclerView = view.findViewById(R.id.comments_recycler_view);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Clique no botão de comentar diretamente
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

            List<IngredientData> ingredients = recipe.getIngredients();
            if (ingredients != null && !ingredients.isEmpty()) {
                IngredientAdapter adapter = new IngredientAdapter(ingredients);
                ingredientsRecyclerView.setAdapter(adapter);
            }

            List<CommentData> comments = recipe.getComments();
            if (comments != null && !comments.isEmpty()) {
                CommentAdapter commentAdapter = new CommentAdapter(comments);
                commentsRecyclerView.setAdapter(commentAdapter);
            }
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
    }
}
