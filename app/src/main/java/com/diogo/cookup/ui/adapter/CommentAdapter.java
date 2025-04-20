package com.diogo.cookup.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.diogo.cookup.R;
import com.diogo.cookup.data.model.CommentData;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<CommentData> commentList;

    public CommentAdapter(List<CommentData> commentList) {
        this.commentList = commentList;
    }

    public void setComments(List<CommentData> newComments) {
        this.commentList = newComments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        CommentData comment = commentList.get(position);
        holder.txtUserName.setText(comment.getName());
        holder.txtComment.setText(comment.getComment());
        holder.txtDate.setText(comment.getCreatedAt());

        Glide.with(holder.itemView.getContext())
                .load(comment.getImage())
                .placeholder(R.drawable.default_profile)
                .into(holder.imgProfile);

        if (comment.getRating() > 0) {
            holder.ratingBar.setVisibility(View.VISIBLE);
            holder.ratingBar.setRating(comment.getRating());
        } else {
            holder.ratingBar.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProfile;
        TextView txtUserName, txtComment, txtDate;
        RatingBar ratingBar;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProfile = itemView.findViewById(R.id.img_profile);
            txtUserName = itemView.findViewById(R.id.txt_user_name);
            txtComment = itemView.findViewById(R.id.txt_comment);
            txtDate = itemView.findViewById(R.id.txt_date);
            ratingBar = itemView.findViewById(R.id.rating_bar);
        }
    }
}
