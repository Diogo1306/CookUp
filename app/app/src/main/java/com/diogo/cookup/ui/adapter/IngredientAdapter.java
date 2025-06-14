package com.diogo.cookup.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.diogo.cookup.R;
import com.diogo.cookup.data.model.IngredientData;

import java.util.ArrayList;
import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private List<IngredientData> ingredients;
    private final Context context;

    public IngredientAdapter(List<IngredientData> ingredients, Context context) {
        this.ingredients = ingredients;
        this.context = context;
    }

    @NonNull
    @Override
    public IngredientAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ingredient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.ViewHolder holder, int position) {
        IngredientData ingredient = ingredients.get(position);

        holder.ingredientName.setText(ingredient.getName());

        if (ingredient.getQuantity() != null && !ingredient.getQuantity().isEmpty()) {
            holder.ingredientQuantity.setText(ingredient.getQuantity());
            holder.ingredientQuantity.setVisibility(View.VISIBLE);
        } else {
            holder.ingredientQuantity.setVisibility(View.GONE);
        }

        Glide.with(context)
                .load(ingredient.getImage())
                .placeholder(R.drawable.placeholder_ingredient)
                .into(holder.ingredientImage);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public void updateList(List<IngredientData> newIngredients) {
        this.ingredients.clear();
        if (newIngredients != null) {
            this.ingredients.addAll(newIngredients);
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView ingredientName, ingredientQuantity;
        ImageView ingredientImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientName = itemView.findViewById(R.id.ingredient_name);
            ingredientQuantity = itemView.findViewById(R.id.ingredient_quantity);
            ingredientImage = itemView.findViewById(R.id.ingredient_image);
        }
    }
}
