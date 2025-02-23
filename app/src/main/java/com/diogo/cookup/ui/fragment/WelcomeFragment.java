package com.diogo.cookup.ui.fragment;

import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.diogo.cookup.R;
import com.diogo.cookup.ui.activity.LoginActivity;
import com.diogo.cookup.ui.activity.SignupActivity;

import java.io.IOException;

public class WelcomeFragment extends Fragment implements TextureView.SurfaceTextureListener {

    private TextureView textureView;
    private MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);

        hideSystemUI();

        textureView = view.findViewById(R.id.video_texture);
        textureView.setSurfaceTextureListener(this);

        Button btnSignup = view.findViewById(R.id.signup_button);
        TextView btnLogin = view.findViewById(R.id.login_button);

        btnSignup.setOnClickListener(v -> startActivity(new Intent(requireActivity(), SignupActivity.class)));

        btnLogin.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction().remove(WelcomeFragment.this).commit();
            restoreSystemUI();
        });

        return view;
    }

    private void hideSystemUI() {
        requireActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    private void restoreSystemUI() {
        requireActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_VISIBLE
        );
    }

    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
        mediaPlayer = new MediaPlayer();
        try {
            Uri videoUri = Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + R.raw.video_welcome);
            mediaPlayer.setDataSource(requireContext(), videoUri);
            Surface videoSurface = new Surface(surface);
            mediaPlayer.setSurface(videoSurface);
            mediaPlayer.setLooping(true);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(mp -> {
                applyFullScreenZoom();
                mediaPlayer.start();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {
        applyFullScreenZoom();
    }

    @Override
    public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {
    }

    private void applyFullScreenZoom() {
        if (textureView == null || mediaPlayer == null) return;

        int videoWidth = mediaPlayer.getVideoWidth();
        int videoHeight = mediaPlayer.getVideoHeight();
        if (videoWidth == 0 || videoHeight == 0) return;

        int screenWidth = textureView.getWidth();
        int screenHeight = textureView.getHeight();

        float videoRatio = (float) videoWidth / videoHeight;
        float screenRatio = (float) screenWidth / screenHeight;

        float scaleX, scaleY;

        if (videoRatio > screenRatio) {
            scaleX = screenHeight / (float) videoHeight;
            scaleY = scaleX;
        } else {
            scaleY = screenWidth / (float) videoWidth;
            scaleX = scaleY;
        }

        float zoomFactor = 1.7f;
        scaleX *= zoomFactor;
        scaleY *= zoomFactor;

        Matrix matrix = new Matrix();
        matrix.setScale(scaleX, scaleY, screenWidth / 2f, screenHeight / 2f);
        textureView.setTransform(matrix);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        restoreSystemUI();

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
