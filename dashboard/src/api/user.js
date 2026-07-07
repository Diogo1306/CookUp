import axios from "axios";
import { auth } from "../firebase";
import { createUserWithEmailAndPassword } from "firebase/auth";
import { deleteFirebaseUser, blockFirebaseUser, unblockFirebaseUser, updateFirebaseEmail, updateFirebasePassword } from "./adminUser";

const API_URL = import.meta.env.VITE_API_URL;

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
  const payload = {
    firebase_uid: cred.user.uid,
    username,
    email,
    role,
  };
  if (profile_picture) payload.profile_picture = profile_picture;

  const res = await axios.post(`${API_URL}?route=user`, payload);
  return res.data;
}

export async function updateUser({ firebase_uid, user_id, username, email, password, role, profile_picture }) {
  console.log("Password recebida:", password);

  if (email) {
    const emailRes = await updateFirebaseEmail(firebase_uid, email);
    if (!emailRes.success) throw new Error(emailRes.error || "Erro ao atualizar email no Firebase");
  }

  if (password) {
    const passRes = await updateFirebasePassword(firebase_uid, password);
    if (!passRes.success) throw new Error(passRes.error || "Erro ao atualizar password no Firebase");
  }

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
