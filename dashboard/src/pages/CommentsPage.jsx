import { useEffect } from "react";
import { useComments } from "../hooks/useComments";
import { Typography, Box, CircularProgress } from "@mui/material";
import CommentsTable from "../components/CommentsTable";

export default function CommentsPage() {
  const { comments, loading, error, loadComments, handleDelete } = useComments();

  useEffect(() => {
    loadComments();
  }, [loadComments]);

  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        Comentários
      </Typography>
      {loading ? (
        <Box sx={{ display: "flex", justifyContent: "center", mt: 4 }}>
          <CircularProgress />
        </Box>
      ) : (
        <CommentsTable comments={comments} onDelete={handleDelete} loading={loading} />
      )}
      {error && <Typography color="error">{error}</Typography>}
    </Box>
  );
}
