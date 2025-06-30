import React from "react";
import { DataGrid } from "@mui/x-data-grid";
import { Button, Box, Avatar, Tooltip, Typography, Stack, IconButton } from "@mui/material";
import RefreshIcon from "@mui/icons-material/Refresh";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";

export default function IngredientsTable({ ingredients, onEdit, onDelete, onRefresh, loading }) {
  const rows = (ingredients || []).map((ing) => ({
    ...ing,
    id: ing.ingredient_id,
  }));

  const columns = [
    {
      field: "ingredient_image_url",
      headerName: "Foto",
      width: 70,
      align: "center",
      renderCell: (params) =>
        params.value ? (
          <Avatar src={params.value} imgProps={{ referrerPolicy: "no-referrer" }} sx={{ width: 40, height: 40, boxShadow: 1 }} />
        ) : (
          <Avatar sx={{ width: 40, height: 40, bgcolor: "grey.200", color: "grey.700" }}>?</Avatar>
        ),
      sortable: false,
      filterable: false,
    },
    { field: "ingredient_name", headerName: "Nome", flex: 1, minWidth: 150 },
    {
      field: "actions",
      headerName: "Ações",
      minWidth: 110,
      align: "center",
      headerAlign: "center",
      sortable: false,
      filterable: false,
      renderCell: (params) => (
        <Stack direction="row" spacing={1}>
          <Tooltip title="Editar">
            <IconButton color="primary" size="small" onClick={() => onEdit(params.row)} aria-label={`Editar ${params.row.ingredient_name}`}>
              <EditIcon fontSize="small" />
            </IconButton>
          </Tooltip>
          <Tooltip title="Eliminar">
            <IconButton
              color="error"
              size="small"
              onClick={() => onDelete(params.row.ingredient_id)}
              aria-label={`Eliminar ${params.row.ingredient_name}`}
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
          Ingredientes
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
        rows={rows}
        columns={columns}
        pageSize={8}
        rowsPerPageOptions={[8, 20, 100]}
        disableRowSelectionOnClick
        autoHeight
        loading={loading}
        sx={{
          width: "100%",
          minWidth: 400,
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
            maxWidth: 200,
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
