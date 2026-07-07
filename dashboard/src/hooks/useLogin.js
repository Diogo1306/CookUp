import { useState } from "react";
import { signInWithEmailAndPassword } from "firebase/auth";
import { auth } from "../firebase";
import { getByUID } from "../api/user";

export function useLogin() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState(false);
  const [usuario, setUsuario] = useState(null);

  async function login(email, senha) {
    setError("");
    setSuccess(false);
    setLoading(true);

    try {
      const userCredential = await signInWithEmailAndPassword(auth, email, senha);
      console.log("Firebase OK:", userCredential);
      const firebaseUser = userCredential.user;
      const userData = await getByUID(firebaseUser.uid);
      console.log("getByUID retornou:", userData);

      if (userData && userData.role === "admin") {
        setUsuario(userData);
        setSuccess(true);
        setLoading(false);
        return userData;
      } else {
        setError("Acesso negado. Não é admin.");
        setLoading(false);
        return null;
      }
    } catch (e) {
      console.log("ERRO NO LOGIN:", e);
      setError("Email ou senha inválidos");
      setLoading(false);
      return null;
    }
  }

  return { login, usuario, loading, error, success };
}
