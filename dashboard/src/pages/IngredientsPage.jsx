import { useEffect, useState } from "react";
import { useIngredients } from "../hooks/useIngredients";
import IngredientsForm from "../components/IngredientsForm";
import IngredientsTable from "../components/IngredientsTable";
import { Typography, Button, CircularProgress, Box } from "@mui/material";

export default function IngredientsPage() {
  const { ingredients, loading, error, handleCreate, handleEdit, handleDelete, loadIngredients } = useIngredients();
  const [openForm, setOpenForm] = useState(false);
  const [editData, setEditData] = useState(null);

  useEffect(() => {
    loadIngredients();
  }, [loadIngredients]);

  function handleAdd() {
    setEditData(null);
    setOpenForm(true);
  }
  function handleEditIngredient(ingredient) {
    setEditData(ingredient);
    setOpenForm(true);
  }
  async function handleSave(form) {
    if (editData) {
      await handleEdit(form);
    } else {
      await handleCreate(form);
    }
    setOpenForm(false);
    setEditData(null);
    loadIngredients();
  }

  function handleCloseForm() {
    setOpenForm(false);
    setEditData(null);
  }

  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        Ingredientes
      </Typography>
      <Button variant="contained" sx={{ mb: 2 }} onClick={handleAdd}>
        Novo Ingrediente
      </Button>
      {loading ? (
        <Box sx={{ display: "flex", justifyContent: "center", mt: 4 }}>
          <CircularProgress />
        </Box>
      ) : (
        <IngredientsTable ingredients={ingredients} onEdit={handleEditIngredient} onDelete={handleDelete} />
      )}
      <IngredientsForm open={openForm} onClose={handleCloseForm} onSave={handleSave} initialData={editData} loading={loading} />
      {error && <Typography color="error">{error}</Typography>}
    </Box>
  );
}
