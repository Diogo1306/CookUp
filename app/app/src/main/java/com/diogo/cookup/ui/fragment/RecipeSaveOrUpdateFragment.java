package com.diogo.cookup.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diogo.cookup.R;
import com.diogo.cookup.data.model.CategoryData;
import com.diogo.cookup.data.model.IngredientData;
import com.diogo.cookup.data.model.UserData;
import com.diogo.cookup.ui.adapter.IngredientSaveOrUpdateAdapter;
import com.diogo.cookup.ui.adapter.RecipeGalleryAdapter;
import com.diogo.cookup.utils.SharedPrefHelper;
import com.diogo.cookup.viewmodel.RecipeCreateEditViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecipeSaveOrUpdateFragment extends Fragment {

    private RecipeCreateEditViewModel viewModel;
    private EditText etTitle, etDescription, etInstructions, etServings, etPreparationTime;
    private RecyclerView rvIngredients, rvGallery;
    private ChipGroup chipGroupCategories;
    private Button btnAddIngredient, btnAddImage;
    private MaterialButton btnSave;
    private RadioGroup rgDifficulty;

    private IngredientSaveOrUpdateAdapter ingredientSaveOrUpdateAdapter;
    private RecipeGalleryAdapter galleryAdapter;
    private Integer editingRecipeId = null;
    // Usar List<Object> para suportar File (novas) e String (antigas/URLs)
    private final List<Object> imageFiles = new ArrayList<>();

    private Set<Integer> categoriesToCheck = new HashSet<>();

    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    handleImageSelection(result.getData());
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recipe_edit_create, container, false);

        viewModel = new ViewModelProvider(this).get(RecipeCreateEditViewModel.class);

        if (getArguments() != null && getArguments().containsKey("recipe_id")) {
            editingRecipeId = getArguments().getInt("recipe_id");
            viewModel.fetchRecipeById(editingRecipeId);
        }

        etTitle = v.findViewById(R.id.et_title);
        etDescription = v.findViewById(R.id.et_description);
        etInstructions = v.findViewById(R.id.et_instructions);
        etServings = v.findViewById(R.id.et_servings);
        etPreparationTime = v.findViewById(R.id.et_preparation_time);
        rgDifficulty = v.findViewById(R.id.rg_difficulty);
        rvIngredients = v.findViewById(R.id.rv_ingredients);
        rvGallery = v.findViewById(R.id.rv_gallery);
        chipGroupCategories = v.findViewById(R.id.chip_group_categories);
        btnAddIngredient = v.findViewById(R.id.btn_add_ingredient);
        btnAddImage = v.findViewById(R.id.btn_add_image);
        btnSave = v.findViewById(R.id.btn_save);

        setupIngredientsRecycler();
        setupGalleryRecycler();
        setupListeners();
        observeViewModel();

        viewModel.fetchCategories();

        return v;
    }

    private void setupIngredientsRecycler() {
        ingredientSaveOrUpdateAdapter = new IngredientSaveOrUpdateAdapter(
                new ArrayList<>(),
                position -> viewModel.removeIngredient(position),
                (position, name, autoView) -> viewModel.fetchIngredientSuggestions(name)
        );
        rvIngredients.setAdapter(ingredientSaveOrUpdateAdapter);
        rvIngredients.setLayoutManager(new LinearLayoutManager(getContext()));
        rvIngredients.setItemViewCacheSize(10);
    }

    private void setupGalleryRecycler() {
        galleryAdapter = new RecipeGalleryAdapter(imageFiles, position -> {
            // Ao remover, sempre remova do imageFiles e atualize a ViewModel
            if (position >= 0 && position < imageFiles.size()) {
                imageFiles.remove(position);
                galleryAdapter.notifyDataSetChanged();
                viewModel.setImages(new ArrayList<>(imageFiles)); // sempre mande cópia nova
            }
        });
        rvGallery.setAdapter(galleryAdapter);
        rvGallery.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
    }

    private void setupListeners() {
        btnAddIngredient.setOnClickListener(v -> viewModel.addIngredient(new IngredientData("", "", null)));
        btnAddImage.setOnClickListener(v -> openGalleryPicker());
        btnSave.setOnClickListener(v -> saveRecipe());
    }

    private void observeViewModel() {
        viewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            chipGroupCategories.removeAllViews();
            for (CategoryData cat : categories) {
                Chip chip = new Chip(getContext());
                chip.setText(cat.getCategoryName());
                chip.setTag(cat.getCategoryId());
                chip.setCheckable(true);
                if (categoriesToCheck.contains(cat.getCategoryId())) {
                    chip.setChecked(true);
                }
                chipGroupCategories.addView(chip);
            }
        });

        viewModel.getRecipeToEdit().observe(getViewLifecycleOwner(), recipe -> {
            if (recipe == null) return;
            etTitle.setText(recipe.getTitle());
            etDescription.setText(recipe.getDescription());
            etInstructions.setText(recipe.getInstructions());
            etServings.setText(String.valueOf(recipe.getServings()));
            etPreparationTime.setText(String.valueOf(recipe.getPreparationTime()));

            // Mantém as imagens antigas na lista, sem substituir
            imageFiles.clear();
            if (recipe.getGallery() != null) {
                imageFiles.addAll(recipe.getGallery());
            }
            galleryAdapter.notifyDataSetChanged();
            viewModel.setImages(new ArrayList<>(imageFiles)); // sincroniza com VM

            if (recipe.getDifficulty() != null) {
                switch (recipe.getDifficulty()) {
                    case "Fácil":
                        rgDifficulty.check(R.id.rb_easy);
                        break;
                    case "Médio":
                        rgDifficulty.check(R.id.rb_medium);
                        break;
                    case "Difícil":
                        rgDifficulty.check(R.id.rb_hard);
                        break;
                }
            }

            categoriesToCheck.clear();
            if (recipe.getCategories() != null) {
                for (CategoryData cat : recipe.getCategories()) {
                    categoriesToCheck.add(cat.getCategoryId());
                }
            }
            for (int i = 0; i < chipGroupCategories.getChildCount(); i++) {
                Chip chip = (Chip) chipGroupCategories.getChildAt(i);
                Integer chipCatId = (Integer) chip.getTag();
                if (categoriesToCheck.contains(chipCatId)) chip.setChecked(true);
            }
        });

        viewModel.getIngredients().observe(getViewLifecycleOwner(), ingredients -> {
            if (ingredientSaveOrUpdateAdapter.getItemCount() != ingredients.size()) {
                ingredientSaveOrUpdateAdapter.updateList(ingredients);
            }
        });

        viewModel.getIngredientSuggestions().observe(getViewLifecycleOwner(), ingredients -> {
            List<String> names = new ArrayList<>();
            for (IngredientData ing : ingredients) {
                names.add(ing.getName());
            }
            ingredientSaveOrUpdateAdapter.updateSuggestions(names);
        });

        viewModel.getImages().observe(getViewLifecycleOwner(), images -> {
            // Não faça clear aqui! Só atualize se mudou de fora!
            if (images != null) {
                imageFiles.clear();
                imageFiles.addAll(images);
                new Handler(Looper.getMainLooper()).post(() -> {
                    galleryAdapter.notifyDataSetChanged();
                    btnAddImage.setEnabled(imageFiles.size() < 6);
                });
            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        });
    }

    private void openGalleryPicker() {
        int remaining = 6 - imageFiles.size();
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        galleryLauncher.launch(Intent.createChooser(intent, "Escolher imagens"));
    }

    private void handleImageSelection(Intent data) {
        int remaining = 6 - imageFiles.size();
        List<Uri> uris = new ArrayList<>();

        if (data.getClipData() != null) {
            int count = data.getClipData().getItemCount();
            for (int i = 0; i < count && uris.size() < remaining; i++) {
                uris.add(data.getClipData().getItemAt(i).getUri());
            }
        } else if (data.getData() != null && remaining > 0) {
            uris.add(data.getData());
        }

        for (Uri uri : uris) {
            requireContext().getContentResolver().takePersistableUriPermission(
                    uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            File file = copyUriToFile(uri);
            if (file != null && file.length() < 2 * 1024 * 1024) {
                imageFiles.add(file); // só ADICIONA, não substitui
            } else {
                Toast.makeText(getContext(), "Imagem ignorada: muito pesada ou falha na leitura.", Toast.LENGTH_SHORT).show();
            }
        }
        galleryAdapter.notifyDataSetChanged();
        viewModel.setImages(new ArrayList<>(imageFiles));
    }

    private File copyUriToFile(Uri uri) {
        try {
            String fileName = "img_" + System.currentTimeMillis() + ".jpg";
            File file = new File(requireContext().getCacheDir(), fileName);
            InputStream in = requireContext().getContentResolver().openInputStream(uri);
            FileOutputStream out = new FileOutputStream(file);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();
            out.close();
            in.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveRecipe() {
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String instructions = etInstructions.getText().toString().trim();
        String difficulty;
        int selectedId = rgDifficulty.getCheckedRadioButtonId();
        if (selectedId == R.id.rb_easy) {
            difficulty = "Fácil";
        } else if (selectedId == R.id.rb_medium) {
            difficulty = "Médio";
        } else if (selectedId == R.id.rb_hard) {
            difficulty = "Difícil";
        } else {
            difficulty = "Fácil";
        }
        String servingsStr = etServings.getText().toString().trim();
        String prepTimeStr = etPreparationTime.getText().toString().trim();

        if (title.isEmpty() || description.isEmpty() || instructions.isEmpty() ||
                difficulty.isEmpty() || servingsStr.isEmpty() || prepTimeStr.isEmpty()) {
            Toast.makeText(getContext(), "Preencha todos os campos obrigatórios.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (ingredientSaveOrUpdateAdapter.getItemCount() == 0) {
            Toast.makeText(getContext(), "Adicione pelo menos um ingrediente.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (imageFiles.isEmpty()) {
            Toast.makeText(getContext(), "Adicione pelo menos uma imagem.", Toast.LENGTH_SHORT).show();
            return;
        }

        int servings = Integer.parseInt(servingsStr);
        int prepTime = Integer.parseInt(prepTimeStr);

        List<Integer> selectedCats = new ArrayList<>();
        for (int i = 0; i < chipGroupCategories.getChildCount(); i++) {
            Chip chip = (Chip) chipGroupCategories.getChildAt(i);
            if (chip.isChecked()) selectedCats.add((Integer) chip.getTag());
        }
        if (selectedCats.isEmpty()) {
            Toast.makeText(getContext(), "Selecione pelo menos uma categoria.", Toast.LENGTH_SHORT).show();
            return;
        }

        int authorId = 1;
        UserData user = SharedPrefHelper.getInstance(requireContext()).getUser();
        if (user != null) {
            authorId = user.getUserId();
        }

        Integer recipeId = editingRecipeId;

        // ==== NOVO: Separe arquivos e urls ====
        List<File> imagensNovas = new ArrayList<>();
        List<String> imagensAntigas = new ArrayList<>();
        for (Object img : imageFiles) {
            if (img instanceof File) {
                imagensNovas.add((File) img);
            } else if (img instanceof String) {
                imagensAntigas.add((String) img);
            }
        }

        // ======= CHAME O NOVO MÉTODO DA VIEWMODEL (vamos ajustar lá!) ======
        viewModel.saveOrUpdateRecipe(
                        recipeId, authorId, title, description, instructions, difficulty,
                        prepTime, servings, selectedCats, imagensNovas, imagensAntigas
                ).observe(getViewLifecycleOwner(), recipeData -> {
            Toast.makeText(getContext(),
                    recipeId == null ? "Receita salva!" : "Receita atualizada!",
                    Toast.LENGTH_SHORT
            ).show();
            NavHostFragment.findNavController(this).popBackStack();
        });
    }
}
