import { useState, useCallback } from "react";
import { getAllUsers, createUser, updateUser, deleteUser } from "../api/user";

export function useUsers() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const loadUsers = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await getAllUsers();
      console.log("Users loaded:", data);
      setUsers(data);
    } catch (err) {
      setError("Erro ao carregar usuários.");
    }
    setLoading(false);
  }, []);

  const handleCreate = async (userData) => {
    setLoading(true);
    setError(null);
    try {
      const result = await createUser(userData);
      if (result.success) {
        await loadUsers();
        return { success: true };
      } else {
        setError(result.message || "Erro ao criar usuário.");
        return { success: false, message: result.message };
      }
    } catch (err) {
      setError("Erro ao criar usuário.");
      return { success: false, message: err.message };
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = async (userData) => {
    setError(null);
    try {
      const result = await updateUser(userData);
      if (result.success) {
        await loadUsers();
        return { success: true };
      } else {
        setError(result.message || "Erro ao editar usuário.");
        return { success: false, message: result.message };
      }
    } catch (err) {
      setError("Erro ao editar usuário.");
      return { success: false, message: err.message };
    }
  };

  const handleDelete = async (user_id) => {
    setLoading(true);
    setError(null);
    try {
      const result = await deleteUser(user_id);
      if (result.success) {
        await loadUsers();
        return { success: true };
      } else {
        setError(result.message || "Erro ao deletar usuário.");
        return { success: false, message: result.message };
      }
    } catch (err) {
      setError("Erro ao deletar usuário.");
      return { success: false, message: err.message };
    } finally {
      setLoading(false);
    }
  };

  return {
    users,
    loading,
    error,
    loadUsers,
    handleCreate,
    handleEdit,
    handleDelete,
    setUsers,
  };
}
