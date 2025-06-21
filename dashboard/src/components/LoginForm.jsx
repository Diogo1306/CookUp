import React, { useState } from "react";
import { useLogin } from "../hooks/useLogin";
import { useUser } from "../context/UserContext";
import { LinearProgress } from "@mui/material";

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
      setUser(user.data);
      onLogin && onLogin(user);
      setShowSuccess(true);
    }
  }

  return (
    <form onSubmit={handleSubmit} style={{ maxWidth: 300, margin: "40px auto", display: "flex", flexDirection: "column", gap: 10 }} autoComplete="on">
      {loading && <LinearProgress sx={{ mb: 2 }} />}
      <h2>Login</h2>
      <label htmlFor="login-email" style={{ fontWeight: "bold" }}>
        Email
      </label>
      <input
        id="login-email"
        type="email"
        placeholder="Email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        required
        autoComplete="email"
      />
      <label htmlFor="login-senha" style={{ fontWeight: "bold" }}>
        Senha
      </label>
      <input
        id="login-senha"
        type="password"
        placeholder="Senha"
        value={senha}
        onChange={(e) => setSenha(e.target.value)}
        required
        autoComplete="current-password"
      />
      {error && <div style={{ color: "red" }}>{error}</div>}
      {success && showSuccess && <div style={{ color: "green" }}>Login realizado com sucesso!</div>}
      <button type="submit" disabled={loading}>
        Entrar
      </button>
    </form>
  );
}
