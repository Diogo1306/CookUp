import React from "react";
import { DataGrid } from "@mui/x-data-grid";
import { Button, Box, Tooltip, Typography, Stack, IconButton } from "@mui/material";
import RefreshIcon from "@mui/icons-material/Refresh";
import DeleteIcon from "@mui/icons-material/Delete";

export default function CommentsTable({ comments, onDelete, onRefresh, loading }) {
  const columns = [
    { field: "comment_id", headerName: "ID", width: 70 },
    { field: "username", headerName: "Usuário", width: 140 },
    { field: "recipe_id", headerName: "Receita", width: 90 },
    { field: "comment", headerName: "Comentário", flex: 1, minWidth: 220 },
    { field: "created_at", headerName: "Data", width: 140 },
    {
      field: "actions",
      headerName: "Ações",
      minWidth: 80,
      align: "center",
      headerAlign: "center",
      sortable: false,
      filterable: false,
      renderCell: (params) => (
        <Stack direction="row" spacing={1}>
          <Tooltip title="Eliminar">
            <IconButton
              color="error"
              size="small"
              onClick={() => onDelete(params.row.comment_id)}
              aria-label={`Eliminar comentário #${params.row.comment_id}`}
            >
              <DeleteIcon fontSize="small" />
            </IconButton>
          </Tooltip>
        </Stack>
      ),
    },
  ];

  return (
    <Box
      sx={{
        width: "100%",
        p: { xs: 1, sm: 2 },
        bgcolor: (theme) => theme.palette.background.paper,
        borderRadius: 3,
        boxShadow: 2,
        mb: 4,
        overflowX: "auto",
      }}
    >
      <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center", mb: 2 }}>
        <Typography variant="h6" fontWeight={700} color="primary.main">
          Comentários
        </Typography>
        <Stack direction="row" spacing={2}>
          <Button
            variant="contained"
            color="success"
            onClick={onRefresh}
            startIcon={<RefreshIcon />}
            disabled={loading}
            sx={{
              borderRadius: 2,
              fontWeight: 600,
              textTransform: "none",
              boxShadow: 1,
              bgcolor: "success.light",
              color: "success.dark",
              "&:hover": {
                bgcolor: "success.main",
                color: "#fff",
              },
            }}
          >
            Atualizar
          </Button>
        </Stack>
      </Box>
      <DataGrid
        rows={comments.map((c) => ({ ...c, id: c.comment_id }))}
        columns={columns}
        autoHeight
        pageSize={10}
        rowsPerPageOptions={[10, 25, 50]}
        loading={loading}
        disableSelectionOnClick
        sx={{
          width: "100%",
          minWidth: 600,
          background: (theme) => theme.palette.background.default,
          borderRadius: 2,
          border: "none",
          "& .MuiDataGrid-columnHeaders": {
            bgcolor: "grey.50",
            fontWeight: 700,
            fontSize: "1rem",
            boxShadow: "0 2px 8px -6px #0003",
          },
          "& .MuiDataGrid-cell": {
            py: 1,
            transition: "background 0.15s",
            whiteSpace: "nowrap",
            maxWidth: 240,
            overflow: "hidden",
            textOverflow: "ellipsis",
          },
          "& .MuiDataGrid-row": {
            transition: "background 0.15s",
            "&:hover": {
              bgcolor: (theme) => (theme.palette.mode === "dark" ? "#23272c" : "#f6f6f9"),
              boxShadow: "0 2px 18px -10px #0002",
              zIndex: 1,
            },
          },
          "& .MuiDataGrid-footerContainer": {
            borderTop: "none",
            bgcolor: "background.paper",
          },
        }}
        getRowHeight={() => 60}
      />
    </Box>
  );
}
