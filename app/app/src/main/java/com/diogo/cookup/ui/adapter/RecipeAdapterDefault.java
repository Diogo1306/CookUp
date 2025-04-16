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

import java.util.List;

public class RecipeAdapterDefault extends RecyclerView.Adapter<RecipeAdapterDefault.ViewHolder> {
    private final List<RecipeData> recipeList;
    private final OnItemClickListener itemClickListener;
    private final OnSaveClickListener saveClickListener;
    private final List<Integer> savedRecipeIds;

    public RecipeAdapterDefault(List<RecipeData> recipeList, List<Integer> savedRecipeIds, OnItemClickListener itemClickListener, OnSaveClickListener saveClickListener) {
        this.recipeList = recipeList;
        this.itemClickListener = itemClickListener;
        this.saveClickListener = saveClickListener;
        this.savedRecipeIds = savedRecipeIds;
    }

    public void updateData(List<RecipeData> newRecipes, List<Integer> newSavedIds) {
        recipeList.clear();
        recipeList.addAll(newRecipes);
        savedRecipeIds.clear();
        savedRecipeIds.addAll(newSavedIds);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe_default, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecipeData recipe = recipeList.get(position);
        holder.bind(recipe);
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title, preparationTime, servings;
        private final ImageView image;
        private final ImageButton buttonSave;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.recipe_title);
            preparationTime = itemView.findViewById(R.id.preparation_time);
            servings = itemView.findViewById(R.id.servings);
            image = itemView.findViewById(R.id.recipe_image);
            buttonSave = itemView.findViewById(R.id.button_save_recipe);
        }

        public void bind(RecipeData recipe) {
            title.setText(recipe.getTitle());
            preparationTime.setText(recipe.getPreparationTime() > 0 ? recipe.getPreparationTime() + " min." : "Tempo não disponível");
            servings.setText(recipe.getServings() > 0 ? String.valueOf(recipe.getServings()) : "Quantidade não disponível");

            Glide.with(itemView.getContext())
                    .load(recipe.getImage())
                    .placeholder(R.drawable.placeholder)
                    .into(image);

            itemView.setOnClickListener(v -> itemClickListener.onItemClick(recipe));

            boolean isSaved = savedRecipeIds.contains(recipe.getRecipeId());

            buttonSave.setImageResource(isSaved ? R.drawable.ic_bookmark_selected : R.drawable.ic_bookmark);

            buttonSave.setOnClickListener(v -> {
                saveClickListener.onSaveClick(recipe.getRecipeId());
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(RecipeData recipe);
    }

    public interface OnSaveClickListener {
        void onSaveClick(int recipeId);
    }
}
