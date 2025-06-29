import axios from "axios";
const API_URL = "http://192.168.0.26/PAP/CookUp_Core/public/api.php";

export async function getAllIngredients() {
  const res = await axios.get(API_URL, { params: { route: "ingredients" } });
  return res.data.data || [];
}

export async function createIngredient({ ingredient_name, image_file }) {
  const formData = new FormData();
  formData.append("ingredient_name", ingredient_name);
  if (image_file) formData.append("image_url", image_file);

  const res = await axios.post(`${API_URL}?route=create_ingredient`, formData);
  return res.data;
}

export async function updateIngredient({ ingredient_id, ingredient_name, oldImage, newImage }) {
  const formData = new FormData();
  formData.append("ingredient_id", ingredient_id);
  formData.append("ingredient_name", ingredient_name);

  if (newImage) {
    formData.append("image_url", newImage);
  } else if (oldImage) {
    formData.append("image_url", oldImage);
  }

  const res = await axios.post(`${API_URL}?route=update_ingredient`, formData);
  return res.data;
}

export async function deleteIngredient(ingredient_id) {
  const formData = new FormData();
  formData.append("ingredient_id", ingredient_id);
  const res = await axios.post(`${API_URL}?route=delete_ingredient`, formData);
  return res.data;
}
