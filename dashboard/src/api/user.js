import axios from "axios";
import { auth } from "../firebase";
import { createUserWithEmailAndPassword, deleteUser as firebaseDeleteUser } from "firebase/auth";

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

export async function createUser({ email, password, username, profile_picture, role = "user" }) {
  const cred = await createUserWithEmailAndPassword(auth, email, password || "123456");
  const res = await axios.post(`${API_URL}?route=user`, {
    firebase_uid: cred.user.uid,
    username,
    email,
    profile_picture,
    role,
  });
  console.log("createUser PHP backend:", res.data);
  return res.data;
}

export async function updateUser({ user_id, username, profile_picture, role = "user" }) {
  const formData = new FormData();
  formData.append("user_id", user_id);
  formData.append("username", username);
  if (profile_picture) formData.append("profile_picture", profile_picture);
  formData.append("role", role);

  const res = await axios.post(`${API_URL}?route=update_profile`, formData);
  console.log("updateUser PHP backend:", res.data);
  return res.data;
}

export async function deleteUser(user_id, firebase_uid = null) {
  const formData = new FormData();
  formData.append("user_id", user_id);
  const res = await axios.post(`${API_URL}?route=delete_user`, formData);
  return res.data;
}
