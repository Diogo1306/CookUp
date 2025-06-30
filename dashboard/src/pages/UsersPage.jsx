import { useEffect, useState } from "react";
import { useUsers } from "../hooks/useUsers";
import UsersForm from "../components/UsersFrom";
import UsersTable from "../components/UsersTable";
import { Typography, Button, CircularProgress, Box } from "@mui/material";

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
    <Box>
      <Typography variant="h4" gutterBottom>
        Utilizador do Sistema
      </Typography>
      <Button variant="contained" sx={{ mb: 2 }} onClick={handleAdd} disabled={loading}>
        {loading ? <CircularProgress size={18} color="inherit" sx={{ mr: 1 }} /> : null}
        Novo Utilizador
      </Button>
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
      {error && <Typography color="error">{error}</Typography>}
    </Box>
  );
}
