package com.diogo.cookup.ui.adapter;

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

public class RecipeAdapterDefault extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<RecipeData> recipeList;
    private final Set<Integer> savedRecipeIdSet;

    private final OnItemClickListener itemClickListener;
    private final OnSaveClickListener saveClickListener;

    private final int layoutResId;

    public RecipeAdapterDefault(List<RecipeData> recipeList, List<Integer> savedRecipeIds,
                                OnItemClickListener itemClickListener, OnSaveClickListener saveClickListener,
                                int layoutResId) {
        this.recipeList = recipeList != null ? recipeList : new ArrayList<>();
        this.savedRecipeIdSet = new HashSet<>(savedRecipeIds);
        this.itemClickListener = itemClickListener;
        this.saveClickListener = saveClickListener;
        this.layoutResId = layoutResId;
    }

    public void updateSavedIds(List<Integer> newSavedIds) {
        savedRecipeIdSet.clear();
        savedRecipeIdSet.addAll(newSavedIds);
        notifyDataSetChanged();
    }

    public void updateData(List<RecipeData> newRecipes, List<Integer> newSavedIds) {
        recipeList.clear();
        recipeList.addAll(newRecipes);
        savedRecipeIdSet.clear();
        savedRecipeIdSet.addAll(newSavedIds);
        notifyDataSetChanged();
    }

    public List<RecipeData> getCurrentRecipes() {
        return recipeList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecipeViewHolder && position < recipeList.size()) {
            ((RecipeViewHolder) holder).bind(recipeList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {

        TextView title, preparationTime, ratingText;
        ImageView image;
        ImageButton saveButton;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.recipe_title);
            preparationTime = itemView.findViewById(R.id.preparation_time);
            ratingText = itemView.findViewById(R.id.rating_text);
            image = itemView.findViewById(R.id.recipe_image);
            saveButton = itemView.findViewById(R.id.button_save_recipe);
        }

        public void bind(RecipeData recipe) {
            title.setBackground(null);
            preparationTime.setBackground(null);
            ratingText.setBackground(null);
            image.setBackground(null);

            title.setText(recipe.getTitle());
            preparationTime.setText(recipe.getPreparationTime() + " min");
            ratingText.setText(String.valueOf(recipe.getAverageRating()));

            Glide.with(itemView.getContext())
                    .load(recipe.getImage())
                    .placeholder(R.drawable.placeholder)
                    .into(image);

            boolean isSaved = savedRecipeIdSet.contains(recipe.getRecipeId());
            saveButton.setImageResource(isSaved ? R.drawable.ic_bookmark_selected : R.drawable.ic_bookmark);

            itemView.setOnClickListener(v -> itemClickListener.onItemClick(recipe));
            saveButton.setOnClickListener(v -> saveClickListener.onSaveClick(recipe.getRecipeId()));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(RecipeData recipe);
    }

    public interface OnSaveClickListener {
        void onSaveClick(int recipeId);
    }
}