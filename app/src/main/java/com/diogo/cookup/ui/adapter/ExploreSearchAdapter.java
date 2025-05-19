package com.diogo.cookup.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.diogo.cookup.R;
import com.diogo.cookup.data.model.*;

import java.util.ArrayList;
import java.util.List;

public class ExploreSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_RECIPE = 1;
    private static final int TYPE_CATEGORY_RECIPE = 2;
    private static final int TYPE_INGREDIENT = 3;

    private final Context context;
    private final List<Object> items = new ArrayList<>();
    private final OnItemClickListener clickListener;
    private boolean showOnlyRecipes = false;

    public ExploreSearchAdapter(Context context, OnItemClickListener clickListener) {
        this.context = context;
        this.clickListener = clickListener;
    }

    public void setSuggestions(SearchData data) {
        showOnlyRecipes = false;
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

    public void setRecipes(List<RecipeData> recipes) {
        showOnlyRecipes = true;
        items.clear();
        items.addAll(recipes);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = items.get(position);
        if (!showOnlyRecipes) {
            if (item instanceof String) return TYPE_HEADER;
            if (item instanceof RecipeData) return TYPE_RECIPE;
            if (item instanceof RecipeCategoryData) return TYPE_CATEGORY_RECIPE;
            if (item instanceof IngredientData) return TYPE_INGREDIENT;
        }
        return TYPE_RECIPE;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == TYPE_HEADER) {
            View view = inflater.inflate(R.layout.item_result_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_result_row, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = items.get(position);

        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).textTitle.setText((String) item);
        } else if (holder instanceof ItemViewHolder) {
            TextView label = ((ItemViewHolder) holder).textItem;
            String displayText = "";

            if (item instanceof RecipeData) {
                displayText = ((RecipeData) item).getTitle();
                label.setText("🍽️ " + displayText);
            } else if (item instanceof RecipeCategoryData) {
                displayText = ((RecipeCategoryData) item).getCategoryName();
                label.setText("🏷️ " + displayText);
            } else if (item instanceof IngredientData) {
                displayText = ((IngredientData) item).getName();
                label.setText("🥕 " + displayText);
            }

            String finalDisplayText = displayText;
            holder.itemView.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onItemClick(finalDisplayText, item instanceof RecipeData ? (RecipeData) item : null);
                }
            });
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle;
        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView textItem;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textItem = itemView.findViewById(R.id.textItem);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String selectedText, @Nullable RecipeData fullRecipe);
    }
}
