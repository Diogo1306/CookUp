package com.diogo.cookup.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.diogo.cookup.R;

import java.util.ArrayList;
import java.util.List;

public class GalleryPagerAdapter extends RecyclerView.Adapter<GalleryPagerAdapter.GalleryViewHolder> {
    private List<String> images;

    public GalleryPagerAdapter(List<String> images) {
        this.images = images != null ? images : new ArrayList<>();
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gallery_image, parent, false);
        return new GalleryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {
        String imageUrl = images.get(position);
        Glide.with(holder.imgPreview.getContext())
                .load(imageUrl)
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .into(holder.imgPreview);
    }

    @Override
    public int getItemCount() {
        return images != null ? images.size() : 0;
    }

    public void updateList(List<String> newImages) {
        this.images = newImages != null ? newImages : new ArrayList<>();
        notifyDataSetChanged();
    }

    static class GalleryViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPreview;

        GalleryViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPreview = itemView.findViewById(R.id.img_gallery_preview);
        }
    }
}
