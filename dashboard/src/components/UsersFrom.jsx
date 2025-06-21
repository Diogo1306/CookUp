import React, { useState, useEffect } from "react";
import { Dialog, DialogTitle, DialogContent, TextField, Button, DialogActions, Avatar, Box, MenuItem } from "@mui/material";

const DEFAULT_PIC = "http://192.168.0.26/PAP/CookUp_Core/uploads/profile_pictures/default.png";

export default function UsersForm({ open, onClose, onSave, initialData, loading }) {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [role, setRole] = useState("user");
  const [profilePicture, setProfilePicture] = useState(null);
  const [preview, setPreview] = useState(DEFAULT_PIC);

  useEffect(() => {
    setUsername(initialData?.username || "");
    setEmail(initialData?.email || "");
    setRole(initialData?.role || "user");
    setProfilePicture(null);
    setPreview(initialData?.profile_picture || DEFAULT_PIC);
    setPassword("");
  }, [initialData, open]);

  function handlePictureChange(e) {
    const file = e.target.files[0];
    setProfilePicture(file);
    if (file) setPreview(URL.createObjectURL(file));
  }

  function handleSubmit(e) {
    e.preventDefault();
    const form = {
      username,
      role,
    };
    if (!initialData) {
      form.email = email;
      form.password = password;
    }
    if (profilePicture) form.profile_picture = profilePicture;
    if (initialData?.user_id) form.user_id = initialData.user_id;
    onSave(form);
  }

  return (
    <Dialog open={open} onClose={onClose}>
      <form onSubmit={handleSubmit}>
        <DialogTitle>{initialData ? "Editar usuário" : "Novo usuário"}</DialogTitle>
        <DialogContent>
          <Box sx={{ display: "flex", flexDirection: "column", gap: 2, mt: 1 }}>
            <Avatar src={preview} sx={{ width: 56, height: 56, alignSelf: "center" }} />
            <Button variant="contained" component="label">
              Escolher foto
              <input type="file" accept="image/*" hidden onChange={handlePictureChange} />
            </Button>
            <TextField label="Nome" value={username} onChange={(e) => setUsername(e.target.value)} required />
            <TextField label="Email" value={email} onChange={(e) => setEmail(e.target.value)} required disabled={!!initialData} />
            {!initialData && <TextField label="Senha" type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />}
            <TextField select label="Tipo" value={role} onChange={(e) => setRole(e.target.value)}>
              <MenuItem value="user">Usuário</MenuItem>
              <MenuItem value="admin">Admin</MenuItem>
            </TextField>
          </Box>
        </DialogContent>
        <DialogActions>
          <Button onClick={onClose}>Cancelar</Button>
          <Button type="submit" disabled={loading}>
            {loading ? "Salvando..." : "Salvar"}
          </Button>
        </DialogActions>
      </form>
    </Dialog>
  );
}
