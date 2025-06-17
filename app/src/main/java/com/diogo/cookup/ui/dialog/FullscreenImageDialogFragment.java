package com.diogo.cookup.ui.dialog;

import android.os.Bundle;
import android.view.*;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.bumptech.glide.Glide;
import com.diogo.cookup.R;

public class FullscreenImageDialogFragment extends DialogFragment {
    private static final String ARG_IMAGE_URL = "image_url";

    public static FullscreenImageDialogFragment newInstance(String imageUrl) {
        FullscreenImageDialogFragment fragment = new FullscreenImageDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_IMAGE_URL, imageUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getTheme() {
        return R.style.FullScreenDialogTheme;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fullscreen_image, container, false);

        ImageView imageView = view.findViewById(R.id.fullscreen_image);
        String imageUrl = getArguments() != null ? getArguments().getString(ARG_IMAGE_URL) : null;

        if (imageUrl != null) {
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.skeleton_background)
                    .into(imageView);
        }

        imageView.setOnClickListener(v -> dismiss());
        view.setOnClickListener(v -> dismiss());

        return view;
    }
}
