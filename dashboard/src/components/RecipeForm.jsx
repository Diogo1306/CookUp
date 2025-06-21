import React, { useState, useEffect } from "react";
import {
  Box,
  TextField,
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Chip,
  Stack,
  IconButton,
  Select,
  MenuItem,
  InputLabel,
  FormControl,
  Avatar,
  Typography,
  Grid,
  Paper,
  useTheme,
} from "@mui/material";
import { Delete, AddPhotoAlternate } from "@mui/icons-material";

const dificuldades = ["Fácil", "Médio", "Difícil"];

export default function RecipeForm({ open, handleClose, handleSave, initialData, allCategories = [] }) {
  const theme = useTheme();

  const [form, setForm] = useState({
    recipe_id: "",
    author_id: "",
    title: "",
    description: "",
    instructions: "",
    difficulty: "",
    preparation_time: "",
    servings: "",
    categories: [],
    ingredients: [],
    gallery: [],
  });

  // Recebe os dados e converte as categorias para array de IDs
  useEffect(() => {
    if (initialData) {
      setForm({
        recipe_id: initialData.recipe_id || "",
        author_id: initialData.author_id || "",
        title: initialData.title || "",
        description: initialData.description || "",
        instructions: initialData.instructions || "",
        difficulty: initialData.difficulty || "",
        preparation_time: initialData.preparation_time || "",
        servings: initialData.servings || "",
        categories: (initialData.categories || []).map((c) => c.category_id),
        ingredients: initialData.ingredients
          ? initialData.ingredients.map((ing) => ({
              ingredient_name: ing.ingredient_name || "",
              ingredient_quantity: ing.ingredient_quantity || "",
            }))
          : [],
        gallery: initialData.gallery || [],
      });
    } else {
      setForm({
        recipe_id: "",
        author_id: "",
        title: "",
        description: "",
        instructions: "",
        difficulty: "",
        preparation_time: "",
        servings: "",
        categories: [],
        ingredients: [],
        gallery: [],
      });
    }
  }, [initialData, open]);

  const handleIngredientChange = (i, field, value) => {
    const newIngredients = [...form.ingredients];
    newIngredients[i][field] = value;
    setForm((f) => ({ ...f, ingredients: newIngredients }));
  };
  const handleAddIngredient = () => {
    setForm((f) => ({
      ...f,
      ingredients: [...(f.ingredients || []), { ingredient_name: "", ingredient_quantity: "" }],
    }));
  };
  const handleRemoveIngredient = (i) => {
    setForm((f) => ({
      ...f,
      ingredients: f.ingredients.filter((_, idx) => idx !== i),
    }));
  };

  // Categorias (IDs)
  const handleCategoryToggle = (cat) => {
    setForm((f) => {
      const isSelected = f.categories.includes(cat.category_id);
      return {
        ...f,
        categories: isSelected ? f.categories.filter((id) => id !== cat.category_id) : [...f.categories, cat.category_id],
      };
    });
  };

  // Imagens
  const handleAddImage = (file) => {
    if (form.gallery.length >= 6) return;
    setForm((f) => ({ ...f, gallery: [...f.gallery, file] }));
  };

  // Trocar para a tua função de upload real!
  const uploadToServer = async (file) => {
    // Mock: só para mostrar preview local, depois faz upload real!
    return URL.createObjectURL(file);
  };

  const handleDeleteImage = (idx) => {
    setForm((f) => ({
      ...f,
      gallery: f.gallery.filter((_, i) => i !== idx),
    }));
  };

  function handleSubmit(e) {
    e.preventDefault();
    if (form.gallery.length > 6) {
      alert("Máximo 6 imagens permitidas.");
      return;
    }
    handleSave({
      ...form,
      categories: form.categories.map(Number),
    });
    console.log("Form submitted:", form);
    console.log("SUBMITTING FORM", form.gallery);
    form.gallery.forEach((img, idx) => {
      console.log(idx, img, typeof img, img instanceof File);
    });
  }

  return (
    <Dialog open={open} onClose={handleClose} maxWidth="md" fullWidth>
      <DialogTitle sx={{ fontWeight: 700 }}>{form.recipe_id ? "Editar Receita" : "Nova Receita"}</DialogTitle>
      <DialogContent
        sx={{
          bgcolor: theme.palette.background.default,
        }}
      >
        <Box
          component="form"
          onSubmit={handleSubmit}
          sx={{
            mt: 1,
            display: "flex",
            flexDirection: "column",
            gap: 3,
          }}
        >
          <Paper
            elevation={0}
            sx={{
              p: 2,
              mb: 1,
              bgcolor: theme.palette.background.paper,
              borderRadius: 2,
              border: `1px solid ${theme.palette.divider}`,
            }}
          >
            <Typography fontWeight={600} mb={1}>
              Galeria de Imagens (até 6)
            </Typography>
            <Grid container spacing={2}>
              {form.gallery.map((img, i) => (
                <Grid item xs={6} sm={4} md={2} key={i}>
                  <Box sx={{ position: "relative", borderRadius: 2, overflow: "hidden", boxShadow: 1 }}>
                    <Avatar variant="rounded" src={img instanceof File ? URL.createObjectURL(img) : img} sx={{ width: "100%", height: 100, mb: 0 }} />
                    <IconButton
                      size="small"
                      color="error"
                      onClick={() => handleDeleteImage(i)}
                      sx={{ position: "absolute", top: 6, right: 6, bgcolor: theme.palette.background.paper, boxShadow: 1 }}
                    >
                      <Delete fontSize="small" />
                    </IconButton>
                  </Box>
                </Grid>
              ))}

              {form.gallery.length < 6 && (
                <Grid item xs={6} sm={4} md={2}>
                  <Button
                    component="label"
                    variant="outlined"
                    sx={{
                      width: "100%",
                      height: 100,
                      display: "flex",
                      alignItems: "center",
                      justifyContent: "center",
                      borderRadius: 2,
                      borderColor: theme.palette.primary.main,
                    }}
                  >
                    <AddPhotoAlternate fontSize="large" color="primary" />
                    <input
                      hidden
                      type="file"
                      accept="image/*"
                      onChange={async (e) => {
                        const file = e.target.files[0];
                        if (!file) return;
                        handleAddImage(file);
                      }}
                    />
                  </Button>
                </Grid>
              )}
            </Grid>
          </Paper>

          {/* Dados principais */}
          <Paper
            elevation={0}
            sx={{
              p: 2,
              bgcolor: theme.palette.background.paper,
              borderRadius: 2,
              border: `1px solid ${theme.palette.divider}`,
              mb: 1,
            }}
          >
            <Grid container spacing={2}>
              <Grid item xs={12} sm={8}>
                <TextField
                  name="title"
                  label="Título"
                  value={form.title}
                  onChange={(e) => setForm((f) => ({ ...f, title: e.target.value }))}
                  required
                  fullWidth
                />
              </Grid>
              <Grid item xs={12} sm={4}>
                <FormControl fullWidth>
                  <InputLabel id="difficulty-label">Dificuldade</InputLabel>
                  <Select
                    labelId="difficulty-label"
                    value={form.difficulty}
                    label="Dificuldade"
                    onChange={(e) =>
                      setForm((f) => ({
                        ...f,
                        difficulty: e.target.value,
                      }))
                    }
                    required
                  >
                    {dificuldades.map((dif) => (
                      <MenuItem key={dif} value={dif}>
                        {dif}
                      </MenuItem>
                    ))}
                  </Select>
                </FormControl>
              </Grid>
              <Grid item xs={6} sm={3}>
                <TextField
                  name="preparation_time"
                  label="Tempo (min)"
                  type="number"
                  value={form.preparation_time}
                  onChange={(e) =>
                    setForm((f) => ({
                      ...f,
                      preparation_time: e.target.value,
                    }))
                  }
                  required
                  fullWidth
                />
              </Grid>
              <Grid item xs={6} sm={3}>
                <TextField
                  name="servings"
                  label="Porções"
                  type="number"
                  value={form.servings}
                  onChange={(e) =>
                    setForm((f) => ({
                      ...f,
                      servings: e.target.value,
                    }))
                  }
                  required
                  fullWidth
                />
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField
                  name="description"
                  label="Descrição"
                  value={form.description}
                  onChange={(e) => setForm((f) => ({ ...f, description: e.target.value }))}
                  required
                  fullWidth
                  multiline
                  minRows={3}
                  maxRows={8}
                  sx={{
                    ".MuiInputBase-root": {
                      fontSize: "1.05rem",
                      background: theme.palette.background.soft,
                    },
                  }}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  name="instructions"
                  label="Instruções"
                  value={form.instructions}
                  onChange={(e) =>
                    setForm((f) => ({
                      ...f,
                      instructions: e.target.value,
                    }))
                  }
                  required
                  fullWidth
                  multiline
                  minRows={5}
                  maxRows={20}
                  sx={{
                    ".MuiInputBase-root": {
                      fontSize: "1.04rem",
                      background: theme.palette.background.soft,
                    },
                  }}
                />
              </Grid>
            </Grid>
          </Paper>

          {/* Ingredientes */}
          <Paper
            elevation={0}
            sx={{
              p: 2,
              bgcolor: theme.palette.background.paper,
              borderRadius: 2,
              border: `1px solid ${theme.palette.divider}`,
              mb: 1,
            }}
          >
            <Typography fontWeight={600} mb={1}>
              Ingredientes
            </Typography>
            <Stack spacing={1}>
              {form.ingredients.map((ing, i) => (
                <Box key={i} sx={{ display: "flex", gap: 1, alignItems: "center" }}>
                  <TextField
                    label="Ingrediente"
                    value={ing.ingredient_name}
                    onChange={(e) => handleIngredientChange(i, "ingredient_name", e.target.value)}
                    size="small"
                    required
                    sx={{ flex: 2 }}
                  />
                  <TextField
                    label="Quantidade"
                    value={ing.ingredient_quantity}
                    onChange={(e) => handleIngredientChange(i, "ingredient_quantity", e.target.value)}
                    size="small"
                    required
                    sx={{ flex: 1 }}
                  />
                  <IconButton onClick={() => handleRemoveIngredient(i)} color="error" size="small">
                    <Delete fontSize="small" />
                  </IconButton>
                </Box>
              ))}
              <Button onClick={handleAddIngredient} size="small" variant="outlined" sx={{ alignSelf: "start" }}>
                Adicionar Ingrediente
              </Button>
            </Stack>
          </Paper>

          {/* Categorias */}
          <Paper
            elevation={0}
            sx={{
              p: 2,
              bgcolor: theme.palette.background.paper,
              borderRadius: 2,
              border: `1px solid ${theme.palette.divider}`,
            }}
          >
            <Typography fontWeight={600} mb={1}>
              Categorias
            </Typography>
            <Stack direction="row" spacing={1} sx={{ flexWrap: "wrap" }}>
              {allCategories.map((cat) => {
                const selected = form.categories.includes(cat.category_id);
                return (
                  <Chip
                    key={cat.category_id}
                    label={cat.category_name}
                    clickable
                    onClick={() => handleCategoryToggle(cat)}
                    avatar={<Avatar src={cat.category_image_url || cat.image_url} />}
                    sx={{
                      mb: 1,
                      border: selected ? `2px solid ${cat.color_hex || cat.category_color}` : `1px solid ${theme.palette.divider}`,
                      bgcolor: selected ? (cat.color_hex || cat.category_color) + "22" : undefined,
                      color: selected ? "#111" : theme.palette.text.primary,
                      fontWeight: 500,
                      boxShadow: selected ? `0 0 0 2px ${cat.color_hex || cat.category_color || "#000"}33` : undefined,
                    }}
                    variant={selected ? "filled" : "outlined"}
                  />
                );
              })}
            </Stack>
          </Paper>
        </Box>
      </DialogContent>
      <DialogActions
        sx={{
          bgcolor: theme.palette.background.paper,
          borderTop: `1px solid ${theme.palette.divider}`,
        }}
      >
        <Button onClick={handleClose} color="secondary" variant="outlined">
          Cancelar
        </Button>
        <Button onClick={handleSubmit} variant="contained" color="primary">
          Salvar
        </Button>
      </DialogActions>
    </Dialog>
  );
}
