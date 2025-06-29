import React, { useState, useEffect } from "react";
import { Dialog, DialogTitle, DialogContent, TextField, Button, DialogActions, Avatar, Box, MenuItem } from "@mui/material";
import { updateFirebaseEmail, updateFirebasePassword } from "../api/adminUser";

const DEFAULT_PIC = "http://192.168.0.26/PAP/CookUp_Core/uploads/profile_pictures/default.png";

export default function UsersForm({ open, onClose, onSave, initialData, loading }) {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [role, setRole] = useState("user");
  const [profilePicture, setProfilePicture] = useState(null);
  const [preview, setPreview] = useState(DEFAULT_PIC);
  const [error, setError] = useState("");

  useEffect(() => {
    setUsername(initialData?.username || "");
    setEmail(initialData?.email || "");
    setRole(initialData?.role || "user");
    setProfilePicture(null);
    setPreview(initialData?.profile_picture || DEFAULT_PIC);
    setPassword("");
    setError("");
  }, [initialData, open]);

  function handlePictureChange(e) {
    const file = e.target.files[0];
    setProfilePicture(file);
    if (file) setPreview(URL.createObjectURL(file));
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");

    if (!initialData) {
      if (!email || !/^[\w-.]+@([\w-]+\.)+[\w-]{2,4}$/.test(email)) {
        setError("Email inválido.");
        return;
      }
      if (password.length < 6) {
        setError("Senha deve ter no mínimo 6 caracteres.");
        return;
      }
    }
    if (initialData) {
      if (email !== initialData.email) {
        const res = await updateFirebaseEmail(initialData.firebase_uid, email);
        if (!res.success) {
          setError("Erro ao trocar e-mail no Firebase: " + res.error);
          return;
        }
      }
      if (password.length > 0) {
        const res = await updateFirebasePassword(initialData.firebase_uid, password);
        if (!res.success) {
          setError("Erro ao trocar senha no Firebase: " + res.error);
          return;
        }
      }
    }

    const form = {
      username,
      role,
      email,
    };
    if (profilePicture) form.profile_picture = profilePicture;
    if (initialData?.user_id) form.user_id = initialData.user_id;
    if (!initialData) form.password = password; // só manda senha ao criar

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
            <TextField
              label="Email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              // Permite editar email tanto na criação quanto edição!
            />
            {/* Senha só na criação */}
            {!initialData && (
              <TextField
                label="Senha"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                inputProps={{ minLength: 6 }}
                required
                helperText="Mínimo 6 caracteres"
              />
            )}
            {/* Edição: senha opcional */}
            {initialData && (
              <TextField
                label="Nova senha"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                inputProps={{ minLength: 6 }}
                helperText="Deixe em branco para não alterar"
              />
            )}
            <TextField select label="Tipo" value={role} onChange={(e) => setRole(e.target.value)}>
              <MenuItem value="user">Usuário</MenuItem>
              <MenuItem value="admin">Admin</MenuItem>
            </TextField>
            {error && <Box sx={{ color: "red", mt: 1 }}>{error}</Box>}
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
