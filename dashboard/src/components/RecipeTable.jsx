import React from "react";
import { DataGrid } from "@mui/x-data-grid";
import { Button, Box, Chip, Avatar, Tooltip, Typography, Stack, IconButton, useTheme } from "@mui/material";
import RefreshIcon from "@mui/icons-material/Refresh";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import FavoriteIcon from "@mui/icons-material/Favorite";
import VisibilityIcon from "@mui/icons-material/Visibility";
import CalendarMonthIcon from "@mui/icons-material/CalendarMonth";
import StarIcon from "@mui/icons-material/Star";

function formatDate(dateString) {
  if (!dateString) return "-";
  const d = new Date(dateString);
  return d.toLocaleDateString("pt-PT", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
  });
}

export default function RecipeTable({ recipes, onEdit, onDelete, onRefresh }) {
  const theme = useTheme();

  const columns = [
    {
      field: "image",
      headerName: "Foto",
      width: 60,
      align: "center",
      renderCell: (params) => (
        <Avatar src={params.value || undefined} variant="rounded" alt="Foto receita" sx={{ width: 42, height: 42, boxShadow: 1 }}>
          {!params.value && "?"}
        </Avatar>
      ),
    },
    { field: "id", headerName: "ID", width: 50 },
    { field: "title", headerName: "Título", minWidth: 140, flex: 1 },
    {
      field: "author_name",
      headerName: "Autor",
      minWidth: 100,
      flex: 0.6,
      renderCell: (params) => params.value || params.row.author_id,
    },
    {
      field: "categories",
      headerName: "Categorias",
      minWidth: 150,
      flex: 1,
      sortable: false,
      filterable: false,
      renderCell: (params) => (
        <Stack direction="row" spacing={0.5} useFlexGap flexWrap="wrap">
          {(params.value || []).map((cat) => (
            <Tooltip title={cat.category_name} key={cat.category_id}>
              <Chip
                avatar={cat.image_url && <Avatar src={cat.image_url} />}
                label={cat.category_name}
                size="small"
                sx={{
                  borderColor: cat.category_color,
                  borderWidth: 2,
                  borderStyle: "solid",
                  bgcolor: cat.category_color + "22",
                  fontWeight: 500,
                }}
              />
            </Tooltip>
          ))}
        </Stack>
      ),
    },
    {
      field: "difficulty",
      headerName: "Dificuldade",
      minWidth: 80,
      flex: 0.4,
      renderCell: (params) => (
        <Chip
          label={params.value}
          size="small"
          sx={{
            textTransform: "capitalize",
            color: "white",
            fontWeight: 600,
            bgcolor: params.value === "Fácil" ? "success.main" : params.value === "Médio" ? "warning.main" : "error.main",
          }}
        />
      ),
    },
    { field: "preparation_time", headerName: "Tempo (min)", width: 80 },
    { field: "servings", headerName: "Porções", width: 70 },
    {
      field: "views",
      headerName: "Visualizações",
      width: 90,
      renderCell: (params) => (
        <Stack direction="row" spacing={0.5} alignItems="center">
          <VisibilityIcon sx={{ fontSize: 16, color: "primary.main" }} />
          <Typography variant="body2" fontWeight={600}>
            {params.value ?? 0}
          </Typography>
        </Stack>
      ),
    },
    {
      field: "favorites",
      headerName: "Favoritos",
      width: 80,
      renderCell: (params) => (
        <Stack direction="row" spacing={0.5} alignItems="center">
          <FavoriteIcon sx={{ fontSize: 16, color: "error.main" }} />
          <Typography variant="body2" fontWeight={600}>
            {params.value ?? 0}
          </Typography>
        </Stack>
      ),
    },
    {
      field: "average_rating",
      headerName: "Média",
      width: 70,
      renderCell: (params) => (
        <Stack direction="row" spacing={0.5} alignItems="center">
          <StarIcon sx={{ fontSize: 16, color: "#FFD700" }} />
          <Typography variant="body2" fontWeight={600}>
            {params.value ? parseFloat(params.value).toFixed(1) : "-"}
          </Typography>
        </Stack>
      ),
    },
    {
      field: "ratings_count",
      headerName: "Avaliações",
      width: 80,
      renderCell: (params) => (
        <Typography variant="body2" fontWeight={600}>
          {params.value ?? 0}
        </Typography>
      ),
    },
    {
      field: "created_at",
      headerName: "Criada",
      width: 90,
      renderCell: (params) => (
        <Stack direction="row" spacing={0.5} alignItems="center">
          <CalendarMonthIcon sx={{ fontSize: 16, color: "info.main" }} />
          <Typography variant="body2">{formatDate(params.value)}</Typography>
        </Stack>
      ),
    },
    {
      field: "updated_at",
      headerName: "Atualizada",
      width: 90,
      renderCell: (params) => (
        <Stack direction="row" spacing={0.5} alignItems="center">
          <CalendarMonthIcon sx={{ fontSize: 16, color: "secondary.main" }} />
          <Typography variant="body2">{formatDate(params.value)}</Typography>
        </Stack>
      ),
    },
    {
      field: "actions",
      headerName: "Ações",
      width: 88,
      renderCell: (params) => (
        <Stack direction="row" spacing={0.5}>
          <Tooltip title="Editar">
            <IconButton size="small" color="primary" onClick={() => onEdit(params.row)}>
              <EditIcon fontSize="small" />
            </IconButton>
          </Tooltip>
          <Tooltip title="Eliminar">
            <IconButton size="small" color="error" onClick={() => onDelete(params.row.id)}>
              <DeleteIcon fontSize="small" />
            </IconButton>
          </Tooltip>
        </Stack>
      ),
    },
  ];

  const rows = Array.isArray(recipes)
    ? recipes.map((r) => ({
        ...r,
        id: r.recipe_id,
        image: r.image || (r.gallery && r.gallery[0]) || null,
        views: r.views_count ?? 0,
        favorites: r.favorites_count ?? 0,
        average_rating: r.average_rating ?? null,
        ratings_count: r.ratings_count ?? 0,
      }))
    : [];

  return (
    <Box
      sx={{
        width: "100%",
        p: 2,
        bgcolor: theme.palette.background.paper,
        borderRadius: 3,
        boxShadow: 3,
        mb: 4,
      }}
    >
      <Box sx={{ display: "flex", justifyContent: "space-between", mb: 2 }}>
        <Typography variant="h6" fontWeight={700} color="primary.main">
          Receitas
        </Typography>
        <Button
          onClick={onRefresh}
          startIcon={<RefreshIcon />}
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
      </Box>
      <DataGrid
        rows={rows}
        columns={columns}
        pageSize={25}
        rowsPerPageOptions={[10, 25, 50]}
        autoHeight
        disableRowSelectionOnClick
        sx={{
          background: theme.palette.background.default,
          borderRadius: 2,
          border: "none",
          "& .MuiDataGrid-columnHeaders": {
            bgcolor: "grey.100",
            fontWeight: 700,
            fontSize: 14,
            borderBottom: `1px solid ${theme.palette.divider}`,
          },
          "& .MuiDataGrid-cell": {
            py: 1,
            whiteSpace: "nowrap",
            overflow: "hidden",
            textOverflow: "ellipsis",
          },
          "& .MuiDataGrid-row:hover": {
            bgcolor: theme.palette.action.hover,
            boxShadow: "0px 2px 12px -5px rgba(0,0,0,0.1)",
          },
          "& .MuiDataGrid-footerContainer": {
            borderTop: `1px solid ${theme.palette.divider}`,
            bgcolor: "background.paper",
          },
        }}
        getRowHeight={() => 58}
      />
    </Box>
  );
}
