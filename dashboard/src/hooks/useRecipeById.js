import { useState } from "react";
import { getRecipeById } from "../api/recipe";

export function useRecipeById() {
  const [recipe, setRecipe] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  async function fetchRecipe(id) {
    setLoading(true);
    setError(null);
    try {
      const data = await getRecipeById(id);
      setRecipe(data);
      setLoading(false);
      return data;
    } catch (err) {
      setError("Erro ao buscar receita");
      setLoading(false);
      return null;
    }
  }

  return { recipe, loading, error, fetchRecipe, setRecipe };
}
