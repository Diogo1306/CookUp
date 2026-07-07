import axios from "axios";
const API_URL = import.meta.env.VITE_API_URL;

export async function getAllComments() {
  const res = await axios.get(API_URL, { params: { route: "comments_admin" } });
  return res.data.data || [];
}

export async function deleteComment(comment_id) {
  const formData = new FormData();
  formData.append("comment_id", comment_id);
  const res = await axios.post(`${API_URL}?route=delete_comment`, formData);
  return res.data;
}
