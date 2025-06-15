package com.diogo.cookup.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.diogo.cookup.R;
import com.diogo.cookup.data.model.RecipeData;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapterSavedInList extends ListAdapter<RecipeData, RecipeAdapterSavedInList.ViewHolder> {

    private final OnRemoveClickListener removeClickListener;
    private final OnItemClickListener itemClickListener;

    public interface OnRemoveClickListener {
        void onRemoveClick(RecipeData recipe);
    }

    public interface OnItemClickListener {
        void onItemClick(RecipeData recipe);
    }

    public RecipeAdapterSavedInList(OnRemoveClickListener removeClickListener,
                                    OnItemClickListener itemClickListener) {
        super(DIFF_CALLBACK);
        this.removeClickListener = removeClickListener;
        this.itemClickListener = itemClickListener;
    }

    private static final DiffUtil.ItemCallback<RecipeData> DIFF_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull RecipeData oldItem, @NonNull RecipeData newItem) {
            return oldItem.getRecipeId() == newItem.getRecipeId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull RecipeData oldItem, @NonNull RecipeData newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getImage().equals(newItem.getImage()) &&
                    oldItem.getAverageRating() == newItem.getAverageRating() &&
                    oldItem.getPreparationTime() == newItem.getPreparationTime();
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe_saved_onlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public void removeRecipe(RecipeData recipe) {
        List<RecipeData> currentList = new ArrayList<>(getCurrentList());
        currentList.remove(recipe);
        submitList(currentList);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, preparationTime, ratingText;
        ImageView image;
        ImageButton trashButton;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.recipe_title);
            preparationTime = itemView.findViewById(R.id.preparation_time);
            ratingText = itemView.findViewById(R.id.rating_text);
            image = itemView.findViewById(R.id.recipe_image);
            trashButton = itemView.findViewById(R.id.button_save_recipe); // ic_trash no layout
        }

        void bind(RecipeData recipe) {
            title.setText(recipe.getTitle());
            preparationTime.setText(recipe.getPreparationTime() + " min");
            ratingText.setText(String.valueOf(recipe.getAverageRating()));

            Glide.with(itemView.getContext())
                    .load(recipe.getImage())
                    .placeholder(R.drawable.placeholder)
                    .into(image);

            trashButton.setImageResource(R.drawable.ic_trash);
            trashButton.setOnClickListener(v -> removeClickListener.onRemoveClick(recipe));
            itemView.setOnClickListener(v -> itemClickListener.onItemClick(recipe));
        }
    }
}
