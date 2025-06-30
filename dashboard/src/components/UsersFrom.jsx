import { useState, useEffect } from "react";
import { Dialog, DialogTitle, DialogContent, TextField, Button, DialogActions, Avatar, Box, MenuItem, Typography } from "@mui/material";

const DEFAULT_PIC = "../assets/default.png";

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
    if (!email || !/^[\w-.]+@([\w-]+\.)+[\w-]{2,4}$/.test(email)) {
      setError("Email inválido.");
      return;
    }
    if (!initialData && password.length < 6) {
      setError("Senha deve ter no mínimo 6 caracteres.");
      return;
    }

    const form = {
      username,
      role,
      email,
    };
    if (initialData?.user_id) form.user_id = initialData.user_id;
    if (!initialData) form.password = password;
    // Só permite mudar imagem no edit
    if (initialData && profilePicture) form.profile_picture = profilePicture;

    onSave(form);
  }

  return (
    <Dialog open={open} onClose={onClose} maxWidth="xs" fullWidth>
      <form onSubmit={handleSubmit}>
        <DialogTitle>{initialData ? "Editar utilizador" : "Novo utilizador"}</DialogTitle>
        <DialogContent>
          <Box
            sx={{
              display: "flex",
              flexDirection: "column",
              gap: 2,
              mt: 1,
              alignItems: "center",
            }}
          >
            {/* Sempre mostra preview */}
            <Avatar
              src={preview}
              sx={{
                width: 60,
                height: 60,
                boxShadow: 2,
                border: "2px solid #eee",
                mb: 1,
              }}
            />
            {/* Só deixa mudar foto em modo editar */}
            {initialData && (
              <Button
                variant="contained"
                component="label"
                sx={{
                  mb: 1,
                  bgcolor: "grey.100",
                  color: "primary.main",
                  fontWeight: 600,
                  "&:hover": { bgcolor: "primary.main", color: "#fff" },
                }}
              >
                Trocar foto
                <input type="file" accept="image/*" hidden onChange={handlePictureChange} />
              </Button>
            )}

            <TextField label="Nome" value={username} onChange={(e) => setUsername(e.target.value)} required fullWidth />
            <TextField label="Email" value={email} onChange={(e) => setEmail(e.target.value)} required fullWidth autoComplete="off" />
            {/* Só pede senha no novo user */}
            {!initialData && (
              <TextField
                label="Senha"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                inputProps={{ minLength: 6 }}
                required
                fullWidth
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
                fullWidth
                helperText="Deixe em branco para não alterar"
              />
            )}
            <TextField select label="Tipo" value={role} onChange={(e) => setRole(e.target.value)} fullWidth>
              <MenuItem value="user">Utilizador</MenuItem>
              <MenuItem value="admin">Admin</MenuItem>
            </TextField>
            {error && (
              <Typography color="error" sx={{ mt: 1 }}>
                {error}
              </Typography>
            )}
          </Box>
        </DialogContent>
        <DialogActions sx={{ p: 2 }}>
          <Button onClick={onClose} color="secondary">
            Cancelar
          </Button>
          <Button type="submit" variant="contained" disabled={loading}>
            {loading ? "Salvando..." : "Salvar"}
          </Button>
        </DialogActions>
      </form>
    </Dialog>
  );
}
