import { useEffect, useState } from "react";
import { useCategories } from "../hooks/useCategories";
import CategoryForm from "../components/CategoryForm";
import CategoriesTable from "../components/CategoriesTable";
import { Typography, Button, CircularProgress, Box, Stack } from "@mui/material";
import AddIcon from "@mui/icons-material/Add";

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
          Nova Categoria
        </Button>
      </Stack>

      {loading ? (
        <Box sx={{ display: "flex", justifyContent: "center", mt: 4 }}>
          <CircularProgress />
        </Box>
      ) : (
        <CategoriesTable categories={categories} onEdit={handleEditCategory} onDelete={handleDelete} onRefresh={loadCategories} loading={loading} />
      )}

      <CategoryForm open={openForm} onClose={handleCloseForm} onSave={handleSave} initialData={editData} loading={loading} />

      {error && (
        <Typography color="error" mt={2}>
          {error}
        </Typography>
      )}
    </Box>
  );
}
