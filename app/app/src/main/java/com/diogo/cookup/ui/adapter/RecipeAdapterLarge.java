package com.diogo.cookup.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.*;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.diogo.cookup.R;
import com.diogo.cookup.data.model.RecipeData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecipeAdapterLarge extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<RecipeData> recipeList = new ArrayList<>();
    private final Set<Integer> savedRecipeIdSet = new HashSet<>();
    private final OnItemClickListener listener;
    private final OnSaveClickListener saveClickListener;
    private final Context context;

    private static final int VIEW_TYPE_RECIPE = 0;
    private static final int VIEW_TYPE_SKELETON = 1;
    private static final int SKELETON_COUNT = 6;

    private boolean skeletonMode = true;

    public interface OnItemClickListener {
        void onItemClick(RecipeData recipe);
    }

    public interface OnSaveClickListener {
        void onSaveClick(int recipeId);
    }

    public RecipeAdapterLarge(Context context, OnItemClickListener listener, OnSaveClickListener saveClickListener) {
        this.context = context;
        this.listener = listener;
        this.saveClickListener = saveClickListener;
        this.skeletonMode = true;
    }

    public void updateSavedIds(List<Integer> newSavedIds) {
        savedRecipeIdSet.clear();
        if (newSavedIds != null) savedRecipeIdSet.addAll(newSavedIds);
        notifyDataSetChanged();
    }

    public void setSkeletonMode(boolean enabled) {
        skeletonMode = enabled;
        notifyDataSetChanged();
    }

    public boolean isSkeletonMode() {
        return skeletonMode;
    }

    public void setData(List<RecipeData> newData) {
        recipeList.clear();
        recipeList.addAll(newData);
        setSkeletonMode(false);
    }

    public void appendData(List<RecipeData> newData) {
        int start = recipeList.size();
        recipeList.addAll(newData);
        notifyItemRangeInserted(start, newData.size());
    }

    @Override
    public int getItemViewType(int position) {
        return (skeletonMode && position >= recipeList.size())
                ? VIEW_TYPE_SKELETON
                : VIEW_TYPE_RECIPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SKELETON) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_recipe_large_skeleton, parent, false);
            return new SkeletonViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_recipe_large, parent, false);
            return new RecipeViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecipeViewHolder && position < recipeList.size()) {
            ((RecipeViewHolder) holder).bind(recipeList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return skeletonMode ? recipeList.size() + SKELETON_COUNT : recipeList.size();
    }

    static class SkeletonViewHolder extends RecyclerView.ViewHolder {
        public SkeletonViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d("SKELETON", "SkeletonViewHolder criado");
        }
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {
        ImageView recipeImage;
        TextView recipeTitle, preparationTime, difficultyText, recipeDescription, ratingText;
        ImageButton saveButton;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeImage = itemView.findViewById(R.id.recipe_image);
            recipeTitle = itemView.findViewById(R.id.recipe_title);
            preparationTime = itemView.findViewById(R.id.preparation_time);
            difficultyText = itemView.findViewById(R.id.difficulty_text);
            recipeDescription = itemView.findViewById(R.id.recipe_description);
            ratingText = itemView.findViewById(R.id.rating_text);
            saveButton = itemView.findViewById(R.id.button_save_recipe);
        }

        public void bind(RecipeData recipe) {
            recipeTitle.setText(recipe.getTitle());
            recipeDescription.setText(recipe.getDescription());
            preparationTime.setText(recipe.getPreparationTime() + " min");
            difficultyText.setText(recipe.getDifficulty() != null ? recipe.getDifficulty() : "");
            ratingText.setText(recipe.getAverageRating() > 0
                    ? String.valueOf(recipe.getAverageRating())
                    : "—");

            String imageUrl = recipe.getImage();
            if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                Glide.with(context)
                        .load(imageUrl)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(recipeImage);
            } else {
                recipeImage.setImageResource(R.drawable.placeholder);
            }

            boolean isSaved = savedRecipeIdSet.contains(recipe.getRecipeId());
            saveButton.setImageResource(isSaved ? R.drawable.ic_bookmark_selected : R.drawable.ic_bookmark);

            itemView.setOnClickListener(v -> listener.onItemClick(recipe));
            saveButton.setOnClickListener(v -> saveClickListener.onSaveClick(recipe.getRecipeId()));
        }
    }
}
