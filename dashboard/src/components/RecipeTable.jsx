import React from "react";
import { DataGrid } from "@mui/x-data-grid";
import { Button, Box, Chip, Avatar, Tooltip } from "@mui/material";

export default function RecipeTable({ recipes, onEdit, onDelete }) {
  const columns = [
    {
      field: "image",
      headerName: "Foto",
      width: 90,
      renderCell: (params) =>
        params.value ? (
          <Avatar src={params.value} variant="rounded" alt="Foto receita" sx={{ width: 56, height: 56 }} />
        ) : (
          <Avatar variant="rounded" sx={{ width: 56, height: 56 }}>
            ?
          </Avatar>
        ),
      sortable: false,
      filterable: false,
    },
    { field: "id", headerName: "ID", width: 60 },
    { field: "title", headerName: "Título", width: 200 },
    {
      field: "author_name",
      headerName: "Autor",
      width: 120,
      renderCell: (params) => (params.value ? params.value : params.row.author_id),
    },
    {
      field: "categories",
      headerName: "Categorias",
      width: 200,
      renderCell: (params) => (
        <Box sx={{ display: "flex", gap: 0.5 }}>
          {(params.value || []).map((cat) => (
            <Tooltip title={cat.category_name} key={cat.category_id}>
              <Chip
                avatar={<Avatar src={cat.image_url} />}
                label={cat.category_name}
                size="small"
                sx={{
                  borderColor: cat.category_color,
                  borderWidth: 2,
                  borderStyle: "solid",
                  bgcolor: cat.category_color + "22",
                  color: "text_primary",
                  fontWeight: 500,
                  marginRight: 0.5,
                }}
              />
            </Tooltip>
          ))}
        </Box>
      ),
      sortable: false,
      filterable: false,
    },
    {
      field: "difficulty",
      headerName: "Dificuldade",
      width: 110,
      renderCell: (params) => (
        <Chip
          label={params.value}
          size="small"
          sx={{
            textTransform: "capitalize",
            fontWeight: 500,
            color: "black",
            bgcolor:
              params.value === "Fácil"
                ? "success.light"
                : params.value === "Médio"
                ? "warning.light"
                : params.value === "Difícil"
                ? "error.light"
                : "default",
          }}
        />
      ),
    },
    {
      field: "preparation_time",
      headerName: "Tempo (min)",
      width: 100,
    },
    {
      field: "servings",
      headerName: "Porções",
      width: 80,
    },
    {
      field: "actions",
      headerName: "Ações",
      width: 160,
      sortable: false,
      filterable: false,
      renderCell: (params) => (
        <Box sx={{ display: "flex", gap: 1 }}>
          <Button color="warning" size="small" onClick={() => onEdit(params.row)} aria-label={`Editar ${params.row.title}`}>
            Editar
          </Button>
          <Button color="error" size="small" onClick={() => onDelete(params.row.id)} aria-label={`Deletar ${params.row.title}`}>
            Deletar
          </Button>
        </Box>
      ),
    },
  ];

  console.log("RecipeTable recipes prop:", recipes);

  const rows = Array.isArray(recipes)
    ? recipes.map((r) => ({
        ...r,
        id: r.recipe_id,
        image: r.image || (r.gallery && r.gallery.length > 0 ? r.gallery[0] : null),
      }))
    : [];

  console.log("rows for DataGrid:", rows);

  return (
    <div style={{ height: 800, width: "100%" }}>
      <DataGrid rows={rows} columns={columns} pageSize={100} pageSizeOptions={[100]} disableRowSelectionOnClick getRowHeight={() => 65} />
    </div>
  );
}
