import axios from "axios";
import { auth } from "../firebase";
import { createUserWithEmailAndPassword, deleteUser as firebaseDeleteUser } from "firebase/auth";
import { deleteFirebaseUser, blockFirebaseUser, unblockFirebaseUser } from "./adminUser";

const API_URL = "http://192.168.0.26/PAP/CookUp_Core/public/api.php";

export async function getByUID(uid) {
  const res = await axios.get(API_URL, {
    params: { route: "user", firebase_uid: uid },
  });
  return res.data.data || res.data;
}

export async function getAllUsers() {
  const res = await axios.get(API_URL, {
    params: { route: "users" },
  });
  console.log("getAllUsers response", res.data);
  return res.data.data || [];
}

export async function setBlocked(user_id, blocked) {
  const formData = new FormData();
  formData.append("user_id", user_id);
  formData.append("blocked", blocked ? 1 : 0);
  const res = await axios.post(`${API_URL}?route=set_blocked`, formData);
  return res.data;
}

export async function createUser({ email, password, username, profile_picture, role = "user" }) {
  if (!email || !password || password.length < 6) {
    throw new Error("Email obrigatório e senha com pelo menos 6 caracteres.");
  }
  let cred;
  try {
    cred = await createUserWithEmailAndPassword(auth, email, password);
  } catch (err) {
    throw new Error("Erro ao criar usuário no Firebase: " + (err.message || err.code));
  }
  const res = await axios.post(`${API_URL}?route=user`, {
    firebase_uid: cred.user.uid,
    username,
    email,
    profile_picture,
    role,
  });
  return res.data;
}

// src/api/user.js
export async function updateUser({ user_id, username, email, role, profile_picture }) {
  const formData = new FormData();
  formData.append("user_id", user_id);
  formData.append("username", username);
  formData.append("email", email);
  formData.append("role", role);
  if (profile_picture) formData.append("profile_picture", profile_picture);

  const res = await axios.post(`${API_URL}?route=update_user_admin`, formData);
  return res.data;
}

export async function deleteUser(user_id, firebase_uid) {
  try {
    if (firebase_uid) {
      const firebaseRes = await deleteFirebaseUser(firebase_uid);
      if (!firebaseRes.success) throw new Error(firebaseRes.error || "Erro ao deletar no Firebase");
    }
  } catch (err) {
    throw new Error("Erro ao deletar usuário no Firebase: " + err.message);
  }
  const formData = new FormData();
  formData.append("user_id", user_id);
  const res = await axios.post(`${API_URL}?route=delete_user`, formData);
  return res.data;
}

export async function blockUser(user_id, firebase_uid) {
  try {
    const firebaseRes = await blockFirebaseUser(firebase_uid);
    if (!firebaseRes.success) throw new Error(firebaseRes.error || "Erro ao bloquear no Firebase");
    return await setBlocked(user_id, 1);
  } catch (err) {
    throw new Error("Erro ao bloquear usuário: " + err.message);
  }
}

export async function unblockUser(user_id, firebase_uid) {
  try {
    const firebaseRes = await unblockFirebaseUser(firebase_uid);
    if (!firebaseRes.success) throw new Error(firebaseRes.error || "Erro ao desbloquear no Firebase");
    return await setBlocked(user_id, 0);
  } catch (err) {
    throw new Error("Erro ao desbloquear usuário: " + err.message);
  }
}
