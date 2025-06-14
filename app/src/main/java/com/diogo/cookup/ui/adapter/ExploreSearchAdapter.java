package com.diogo.cookup.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.diogo.cookup.R;
import com.diogo.cookup.data.model.*;

import java.util.ArrayList;
import java.util.List;

public class ExploreSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_RECIPE = 1;
    public static final int TYPE_CATEGORY_RECIPE = 2;
    public static final int TYPE_INGREDIENT = 3;

    private final Context context;
    private final List<Object> items = new ArrayList<>();
    private final OnItemClickListener clickListener;

    public ExploreSearchAdapter(Context context, OnItemClickListener clickListener) {
        this.context = context;
        this.clickListener = clickListener;
    }

    public void setSuggestions(SearchData data) {
        items.clear();
        if (data.getRecipes() != null && !data.getRecipes().isEmpty()) {
            items.add("Receitas");
            items.addAll(data.getRecipes());
        }
        if (data.getRecipeCategories() != null && !data.getRecipeCategories().isEmpty()) {
            items.add("Categorias");
            items.addAll(data.getRecipeCategories());
        }
        if (data.getIngredients() != null && !data.getIngredients().isEmpty()) {
            items.add("Ingredientes");
            items.addAll(data.getIngredients());
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = items.get(position);
        if (item instanceof String) return TYPE_HEADER;
        if (item instanceof RecipeData) return TYPE_RECIPE;
        if (item instanceof CategoryData) return TYPE_CATEGORY_RECIPE;
        if (item instanceof IngredientData) return TYPE_INGREDIENT;
        return -1;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (viewType) {
            case TYPE_HEADER:
                return new HeaderViewHolder(inflater.inflate(R.layout.item_result_header, parent, false));
            case TYPE_RECIPE:
                return new RecipeViewHolder(inflater.inflate(R.layout.item_recipe_row, parent, false));
            case TYPE_CATEGORY_RECIPE:
                return new CategoryViewHolder(inflater.inflate(R.layout.item_suggest_category, parent, false));
            case TYPE_INGREDIENT:
                return new IngredientViewHolder(inflater.inflate(R.layout.item_suggest_ingredient, parent, false));
            default:
                throw new IllegalArgumentException("Tipo de view inválido: " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = items.get(position);

        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).textTitle.setText((String) item);
        } else if (holder instanceof RecipeViewHolder && item instanceof RecipeData) {
            ((RecipeViewHolder) holder).bind((RecipeData) item);
        } else if (holder instanceof CategoryViewHolder && item instanceof CategoryData) {
            ((CategoryViewHolder) holder).bind((CategoryData) item);
        } else if (holder instanceof IngredientViewHolder && item instanceof IngredientData) {
            ((IngredientViewHolder) holder).bind((IngredientData) item);
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle;
        HeaderViewHolder(View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
        }
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, subtitle, rating, difficulty;

        RecipeViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.recipe_image);
            title = itemView.findViewById(R.id.recipe_title);
            subtitle = itemView.findViewById(R.id.recipe_subtext);
            rating = itemView.findViewById(R.id.rating_text);
            difficulty = itemView.findViewById(R.id.difficulty_text);
        }

        void bind(RecipeData recipe) {
            title.setText(recipe.getTitle());
            subtitle.setText(recipe.getPreparationTime() + " min");
            rating.setText(recipe.getAverageRating() > 0 ? String.valueOf(recipe.getAverageRating()) : "—");
            difficulty.setText(recipe.getDifficulty() != null ? recipe.getDifficulty() : "—");

            Glide.with(context)
                    .load(recipe.getImage())
                    .placeholder(R.drawable.placeholder)
                    .into(image);

            itemView.setOnClickListener(v -> clickListener.onItemClick(recipe.getTitle(), recipe));
        }
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView label;

        CategoryViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imgCategoryIcon);
            label = itemView.findViewById(R.id.tvCategoryLabel);
        }

        void bind(CategoryData category) {
            label.setText(category.getCategoryName());
            Glide.with(context).load(category.getImageUrl()).placeholder(R.drawable.placeholder).into(image);
            itemView.setOnClickListener(v -> clickListener.onItemClick(category.getCategoryName(), null));
        }
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView label;

        IngredientViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.ingredient_image);
            label = itemView.findViewById(R.id.ingredient_name);
        }

        void bind(IngredientData ingredient) {
            label.setText(ingredient.getName());
            Glide.with(context).load(ingredient.getImage()).placeholder(R.drawable.placeholder).into(image);
            itemView.setOnClickListener(v -> clickListener.onItemClick(ingredient.getName(), null));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String selectedText, @Nullable RecipeData recipe);
    }
}
