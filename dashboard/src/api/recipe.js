import axios from "axios";

const API_URL = "http://192.168.0.26/PAP/CookUp_Core/public/api.php";

export async function getAllRecipes() {
  const res = await axios.get(API_URL, {
    params: { route: "recipes" },
  });
  return res.data;
}

export async function getRecipeById(id) {
  const res = await axios.get(API_URL, {
    params: { route: "recipe", recipe_id: id },
  });
  return res.data.recipe || res.data;
}

export async function deleteRecipe(id) {
  const formData = new FormData();
  formData.append("recipe_id", id);

  const res = await axios.post(`${API_URL}?route=delete_recipe`, formData);
  return res.data;
}

export async function saveOrUpdateRecipe(form, isEdit = false) {
  const formData = new FormData();

  formData.append("title", form.title);
  formData.append("description", form.description);
  formData.append("instructions", form.instructions);
  formData.append("difficulty", form.difficulty);
  formData.append("preparation_time", form.preparation_time);
  formData.append("servings", form.servings);
  if (isEdit && form.recipe_id) formData.append("recipe_id", form.recipe_id);

  formData.append("ingredients", JSON.stringify(form.ingredients));
  formData.append("categories", JSON.stringify(form.categories));

  const oldImages = form.gallery.filter((img) => typeof img === "string" && !img.startsWith("blob:"));
  formData.append("old_gallery", JSON.stringify(oldImages));

  form.gallery.forEach((img) => {
    if (img instanceof File) {
      formData.append("gallery[]", img);
    }
  });

  const res = await axios.post(`${API_URL}?route=recipes`, formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });
  return res.data;
}
