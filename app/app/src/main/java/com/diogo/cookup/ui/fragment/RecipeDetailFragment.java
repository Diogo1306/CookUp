package com.diogo.cookup.ui.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.*;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.diogo.cookup.R;
import com.diogo.cookup.data.model.RecipeData;
import com.diogo.cookup.data.model.IngredientData;
import com.diogo.cookup.ui.adapter.IngredientAdapter;
import com.diogo.cookup.viewmodel.RecipeViewModel;

import java.util.List;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d("RECIPE_DETAIL", "onViewCreated() foi chamado!");

        if (getArguments() != null && getArguments().containsKey(ARG_RECIPE_ID)) {
            recipeId = getArguments().getInt(ARG_RECIPE_ID);
            Log.d("RECIPE_DETAIL", "Recebido recipeId: " + recipeId);
        } else {
            Log.e("RECIPE_DETAIL", "Argumento 'recipe_id' não encontrado!");
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
        RecyclerView ingredientsRecyclerView = view.findViewById(R.id.ingredients_recycler_view);
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel.getRecipeDetailLiveData().observe(getViewLifecycleOwner(), recipe -> {
            if (recipe == null) {
                Log.e("RECIPE_DETAIL", "Receita retornada nula do ViewModel");
                return;
            }

            Log.d("RECIPE_DETAIL", "Receita carregada: " + recipe.getTitle());

            Glide.with(requireContext())
                    .load(recipe.getImage())
                    .placeholder(R.drawable.placeholder)
                    .into(recipeImage);

            recipeTitle.setText(recipe.getTitle());
            txtTime.setText(recipe.getPreparationTime() + " mins");
            txtRating.setText("4.9");
            txtDifficulty.setText(recipe.getDifficulty());

            String fullDescription = recipe.getDescription();
            String introduction = recipe.getInstructions();

            recipeInstructions.setText(introduction);
            recipeInstructions.setVisibility(View.VISIBLE);

            if (fullDescription != null && !fullDescription.isEmpty()) {
                recipeDescription.setMaxLines(2);
                recipeDescription.setEllipsize(TextUtils.TruncateAt.END);
                recipeDescription.setText(fullDescription);

                recipeDescription.post(() -> {
                    Layout layout = recipeDescription.getLayout();
                    if (layout == null || layout.getLineCount() < 2) return;

                    int lineEndIndex = layout.getLineEnd(1);
                    if (lineEndIndex >= fullDescription.length()) return;

                    String trimmed = fullDescription.substring(0, lineEndIndex).trim();
                    String suffix = "... Ver mais";
                    String finalText = trimmed + suffix;

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
        });

        viewModel.loadRecipeDetail(recipeId);
    }
}
