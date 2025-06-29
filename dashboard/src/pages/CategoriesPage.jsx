import { useEffect, useState } from "react";
import { useCategories } from "../hooks/useCategories";
import CategoryForm from "../components/CategoryForm";
import CategoriesTable from "../components/CategoriesTable";
import { Typography, Button, CircularProgress, Box } from "@mui/material";

export default function CategoriesPage() {
  const { categories, loading, error, handleCreate, handleEdit, handleDelete, loadCategories } = useCategories();
  const [openForm, setOpenForm] = useState(false);
  const [editData, setEditData] = useState(null);

  useEffect(() => {
    loadCategories();
  }, [loadCategories]);

  function handleAdd() {
    setEditData(null);
    setOpenForm(true);
  }
  function handleEditCategory(cat) {
    setEditData(cat);
    setOpenForm(true);
  }
  async function handleSave(form) {
    if (editData) {
      await handleEdit({ ...editData, ...form });
    } else {
      await handleCreate(form);
    }
    setOpenForm(false);
    setEditData(null);
    loadCategories();
  }
  function handleCloseForm() {
    setOpenForm(false);
    setEditData(null);
  }

  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        Categorias
      </Typography>
      <Button variant="contained" sx={{ mb: 2 }} onClick={handleAdd}>
        Nova Categoria
      </Button>
      {loading ? (
        <Box sx={{ display: "flex", justifyContent: "center", mt: 4 }}>
          <CircularProgress />
        </Box>
      ) : (
        <CategoriesTable categories={categories} onEdit={handleEditCategory} onDelete={handleDelete} />
      )}
      <CategoryForm open={openForm} onClose={handleCloseForm} onSave={handleSave} initialData={editData} loading={loading} />
      {error && <Typography color="error">{error}</Typography>}
    </Box>
  );
}
// Compare this snippet from src/components/CategoryForm.jsx:
