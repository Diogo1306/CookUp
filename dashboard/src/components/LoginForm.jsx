import React, { useState } from "react";
import { useLogin } from "../hooks/useLogin";
import { useUser } from "../context/UserContext";
import { Box, TextField, Button, Typography, Alert, LinearProgress, Paper } from "@mui/material";

export default function LoginForm({ onLogin }) {
  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const { setUser } = useUser() || {};
  const { login, loading, error, success } = useLogin();
  const [showSuccess, setShowSuccess] = useState(false);

  async function handleSubmit(e) {
    e.preventDefault();
    setShowSuccess(false);
    const user = await login(email, senha);
    if (user && setUser) {
      setUser(user);
      onLogin && onLogin(user);
      setShowSuccess(true);
    }
  }

  return (
    <Box
      component={Paper}
      elevation={3}
      sx={{
        maxWidth: 360,
        mx: "auto",
        mt: 8,
        p: 4,
        borderRadius: 3,
        display: "flex",
        flexDirection: "column",
        gap: 2,
      }}
    >
      <Typography variant="h5" fontWeight={700} align="center" sx={{ mb: 2 }}>
        Iniciar Sessão
      </Typography>

      {loading && <LinearProgress sx={{ mb: 1 }} />}

      <form onSubmit={handleSubmit} style={{ display: "flex", flexDirection: "column", gap: 18 }}>
        <TextField
          id="login-email"
          label="Email"
          type="email"
          placeholder="O seu email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
          fullWidth
          autoComplete="email"
          variant="outlined"
        />
        <TextField
          id="login-senha"
          label="Palavra-passe"
          type="password"
          placeholder="A sua palavra-passe"
          value={senha}
          onChange={(e) => setSenha(e.target.value)}
          required
          fullWidth
          autoComplete="current-password"
          variant="outlined"
        />

        {error && (
          <Alert severity="error" sx={{ mt: 1 }}>
            {error}
          </Alert>
        )}
        {success && showSuccess && (
          <Alert severity="success" sx={{ mt: 1 }}>
            Sessão iniciada com sucesso!
          </Alert>
        )}

        <Button
          type="submit"
          variant="contained"
          color="primary"
          disabled={loading}
          size="large"
          sx={{ mt: 2, borderRadius: 2, fontWeight: 700, textTransform: "none" }}
          fullWidth
        >
          Entrar
        </Button>
      </form>
    </Box>
  );
}
