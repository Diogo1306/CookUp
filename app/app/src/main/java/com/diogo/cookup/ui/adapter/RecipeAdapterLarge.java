package com.diogo.cookup.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private boolean isPaginating = false;

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
    }

    public void updateSavedIds(List<Integer> newSavedIds) {
        savedRecipeIdSet.clear();
        if (newSavedIds != null) savedRecipeIdSet.addAll(newSavedIds);
        notifyDataSetChanged();
    }

    public void setPaginating(boolean paginating) {
        this.isPaginating = paginating;
        notifyItemChanged(getItemCount() - 1);
    }

    public void setDataWithAnimation(List<RecipeData> newData) {
        int previousSize = recipeList.size();
        recipeList.clear();
        recipeList.addAll(newData);
        notifyItemRangeInserted(previousSize, newData.size());
        setSkeletonMode(false);
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
        if (skeletonMode && position >= recipeList.size()) return VIEW_TYPE_SKELETON;
        if (isPaginating && position == recipeList.size()) return VIEW_TYPE_SKELETON;
        return VIEW_TYPE_RECIPE;
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
        return skeletonMode ? recipeList.size() + SKELETON_COUNT : recipeList.size() + (isPaginating ? 1 : 0);
    }

    static class SkeletonViewHolder extends RecyclerView.ViewHolder {
        public SkeletonViewHolder(@NonNull View itemView) {
            super(itemView);
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
            if (recipeTitle != null) recipeTitle.setText(recipe.getTitle());
            if (recipeDescription != null) recipeDescription.setText(recipe.getDescription());
            if (preparationTime != null) preparationTime.setText(recipe.getPreparationTime() + " min");
            if (difficultyText != null) difficultyText.setText(recipe.getDifficulty() != null ? recipe.getDifficulty() : "");
            if (ratingText != null) ratingText.setText(recipe.getAverageRating() > 0
                    ? String.valueOf(recipe.getAverageRating())
                    : "—");

            if (recipeImage != null) {
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
            }

            if (saveButton != null) {
                boolean isSaved = savedRecipeIdSet.contains(recipe.getRecipeId());
                saveButton.setImageResource(isSaved ? R.drawable.ic_bookmark_selected : R.drawable.ic_bookmark);
                saveButton.setOnClickListener(v -> saveClickListener.onSaveClick(recipe.getRecipeId()));
            }

            itemView.setOnClickListener(v -> listener.onItemClick(recipe));
        }
    }
}