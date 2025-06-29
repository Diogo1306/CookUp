import { useEffect, useState } from "react";
import { useUsers } from "../hooks/useUsers";
import UsersForm from "../components/UsersFrom";
import UsersTable from "../components/UsersTable";
import { Typography, Button, CircularProgress, Box } from "@mui/material";

export default function UsersPage() {
  const { users, loading, error, handleCreate, handleEdit, handleDelete, handleBlock, handleUnblock, loadUsers } = useUsers();
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
        Usuários do Sistema
      </Typography>
      <Button variant="contained" sx={{ mb: 2 }} onClick={handleAdd}>
        Novo Usuário
      </Button>
      {loading ? (
        <Box sx={{ display: "flex", justifyContent: "center", mt: 4 }}>
          <CircularProgress />
        </Box>
      ) : (
        <UsersTable users={users} onEdit={handleEditUser} onBlock={handleBlock} onUnblock={handleUnblock} reloadUsers={loadUsers} />
      )}
      <UsersForm open={openForm} onClose={handleCloseForm} onSave={handleSave} initialData={editData} loading={loading} />
      {error && <Typography color="error">{error}</Typography>}
    </Box>
  );
}
