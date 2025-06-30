import { useState, useCallback } from "react";
import { getAllUsers, createUser, updateUser, deleteUser, blockUser, unblockUser } from "../api/user";

export function useUsers() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const loadUsers = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await getAllUsers();
      setUsers(data || []);
    } catch (err) {
      setError("Erro ao carregar utilizadores.");
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
        setError(result.message || "Erro ao criar utilizador.");
        return { success: false, message: result.message };
      }
    } catch (err) {
      setError("Erro ao criar utilizador: " + err.message);
      return { success: false, message: err.message };
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = async (userData) => {
    setLoading(true);
    setError(null);
    try {
      const result = await updateUser(userData);
      if (result.success) {
        await loadUsers();
        return { success: true };
      } else {
        setError(result.message || "Erro ao editar utilizador.");
        return { success: false, message: result.message };
      }
    } catch (err) {
      setError("Erro ao editar utilizador: " + err.message);
      return { success: false, message: err.message };
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (user_id, firebase_uid) => {
    if (!user_id) {
      setError("ID do utilizador inválido.");
      return { success: false, message: "ID do utilizador inválido." };
    }
    setLoading(true);
    setError(null);
    try {
      const result = await deleteUser(user_id, firebase_uid);
      if (result.success) {
        await loadUsers();
        return { success: true };
      } else {
        setError(result.message || "Erro ao apagar utilizador.");
        return { success: false, message: result.message };
      }
    } catch (err) {
      setError("Erro ao apagar utilizador: " + err.message);
      return { success: false, message: err.message };
    } finally {
      setLoading(false);
    }
  };

  const handleBlock = async (user_id, firebase_uid) => {
    if (!user_id || !firebase_uid) {
      setError("IDs inválidos para bloquear!");
      return;
    }
    setLoading(true);
    setError(null);
    try {
      await blockUser(user_id, firebase_uid);
      await loadUsers();
    } catch (err) {
      setError("Erro ao bloquear utilizador.");
    } finally {
      setLoading(false);
    }
  };

  const handleUnblock = async (user_id, firebase_uid) => {
    if (!user_id || !firebase_uid) {
      setError("IDs inválidos para desbloquear!");
      return;
    }
    setLoading(true);
    setError(null);
    try {
      await unblockUser(user_id, firebase_uid);
      await loadUsers();
    } catch (err) {
      setError("Erro ao desbloquear utilizador.");
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
