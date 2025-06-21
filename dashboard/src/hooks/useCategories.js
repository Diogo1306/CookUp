import { useState, useEffect } from "react";
import { getAllCategories } from "../api/your-api";

export function useCategories() {
  const [categories, setCategories] = useState([]);
  useEffect(() => {
    getAllCategories().then((data) => setCategories(data));
  }, []);
  return categories;
}
