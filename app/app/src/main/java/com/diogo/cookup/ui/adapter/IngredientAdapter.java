package com.diogo.cookup.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diogo.cookup.R;
import com.diogo.cookup.data.model.IngredientData;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private final List<IngredientData> ingredients;

    public IngredientAdapter(List<IngredientData> ingredients) {
        this.ingredients = ingredients;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView ingredientName;

        public ViewHolder(View view) {
            super(view);
            ingredientName = view.findViewById(R.id.ingredient_name);
        }
    }

    @NonNull
    @Override
    public IngredientAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.ViewHolder holder, int position) {
        IngredientData ingredient = ingredients.get(position);
        holder.ingredientName.setText(ingredient.getFull_entry());
    }

    @Override
    public int getItemCount() {
        return ingredients != null ? ingredients.size() : 0;
    }
}
