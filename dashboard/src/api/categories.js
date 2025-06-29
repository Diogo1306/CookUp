import axios from "axios";
const API_URL = "http://192.168.0.26/PAP/CookUp_Core/public/api.php";

export async function getAllCategories() {
  const res = await axios.get(API_URL, { params: { route: "categories" } });
  return res.data.data || [];
}

export async function createCategory({ category_name, color_hex, image_file }) {
  const formData = new FormData();
  formData.append("category_name", category_name);
  if (color_hex) formData.append("color_hex", color_hex);
  if (image_file) formData.append("image_url", image_file);

  const res = await axios.post(`${API_URL}?route=create_category`, formData);
  return res.data;
}

export async function updateCategory({ category_id, category_name, color_hex, image_file, image_url }) {
  const formData = new FormData();
  formData.append("category_id", category_id);
  formData.append("category_name", category_name);
  if (color_hex) formData.append("color_hex", color_hex);
  if (image_file) {
    formData.append("image_url", image_file);
  } else if (image_url) {
    formData.append("image_url", image_url);
  }
  const res = await axios.post(`${API_URL}?route=update_category`, formData);
  return res.data;
}

export async function deleteCategory(category_id) {
  const formData = new FormData();
  formData.append("category_id", category_id);
  const res = await axios.post(`${API_URL}?route=delete_category`, formData);
  return res.data;
}
