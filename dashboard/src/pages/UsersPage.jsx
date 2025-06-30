import { useEffect, useState } from "react";
import { useUsers } from "../hooks/useUsers";
import UsersForm from "../components/UsersFrom";
import UsersTable from "../components/UsersTable";
import { Typography, Button, CircularProgress, Box, Stack } from "@mui/material";
import PersonAddIcon from "@mui/icons-material/PersonAdd";

export default function UsersPage() {
  const { users, loading, error, loadUsers, handleCreate, handleEdit, handleDelete, handleBlock, handleUnblock } = useUsers();
  const [openForm, setOpenForm] = useState(false);
  const [editData, setEditData] = useState(null);

  useEffect(() => {
    loadUsers();
  }, [loadUsers]);

  function handleAdd() {
    setEditData(null);
    setOpenForm(true);
  }

  function handleEditUser(user) {
    setEditData(user);
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
          disabled={loading}
          startIcon={<PersonAddIcon />}
          sx={{
            borderRadius: 2,
            fontWeight: 600,
            textTransform: "none",
            boxShadow: 1,
            px: 3,
          }}
        >
          Novo Utilizador
        </Button>
      </Stack>

      {loading ? (
        <Box sx={{ display: "flex", justifyContent: "center", mt: 4 }}>
          <CircularProgress />
        </Box>
      ) : (
        <UsersTable
          users={users}
          onEdit={handleEditUser}
          onDelete={handleDelete}
          onBlock={handleBlock}
          onUnblock={handleUnblock}
          reloadUsers={loadUsers}
          loading={loading}
        />
      )}

      <UsersForm open={openForm} onClose={handleCloseForm} onSave={handleSave} initialData={editData} loading={loading} />

      {error && (
        <Typography color="error" mt={2}>
          {error}
        </Typography>
      )}
    </Box>
  );
}
