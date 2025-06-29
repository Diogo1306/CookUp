import { DataGrid } from "@mui/x-data-grid";
import { Button } from "@mui/material";

export default function CommentsTable({ comments, onDelete, loading }) {
  const columns = [
    { field: "comment_id", headerName: "ID", width: 70 },
    { field: "username", headerName: "Usuário", width: 140 },
    { field: "recipe_id", headerName: "Receita", width: 90 },
    { field: "comment", headerName: "Comentário", flex: 1, minWidth: 220 },
    { field: "created_at", headerName: "Data", width: 140 },
    {
      field: "actions",
      headerName: "Ações",
      width: 110,
      renderCell: (params) => (
        <Button color="error" size="small" variant="contained" onClick={() => onDelete(params.row.comment_id)}>
          Deletar
        </Button>
      ),
    },
  ];

  return (
    <DataGrid
      rows={comments.map((c) => ({ ...c, id: c.comment_id }))}
      columns={columns}
      autoHeight
      pageSize={10}
      rowsPerPageOptions={[10, 25, 50]}
      loading={loading}
      disableSelectionOnClick
    />
  );
}
