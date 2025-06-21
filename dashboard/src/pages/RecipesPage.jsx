import React, { useState, useEffect } from "react";
import { useRecipes } from "../hooks/useRecipes";
import RecipeForm from "../components/RecipeForm";
import RecipeTable from "../components/RecipeTable";
import { Button, CircularProgress, Box, Typography } from "@mui/material";
import { getAllCategories } from "../api/categories";
import { getRecipeById } from "../api/recipe";

export default function RecipesPage() {
  const { recipes, loading, error, handleDelete, handleSave } = useRecipes();

  const [openForm, setOpenForm] = useState(false);
  const [editData, setEditData] = useState(null);
  const [allCategories, setAllCategories] = useState([]);
  const [loadingEdit, setLoadingEdit] = useState(false);

  useEffect(() => {
    getAllCategories().then((data) => setAllCategories(data));
  }, []);

  async function handleEdit(recipe) {
    const dataCompleta = await getRecipeById(recipe.recipe_id);
    setEditData(dataCompleta.data);
    setOpenForm(true);
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
  console.log("recipes", recipes);

  return (
    <div>
      <Button onClick={handleAdd} variant="contained" sx={{ mb: 2 }}>
        Nova Receita
      </Button>
      {loading && (
        <Box sx={{ display: "flex", justifyContent: "center", my: 4 }}>
          <CircularProgress />
        </Box>
      )}
      {error && (
        <Typography color="error" sx={{ mb: 2 }}>
          {error}
        </Typography>
      )}
      {!loading && !error && <RecipeTable recipes={recipes} onEdit={handleEdit} onDelete={handleDelete} />}
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
    </div>
  );
}
