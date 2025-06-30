package com.diogo.cookup.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.diogo.cookup.R;
import com.diogo.cookup.data.model.UserData;
import com.diogo.cookup.utils.MessageUtils;
import com.diogo.cookup.utils.SharedPrefHelper;
import com.diogo.cookup.viewmodel.UserViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SettingsProfileFragment extends Fragment {

    private ImageView imgProfile;
    private ImageButton arrowBack, btnEditPhoto;
    private EditText editName;
    private Button btnSave;
    private Uri selectedImageUri = null;

    private UserViewModel userViewModel;
    private UserData currentUser;

    private final ActivityResultLauncher<String> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    Glide.with(this).load(selectedImageUri).into(imgProfile);
                }
            });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_profile, container, false);

        imgProfile = view.findViewById(R.id.img_profile);
        editName = view.findViewById(R.id.edit_name);
        btnSave = view.findViewById(R.id.btn_save);
        btnEditPhoto = view.findViewById(R.id.btn_edit_photo);
        arrowBack = view.findViewById(R.id.arrow_back);

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        userViewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
            loadUserToUI(user);
        });

        userViewModel.getSuccessMessageLiveData().observe(getViewLifecycleOwner(), message -> {
            if (message != null && !message.isEmpty()) {
                MessageUtils.showSnackbar(requireView(), getString(R.string.profile_saved_success));
                NavHostFragment.findNavController(SettingsProfileFragment.this).navigateUp();
            }
        });

        UserData user = SharedPrefHelper.getInstance(requireContext()).getUser();
        if (user != null) {
            currentUser = user;
            loadUserToUI(user);
        }

        imgProfile.setOnClickListener(v -> pickImageLauncher.launch("image/*"));
        btnEditPhoto.setOnClickListener(v -> pickImageLauncher.launch("image/*"));

        btnSave.setOnClickListener(v -> saveProfile());

        arrowBack.setOnClickListener(v -> NavHostFragment.findNavController(SettingsProfileFragment.this).navigateUp());

        return view;
    }

    private void loadUserToUI(UserData user) {
        if (user == null) return;
        editName.setText(user.getUsername() != null ? user.getUsername() : "");
        if (user.getProfilePicture() != null && !user.getProfilePicture().isEmpty()) {
            Glide.with(this).load(user.getProfilePicture()).placeholder(R.drawable.placeholder).into(imgProfile);
        } else {
            imgProfile.setImageResource(R.drawable.placeholder);
        }
    }

    private void saveProfile() {
        String newName = editName.getText().toString().trim();

        if (newName.isEmpty()) {
            MessageUtils.showSnackbar(requireView(), getString(R.string.enter_name));
            return;
        }

        if (currentUser == null) return;

        if (selectedImageUri != null) {
            try {
                File imageFile = getFileFromUri(requireContext(), selectedImageUri);
                userViewModel.updateUserWithImageFile(currentUser.getUserId(), newName, imageFile);
            } catch (IOException e) {
                MessageUtils.showSnackbar(requireView(), getString(R.string.image_processing_error));
                e.printStackTrace();
            }
        } else {
            UserData updatedUser = new UserData();
            updatedUser.setUserId(currentUser.getUserId());
            updatedUser.setUsername(newName);
            updatedUser.setProfilePicture(currentUser.getProfilePicture());
            userViewModel.updateUser(currentUser.getUserId(), newName, null);
        }
    }

    public static File getFileFromUri(Context context, Uri uri) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        File tempFile = File.createTempFile("upload_", ".jpg", context.getCacheDir());
        FileOutputStream outputStream = new FileOutputStream(tempFile);
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.close();
        inputStream.close();
        return tempFile;
    }
}