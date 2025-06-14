package com.diogo.cookup.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.diogo.cookup.R;

import java.io.File;
import java.util.List;

public class RecipeGalleryEditAdapter extends RecyclerView.Adapter<RecipeGalleryEditAdapter.GalleryViewHolder> {

    private List<Object> images;
    private OnRemoveClickListener onRemoveClickListener;
    private boolean removable = true;

    public interface OnRemoveClickListener {
        void onRemove(int position);
    }

    public RecipeGalleryEditAdapter(List<Object> images, OnRemoveClickListener onRemoveClickListener) {
        this.images = images;
        this.onRemoveClickListener = onRemoveClickListener;
    }

    public void updateList(List<Object> newList) {
        images = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gallery_thumbnail, parent, false);
        return new GalleryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {
        Object imgObj = images.get(position);

        if (imgObj instanceof File) {
            Glide.with(holder.imgPreview.getContext())
                    .load((File) imgObj)
                    .centerCrop()
                    .placeholder(R.drawable.bg_dialog_rounded)
                    .into(holder.imgPreview);
        } else if (imgObj instanceof String) {
            Glide.with(holder.imgPreview.getContext())
                    .load((String) imgObj)
                    .centerCrop()
                    .placeholder(R.drawable.bg_dialog_rounded)
                    .into(holder.imgPreview);
        }

        if (onRemoveClickListener != null) {
            holder.btnRemove.setVisibility(View.VISIBLE);
            holder.btnRemove.setOnClickListener(v -> {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) onRemoveClickListener.onRemove(pos);
            });
        } else {
            holder.btnRemove.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return images != null ? images.size() : 0;
    }

    public static class GalleryViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPreview;
        ImageButton btnRemove;

        public GalleryViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPreview = itemView.findViewById(R.id.img_gallery_preview);
            btnRemove = itemView.findViewById(R.id.btn_remove_gallery_image);
        }
    }
}