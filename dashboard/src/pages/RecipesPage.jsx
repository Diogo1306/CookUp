import React, { useState, useEffect } from "react";
import { useRecipes } from "../hooks/useRecipes";
import RecipeForm from "../components/RecipeForm";
import RecipeTable from "../components/RecipeTable";
import { Button, CircularProgress, Box, Typography, Stack, useTheme } from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import { getAllCategories } from "../api/categories";
import { getRecipeById } from "../api/recipe";

export default function RecipesPage() {
  const theme = useTheme();
  const { recipes, loading, error, handleDelete, handleSave, handleRefresh } = useRecipes();
  const [openForm, setOpenForm] = useState(false);
  const [editData, setEditData] = useState(null);
  const [allCategories, setAllCategories] = useState([]);
  const [loadingEdit, setLoadingEdit] = useState(false);

  useEffect(() => {
    getAllCategories().then((data) => setAllCategories(data));
  }, []);

  async function handleEdit(recipe) {
    setLoadingEdit(true);
    const dataCompleta = await getRecipeById(recipe.recipe_id);
    setEditData(dataCompleta.data);
    setOpenForm(true);
    setLoadingEdit(false);
  }

  function handleAdd() {
    setEditData(null);
    setOpenForm(true);
  }

  function handleFormClose() {
    setOpenForm(false);
    setEditData(null);
  }

  async function handleFormSave(data) {
    await handleSave(data, !!editData);
    handleFormClose();
  }

  return (
    <Box sx={{ m: { xs: 1, md: 3 }, mt: 3, maxWidth: "100%" }}>
      <Stack direction="row" spacing={2} sx={{ mb: 2 }}>
        <Button
          onClick={handleAdd}
          variant="contained"
          color="primary"
          startIcon={<AddIcon />}
          sx={{
            fontWeight: 600,
            borderRadius: 2,
            textTransform: "none",
            px: 3,
            boxShadow: 1,
          }}
        >
          Nova Receita
        </Button>
      </Stack>
      {loading ? (
        <Box sx={{ display: "flex", justifyContent: "center", my: 4 }}>
          <CircularProgress />
        </Box>
      ) : error ? (
        <Typography color="error" sx={{ mb: 2 }}>
          {error}
        </Typography>
      ) : (
        <RecipeTable recipes={recipes} onEdit={handleEdit} onDelete={handleDelete} onRefresh={handleRefresh} />
      )}
      <RecipeForm open={openForm} handleClose={handleFormClose} handleSave={handleFormSave} initialData={editData} allCategories={allCategories} />
      {loadingEdit && (
        <Box
          sx={{
            position: "fixed",
            top: 0,
            left: 0,
            width: "100vw",
            height: "100vh",
            bgcolor: "#0008",
            zIndex: 2000,
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
          }}
        >
          <CircularProgress color="secondary" />
        </Box>
      )}
    </Box>
  );
}
