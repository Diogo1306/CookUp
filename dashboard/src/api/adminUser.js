import { auth } from "../firebase";

const API_URL = import.meta.env.VITE_ADMIN_URL || "http://localhost:4000";

// Cabeçalhos com o token Firebase do admin autenticado.
async function authHeaders(extra = {}) {
  const user = auth.currentUser;
  if (!user) throw new Error("Sessão expirada. Inicia sessão novamente.");
  const token = await user.getIdToken();
  return { Authorization: `Bearer ${token}`, ...extra };
}

export async function deleteFirebaseUser(uid) {
  const res = await fetch(`${API_URL}/user/${uid}`, {
    method: "DELETE",
    headers: await authHeaders(),
  });
  return res.json();
}

export async function updateFirebaseEmail(uid, email) {
  const res = await fetch(`${API_URL}/user/${uid}/email`, {
    method: "POST",
    headers: await authHeaders({ "Content-Type": "application/json" }),
    body: JSON.stringify({ email }),
  });
  return res.json();
}

export async function updateFirebasePassword(uid, password) {
  const res = await fetch(`${API_URL}/user/${uid}/password`, {
    method: "POST",
    headers: await authHeaders({ "Content-Type": "application/json" }),
    body: JSON.stringify({ password }),
  });
  return res.json();
}

export async function blockFirebaseUser(uid) {
  const res = await fetch(`${API_URL}/user/${uid}/block`, {
    method: "POST",
    headers: await authHeaders(),
  });
  return res.json();
}

export async function unblockFirebaseUser(uid) {
  const res = await fetch(`${API_URL}/user/${uid}/unblock`, {
    method: "POST",
    headers: await authHeaders(),
  });
  return res.json();
}
