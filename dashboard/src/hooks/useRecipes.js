import { useState, useEffect, useCallback } from "react";
import { getAllRecipes, deleteRecipe, saveOrUpdateRecipe } from "../api/recipe";

export function useRecipes() {
  const [recipes, setRecipes] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchRecipes = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await getAllRecipes();
      setRecipes(data.data || []);
    } catch (err) {
      setError(err?.message || "Erro ao carregar receitas");
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchRecipes();
  }, [fetchRecipes]);

  const handleDelete = useCallback(
    async (id) => {
      setLoading(true);
      setError(null);
      try {
        await deleteRecipe(id);
        await fetchRecipes();
      } catch (err) {
        setError(err?.message || "Erro ao deletar receita");
      } finally {
        setLoading(false);
      }
    },
    [fetchRecipes]
  );

  const handleSave = useCallback(
    async (data, isEdit = false) => {
      setLoading(true);
      setError(null);
      try {
        await saveOrUpdateRecipe(data, isEdit);
        await fetchRecipes();
      } catch (err) {
        setError(err?.message || "Erro ao salvar receita");
      } finally {
        setLoading(false);
      }
    },
    [fetchRecipes]
  );

  return {
    recipes,
    loading,
    error,
    handleRefresh: fetchRecipes,
    fetchRecipes,
    handleDelete,
    handleSave,
  };
}
