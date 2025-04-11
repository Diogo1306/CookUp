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

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {
    private final List<RecipeData> recipeList;
    private final boolean isSavedMode;
    private OnItemClickListener onItemClickListener;
    private OnSaveClickListener onSaveClickListener;

    public RecipeAdapter(List<RecipeData> recipes, boolean isSavedMode) {
        this.recipeList = recipes;
        this.isSavedMode = isSavedMode;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setOnSaveClickListener(OnSaveClickListener listener) {
        this.onSaveClickListener = listener;
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
        holder.bind(recipe, position);
    }

    @Override
    public int getItemCount() {
        return recipeList != null ? recipeList.size() : 0;
    }

    public void updateData(List<RecipeData> newRecipes) {
        recipeList.clear();
        recipeList.addAll(newRecipes);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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

        public void bind(RecipeData recipe, int position) {
            title.setText(recipe.getTitle());

            int prepTime = recipe.getPreparationTime();
            preparationTime.setText(prepTime > 0 ? prepTime + " min." : "Tempo não disponível");

            int servingsCount = recipe.getServings();
            servings.setText(servingsCount > 0 ? servingsCount + " doses" : "Quantidade não disponível");

            Glide.with(itemView.getContext())
                    .load(recipe.getImage())
                    .placeholder(R.drawable.placeholder)
                    .into(image);

            if (isSavedMode) {
                buttonSave.setImageResource(R.drawable.ic_trash);
            } else {
                buttonSave.setImageResource(R.drawable.ic_bookmark_selected);
            }

            itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) onItemClickListener.onItemClick(recipe);
            });

            buttonSave.setOnClickListener(v -> {
                if (onSaveClickListener != null) {
                    onSaveClickListener.onSaveClick(recipe.getRecipeId());

                    if (isSavedMode) {
                        recipeList.remove(position);
                        notifyItemRemoved(position);
                    } else {
                        notifyItemChanged(position);
                    }
                }
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
