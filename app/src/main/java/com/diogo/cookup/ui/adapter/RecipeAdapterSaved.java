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

public class RecipeAdapterSaved extends RecyclerView.Adapter<RecipeAdapterSaved.ViewHolder> {

    private final List<RecipeData> recipeList;
    private final OnRemoveClickListener removeClickListener;
    private final OnRecipeClickListener recipeClickListener;

    public RecipeAdapterSaved(List<RecipeData> recipeList, OnRemoveClickListener removeClickListener, OnRecipeClickListener recipeClickListener) {
        this.recipeList = recipeList;
        this.removeClickListener = removeClickListener;
        this.recipeClickListener = recipeClickListener;
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
        RecipeData recipe = recipeList.get(position);
        holder.bind(recipe, position);
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public void updateData(List<RecipeData> newList) {
        recipeList.clear();
        recipeList.addAll(newList);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, preparationTime, servings;
        ImageView image;
        ImageButton buttonRemove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.recipe_title);
            preparationTime = itemView.findViewById(R.id.preparation_time);
            servings = itemView.findViewById(R.id.servings);
            image = itemView.findViewById(R.id.recipe_image);
            buttonRemove = itemView.findViewById(R.id.button_save_recipe);
        }

        public void bind(RecipeData recipe, int position) {
            title.setText(recipe.getTitle());

            if (recipe.getPreparationTime() > 0) {
                preparationTime.setText(recipe.getPreparationTime() + " min.");
            } else {
                preparationTime.setText("Tempo não disponível");
            }

            if (recipe.getServings() > 0) {
                servings.setText(recipe.getServings() + " doses");
            } else {
                servings.setText("Quantidade não disponível");
            }

            Glide.with(itemView.getContext())
                    .load(recipe.getImage())
                    .placeholder(R.drawable.placeholder)
                    .into(image);

            buttonRemove.setImageResource(R.drawable.ic_trash);
            buttonRemove.setOnClickListener(v -> {
                removeClickListener.onRemoveClick(recipe);
                recipeList.remove(position);
                notifyItemRemoved(position);
            });

            itemView.setOnClickListener(v -> {
                recipeClickListener.onRecipeClick(recipe);
            });
        }
    }

    public interface OnRemoveClickListener {
        void onRemoveClick(RecipeData recipe);
    }

    public interface OnRecipeClickListener {
        void onRecipeClick(RecipeData recipe);
    }
}
