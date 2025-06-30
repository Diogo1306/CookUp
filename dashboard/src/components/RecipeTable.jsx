import React from "react";
import { DataGrid } from "@mui/x-data-grid";
import { Button, Box, Chip, Avatar, Tooltip, Typography, Stack, IconButton } from "@mui/material";
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
  return d.toLocaleDateString("pt-PT", { day: "2-digit", month: "2-digit", year: "numeric" });
}

export default function RecipeTable({ recipes, onEdit, onDelete, onRefresh }) {
  const columns = [
    {
      field: "image",
      headerName: "Foto",
      width: 80,
      align: "center",
      sortable: false,
      filterable: false,
      renderCell: (params) =>
        params.value ? (
          <Avatar src={params.value} variant="rounded" alt="Foto receita" sx={{ width: 54, height: 54, boxShadow: 1 }} />
        ) : (
          <Avatar variant="rounded" sx={{ width: 54, height: 54, bgcolor: "grey.200", color: "grey.700" }}>
            ?
          </Avatar>
        ),
    },
    { field: "id", headerName: "ID", width: 60 },
    { field: "title", headerName: "Título", flex: 1, minWidth: 150 },
    {
      field: "author_name",
      headerName: "Autor",
      minWidth: 110,
      flex: 0.5,
      renderCell: (params) => (params.value ? params.value : params.row.author_id),
    },
    {
      field: "categories",
      headerName: "Categorias",
      minWidth: 170,
      flex: 1,
      renderCell: (params) => (
        <Box sx={{ display: "flex", gap: 0.5, flexWrap: "wrap" }}>
          {(params.value || []).map((cat) => (
            <Tooltip title={cat.category_name} key={cat.category_id}>
              <Chip
                avatar={cat.image_url ? <Avatar src={cat.image_url} /> : undefined}
                label={cat.category_name}
                size="small"
                sx={{
                  borderColor: cat.category_color,
                  borderWidth: 2,
                  borderStyle: "solid",
                  bgcolor: cat.category_color + "22",
                  color: "text.primary",
                  fontWeight: 500,
                  mr: 0.5,
                  mb: 0.3,
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
      minWidth: 90,
      flex: 0.5,
      renderCell: (params) => (
        <Chip
          label={params.value}
          size="small"
          sx={{
            textTransform: "capitalize",
            fontWeight: 500,
            color: "white",
            bgcolor:
              params.value === "Fácil"
                ? "success.main"
                : params.value === "Médio"
                ? "warning.main"
                : params.value === "Difícil"
                ? "error.main"
                : "default",
          }}
        />
      ),
    },
    {
      field: "preparation_time",
      headerName: "Tempo (min)",
      width: 90,
    },
    {
      field: "servings",
      headerName: "Porções",
      width: 80,
    },
    {
      field: "views",
      headerName: "Visualizações",
      minWidth: 80,
      flex: 0.5,
      align: "center",
      renderCell: (params) => (
        <Stack direction="row" spacing={0.5} alignItems="center">
          <VisibilityIcon sx={{ color: "primary.main", fontSize: 18 }} />
          <Typography variant="body2" fontWeight={600}>
            {params.value ?? 0}
          </Typography>
        </Stack>
      ),
    },
    {
      field: "favorites",
      headerName: "Favoritos",
      minWidth: 80,
      flex: 0.5,
      align: "center",
      renderCell: (params) => (
        <Stack direction="row" spacing={0.5} alignItems="center">
          <FavoriteIcon sx={{ color: "error.main", fontSize: 18 }} />
          <Typography variant="body2" fontWeight={600}>
            {params.value ?? 0}
          </Typography>
        </Stack>
      ),
    },
    {
      field: "average_rating",
      headerName: "Média",
      minWidth: 70,
      flex: 0.4,
      align: "center",
      renderCell: (params) =>
        params.value !== null && params.value !== undefined ? (
          <Stack direction="row" spacing={0.5} alignItems="center">
            <StarIcon sx={{ color: "#FFD700", fontSize: 18 }} />
            <Typography variant="body2" fontWeight={600}>
              {parseFloat(params.value).toFixed(1)}
            </Typography>
          </Stack>
        ) : (
          "-"
        ),
    },
    {
      field: "ratings_count",
      headerName: "Avaliações",
      minWidth: 70,
      flex: 0.4,
      align: "center",
      renderCell: (params) => (
        <Typography variant="body2" fontWeight={600}>
          {params.value ?? 0}
        </Typography>
      ),
    },
    {
      field: "created_at",
      headerName: "Criada em",
      minWidth: 92,
      flex: 0.5,
      renderCell: (params) => (
        <Stack direction="row" spacing={0.5} alignItems="center">
          <CalendarMonthIcon sx={{ color: "info.main", fontSize: 18 }} />
          <Typography variant="body2">{formatDate(params.value)}</Typography>
        </Stack>
      ),
    },
    {
      field: "updated_at",
      headerName: "Atualizada em",
      minWidth: 92,
      flex: 0.5,
      renderCell: (params) => (
        <Stack direction="row" spacing={0.5} alignItems="center">
          <CalendarMonthIcon sx={{ color: "secondary.main", fontSize: 18 }} />
          <Typography variant="body2">{formatDate(params.value)}</Typography>
        </Stack>
      ),
    },
    {
      field: "actions",
      headerName: "Ações",
      minWidth: 100,
      align: "center",
      headerAlign: "center",
      sortable: false,
      filterable: false,
      renderCell: (params) => (
        <Stack direction="row" spacing={1}>
          <Tooltip title="Editar">
            <IconButton color="primary" size="small" onClick={() => onEdit(params.row)} aria-label={`Editar ${params.row.title}`}>
              <EditIcon fontSize="small" />
            </IconButton>
          </Tooltip>
          <Tooltip title="Eliminar">
            <IconButton color="error" size="small" onClick={() => onDelete(params.row.id)} aria-label={`Eliminar ${params.row.title}`}>
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
        image: r.image || (r.gallery && r.gallery.length > 0 ? r.gallery[0] : null),
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
        p: { xs: 1, sm: 2 },
        bgcolor: (theme) => theme.palette.background.paper,
        borderRadius: 3,
        boxShadow: 2,
        mb: 4,
        overflowX: "false",
      }}
    >
      <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center", mb: 2 }}>
        <Typography variant="h6" fontWeight={700} color="primary.main">
          Receitas
        </Typography>
        <Stack direction="row" spacing={2}>
          <Button
            variant="contained"
            color="success"
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
        </Stack>
      </Box>
      <DataGrid
        rows={rows}
        columns={columns}
        pageSize={25}
        rowsPerPageOptions={[10, 25, 50, 100]}
        disableRowSelectionOnClick
        autoHeight
        sx={{
          width: "100%",
          minWidth: 700,
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
        getRowHeight={() => 66}
      />
    </Box>
  );
}
