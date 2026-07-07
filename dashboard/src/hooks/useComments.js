import { useCallback, useState } from "react";
import { getAllComments, deleteComment } from "../api/comments";

export function useComments() {
  const [comments, setComments] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const loadComments = useCallback(async () => {
    setLoading(true);
    setError("");
    try {
      const data = await getAllComments();
      setComments(data);
    } catch (e) {
      setError("Erro ao carregar comentários.");
    } finally {
      setLoading(false);
    }
  }, []);

  const handleDelete = async (comment_id) => {
    setLoading(true);
    setError("");
    try {
      await deleteComment(comment_id);
      setComments((prev) => prev.filter((c) => c.comment_id !== comment_id));
    } catch {
      setError("Erro ao deletar comentário.");
    } finally {
      setLoading(false);
    }
  };

  return { comments, loading, error, loadComments, handleDelete };
}
