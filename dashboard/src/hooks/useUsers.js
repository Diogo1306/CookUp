import { useState, useCallback } from "react";
import { getAllUsers, createUser, updateUser, deleteUser, blockUser, unblockUser } from "../api/user"; // Corrigido aqui

export function useUsers() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const handleBlock = async (user_id, firebase_uid) => {
    setLoading(true);
    setError(null);
    try {
      await blockUser(user_id, firebase_uid);
      await loadUsers();
    } catch (err) {
      setError("Erro ao bloquear usuário.");
    } finally {
      setLoading(false);
    }
  };

  const handleUnblock = async (user_id, firebase_uid) => {
    setLoading(true);
    setError(null);
    try {
      await unblockUser(user_id, firebase_uid);
      await loadUsers();
    } catch (err) {
      setError("Erro ao desbloquear usuário.");
    } finally {
      setLoading(false);
    }
  };

  const loadUsers = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await getAllUsers();
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
      setError("Erro ao criar usuário: " + err.message);
      return { success: false, message: err.message };
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = async (userData) => {
    setLoading(true); // Adicione aqui para loading correto
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
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (user_id, firebase_uid) => {
    setLoading(true);
    setError(null);
    try {
      const result = await deleteUser(user_id, firebase_uid);
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
    handleBlock,
    handleUnblock,
    setUsers,
  };
}
