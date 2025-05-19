package com.diogo.cookup.ui.adapter;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
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

public class RecipeAdapterDefault extends RecyclerView.Adapter<RecipeAdapterDefault.ViewHolder> {

    private final List<RecipeData> recipeList;
    private final Set<Integer> savedRecipeIdSet;
    private final OnItemClickListener itemClickListener;
    private final OnSaveClickListener saveClickListener;
    private boolean skeletonMode = true;

    public RecipeAdapterDefault(List<RecipeData> recipeList, List<Integer> savedRecipeIds, OnItemClickListener itemClickListener, OnSaveClickListener saveClickListener) {
        this.recipeList = recipeList != null ? recipeList : new ArrayList<>();
        this.savedRecipeIdSet = new HashSet<>(savedRecipeIds);
        this.itemClickListener = itemClickListener;
        this.saveClickListener = saveClickListener;
    }

    public void setSkeletonMode(boolean skeletonMode) {
        this.skeletonMode = skeletonMode;
        notifyDataSetChanged();
    }

    public boolean isSkeletonMode() {
        return skeletonMode;
    }

    public void updateData(List<RecipeData> newRecipes, List<Integer> newSavedIds) {
        recipeList.clear();
        recipeList.addAll(newRecipes);
        savedRecipeIdSet.clear();
        savedRecipeIdSet.addAll(newSavedIds);

        if (skeletonMode) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                skeletonMode = false;
                notifyItemRangeChanged(0, recipeList.size());
            }, 50);
        } else {
            notifyItemRangeChanged(0, recipeList.size());
        }
    }

    public void updateSavedIds(List<Integer> newSavedIds) {
        savedRecipeIdSet.clear();
        savedRecipeIdSet.addAll(newSavedIds);
        notifyDataSetChanged();
    }

    public void notifyRecipeChanged(int recipeId) {
        for (int i = 0; i < recipeList.size(); i++) {
            if (recipeList.get(i).getRecipeId() == recipeId) {
                notifyItemChanged(i);
                break;
            }
        }
    }

    public List<RecipeData> getCurrentRecipes() {
        return recipeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe_default, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (skeletonMode) {
            holder.bindSkeleton();
        } else {
            holder.bind(recipeList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return skeletonMode ? 5 : recipeList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, preparationTime, ratingText;
        ImageView image;
        ImageButton saveButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.recipe_title);
            preparationTime = itemView.findViewById(R.id.preparation_time);
            ratingText = itemView.findViewById(R.id.rating_text);
            image = itemView.findViewById(R.id.recipe_image);
            saveButton = itemView.findViewById(R.id.button_save_recipe);
        }

        public void bind(RecipeData recipe) {
            title.setVisibility(View.VISIBLE);
            preparationTime.setVisibility(View.VISIBLE);
            ratingText.setVisibility(View.VISIBLE);
            image.setVisibility(View.VISIBLE);
            saveButton.setVisibility(View.VISIBLE);

            title.setText(recipe.getTitle() != null ? recipe.getTitle() : "Sem título");

            preparationTime.setText(recipe.getPreparationTime() > 0
                    ? recipe.getPreparationTime() + " min"
                    : "Tempo não disponível");

            ratingText.setText(String.valueOf(recipe.getAverageRating()));

            Glide.with(itemView.getContext())
                    .load(recipe.getImage() != null ? recipe.getImage() : R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(image);

            boolean isSaved = savedRecipeIdSet.contains(recipe.getRecipeId());
            int icon = isSaved ? R.drawable.ic_bookmark_selected : R.drawable.ic_bookmark;
            saveButton.setImageResource(icon);
            saveButton.setTag(icon);

            itemView.setOnClickListener(v -> itemClickListener.onItemClick(recipe));
            saveButton.setOnClickListener(v -> saveClickListener.onSaveClick(recipe.getRecipeId()));
        }

        public void bindSkeleton() {
            title.setVisibility(View.INVISIBLE);
            preparationTime.setVisibility(View.INVISIBLE);
            ratingText.setVisibility(View.INVISIBLE);
            image.setImageResource(R.drawable.skeleton_background);
            saveButton.setVisibility(View.INVISIBLE);
        }
    }

    public void setData(List<RecipeData> newList) {
        recipeList.clear();
        recipeList.addAll(newList);
        skeletonMode = false;
        notifyDataSetChanged();
    }


    public void clearList() {
        recipeList.clear();
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(RecipeData recipe);
    }

    public interface OnSaveClickListener {
        void onSaveClick(int recipeId);
    }
}
