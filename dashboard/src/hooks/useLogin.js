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
      const firebaseUser = userCredential.user;
      const userData = await getByUID(firebaseUser.uid);

      console.log("Dados do usuário:", userData);

      if (userData && userData.data.role === "admin") {
        setUsuario(userData.data);
        setSuccess(true);
        setLoading(false);
        return userData.data;
      } else {
        setError("Acesso negado. Não é admin.");
        setLoading(false);
        return null;
      }
    } catch (e) {
      setError("Email ou senha inválidos");
      setLoading(false);
      return null;
    }
  }

  return { login, usuario, loading, error, success };
}
