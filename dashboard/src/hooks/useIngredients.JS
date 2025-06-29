import { useState, useCallback } from "react";
import { getAllIngredients, createIngredient, updateIngredient, deleteIngredient } from "../../src/api/ingredients";

export function useIngredients() {
  const [ingredients, setIngredients] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const loadIngredients = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await getAllIngredients();
      setIngredients(data);
    } catch (err) {
      setError("Erro ao carregar ingredientes.");
    }
    setLoading(false);
  }, []);

  const handleCreate = async (ingredientData) => {
    setLoading(true);
    setError(null);
    try {
      const result = await createIngredient(ingredientData);
      if (result.success) {
        await loadIngredients();
        return { success: true };
      } else {
        setError(result.message || "Erro ao criar ingrediente.");
        return { success: false, message: result.message };
      }
    } catch (err) {
      setError("Erro ao criar ingrediente: " + err.message);
      return { success: false, message: err.message };
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = async (ingredientData) => {
    setLoading(true);
    setError(null);
    try {
      const result = await updateIngredient(ingredientData);
      if (result.success) {
        await loadIngredients();
        return { success: true };
      } else {
        setError(result.message || "Erro ao editar ingrediente.");
        return { success: false, message: result.message };
      }
    } catch (err) {
      setError("Erro ao editar ingrediente: " + err.message);
      return { success: false, message: err.message };
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (ingredient_id) => {
    setLoading(true);
    setError(null);
    try {
      const result = await deleteIngredient(ingredient_id);
      if (result.success) {
        await loadIngredients();
        return { success: true };
      } else {
        setError(result.message || "Erro ao deletar ingrediente.");
        return { success: false, message: result.message };
      }
    } catch (err) {
      setError("Erro ao deletar ingrediente: " + err.message);
      return { success: false, message: err.message };
    } finally {
      setLoading(false);
    }
  };

  return {
    ingredients,
    loading,
    error,
    loadIngredients,
    handleCreate,
    handleEdit,
    handleDelete,
    setIngredients,
  };
}
