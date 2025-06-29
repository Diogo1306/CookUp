import { useState, useCallback } from "react";
import { getAllCategories, createCategory, updateCategory, deleteCategory } from "../../src/api/categories";

export function useCategories() {
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const loadCategories = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await getAllCategories();
      setCategories(data);
    } catch {
      setError("Erro ao carregar categorias.");
    }
    setLoading(false);
  }, []);

  const handleCreate = async (cat) => {
    setLoading(true);
    setError(null);
    try {
      const res = await createCategory(cat);
      if (res.success) {
        await loadCategories();
        return { success: true };
      }
      setError(res.message || "Erro ao criar categoria.");
      return { success: false, message: res.message };
    } catch (err) {
      setError("Erro ao criar categoria: " + err.message);
      return { success: false, message: err.message };
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = async (cat) => {
    setLoading(true);
    setError(null);
    try {
      const res = await updateCategory(cat);
      if (res.success) {
        await loadCategories();
        return { success: true };
      }
      setError(res.message || "Erro ao editar categoria.");
      return { success: false, message: res.message };
    } catch (err) {
      setError("Erro ao editar categoria: " + err.message);
      return { success: false, message: err.message };
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (category_id) => {
    setLoading(true);
    setError(null);
    try {
      const res = await deleteCategory(category_id);
      if (res.success) {
        await loadCategories();
        return { success: true };
      }
      setError(res.message || "Erro ao deletar categoria.");
      return { success: false, message: res.message };
    } catch (err) {
      setError("Erro ao deletar categoria: " + err.message);
      return { success: false, message: err.message };
    } finally {
      setLoading(false);
    }
  };

  return {
    categories,
    loading,
    error,
    loadCategories,
    handleCreate,
    handleEdit,
    handleDelete,
    setCategories,
  };
}
