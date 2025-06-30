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
    <Box sx={{ m: { xs: 1, md: 3 }, mt: 3, maxWidth: "100%" }}>
      {loading ? (
        <Box sx={{ display: "flex", justifyContent: "center", mt: 5 }}>
          <CircularProgress />
        </Box>
      ) : (
        <CommentsTable comments={comments} onDelete={handleDelete} onRefresh={loadComments} loading={loading} />
      )}
      {error && (
        <Typography color="error" mt={2}>
          {error}
        </Typography>
      )}
    </Box>
  );
}
