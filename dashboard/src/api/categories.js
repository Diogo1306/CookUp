import axios from "axios";

const API_URL = "http://192.168.0.26/PAP/CookUp_Core/public/api.php";

export async function getAllCategories() {
  const res = await axios.get(API_URL, {
    params: { route: "categories" },
  });
  return res.data.data || [];
}
