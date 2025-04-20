package com.diogo.cookup.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.diogo.cookup.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class RatingBottomSheet extends BottomSheetDialogFragment {

    public interface OnRatingSubmittedListener {
        void onRatingSubmitted(float rating, String comment);
    }

    private final OnRatingSubmittedListener listener;

    public RatingBottomSheet(OnRatingSubmittedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_rating, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RatingBar ratingBar = view.findViewById(R.id.rating_bar);
        EditText commentInput = view.findViewById(R.id.input_comment);
        Button submitBtn = view.findViewById(R.id.btn_submit_rating);

        submitBtn.setOnClickListener(v -> {
            float rating = ratingBar.getRating();
            String comment = commentInput.getText().toString().trim();
            if (listener != null) {
                listener.onRatingSubmitted(rating, comment);
                dismiss();
            }
        });
    }
}
