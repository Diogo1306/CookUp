import { useEffect, useState } from "react";
import { useIngredients } from "../hooks/useIngredients";
import IngredientsForm from "../components/IngredientsForm";
import IngredientsTable from "../components/IngredientsTable";
import { Typography, Button, CircularProgress, Box, Stack } from "@mui/material";
import AddIcon from "@mui/icons-material/Add";

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
    <Box sx={{ m: { xs: 1, md: 3 }, mt: 3, maxWidth: "100%" }}>
      <Stack direction="row" spacing={2} alignItems="center" justifyContent="space-between" sx={{ mb: 2 }}>
        <Button
          variant="contained"
          color="primary"
          onClick={handleAdd}
          startIcon={<AddIcon />}
          sx={{
            borderRadius: 2,
            fontWeight: 600,
            textTransform: "none",
            boxShadow: 1,
            px: 3,
          }}
        >
          Novo Ingrediente
        </Button>
      </Stack>

      {loading ? (
        <Box sx={{ display: "flex", justifyContent: "center", mt: 4 }}>
          <CircularProgress />
        </Box>
      ) : (
        <IngredientsTable
          ingredients={ingredients}
          onEdit={handleEditIngredient}
          onDelete={handleDelete}
          onRefresh={loadIngredients}
          loading={loading}
        />
      )}

      <IngredientsForm open={openForm} onClose={handleCloseForm} onSave={handleSave} initialData={editData} loading={loading} />

      {error && (
        <Typography color="error" mt={2}>
          {error}
        </Typography>
      )}
    </Box>
  );
}
