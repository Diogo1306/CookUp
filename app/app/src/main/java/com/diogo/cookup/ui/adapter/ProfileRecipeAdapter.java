package com.diogo.cookup.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.diogo.cookup.R;
import com.diogo.cookup.data.model.RecipeData;
import com.diogo.cookup.ui.fragment.ProfileFragment;

import java.util.List;

public class ProfileRecipeAdapter extends RecyclerView.Adapter<ProfileRecipeAdapter.ProfileRecipeViewHolder> {

    public interface OnRecipeActionListener {
        void onEdit(RecipeData recipe);
        void onDelete(RecipeData recipe);
        void onRecipeClick(RecipeData recipe);
    }

    private List<RecipeData> recipeList;
    private final OnRecipeActionListener actionListener;
    private final Context context;

    public ProfileRecipeAdapter(Context context, List<RecipeData> recipes, OnRecipeActionListener listener) {
        this.context = context;
        this.recipeList = recipes;
        this.actionListener = listener;
    }

    @NonNull
    @Override
    public ProfileRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recipe_user_management, parent, false);
        return new ProfileRecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileRecipeViewHolder holder, int position) {
        RecipeData recipe = recipeList.get(position);

        holder.recipeTitle.setText(recipe.getTitle());
        holder.recipeViews.setText(String.valueOf(recipe.getViewsCount()));
        holder.ratingText.setText(String.format("%.1f", recipe.getAverageRating()));

        Glide.with(context)
                .load(recipe.getImage())
                .placeholder(R.drawable.placeholder)
                .into(holder.recipeImage);

        holder.buttonEdit.setOnClickListener(v -> {
            if (actionListener != null) actionListener.onEdit(recipe);
        });
        holder.buttonDelete.setOnClickListener(v -> {
            if (actionListener != null) actionListener.onDelete(recipe);
        });
        holder.itemView.setOnClickListener(v -> {
            if (actionListener != null) actionListener.onRecipeClick(recipe);
        });
    }

    @Override
    public int getItemCount() {
        return recipeList != null ? recipeList.size() : 0;
    }

    public void setRecipeList(List<RecipeData> newList) {
        this.recipeList = newList;
        notifyDataSetChanged();
    }

    static class ProfileRecipeViewHolder extends RecyclerView.ViewHolder {
        ImageView recipeImage;
        TextView recipeTitle, recipeViews, ratingText;
        ImageButton buttonEdit, buttonDelete;

        public ProfileRecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeImage = itemView.findViewById(R.id.recipe_image);
            recipeTitle = itemView.findViewById(R.id.recipe_title);
            recipeViews = itemView.findViewById(R.id.recipe_views);
            ratingText = itemView.findViewById(R.id.rating_text);
            buttonEdit = itemView.findViewById(R.id.button_edit_recipe);
            buttonDelete = itemView.findViewById(R.id.button_delete_recipe);
        }
    }
}
