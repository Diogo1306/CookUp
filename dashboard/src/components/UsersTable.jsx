import { useState } from "react";
import { DataGrid } from "@mui/x-data-grid";
import { Avatar, Box, Chip, Typography, CircularProgress, Tooltip, Stack, IconButton, Button } from "@mui/material";
import RefreshIcon from "@mui/icons-material/Refresh";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import LockIcon from "@mui/icons-material/Lock";
import LockOpenIcon from "@mui/icons-material/LockOpen";

export default function UsersTable({ users = [], onEdit, onDelete, onBlock, onUnblock, reloadUsers, loading = false }) {
  const [loadingRow, setLoadingRow] = useState(null);

  const rows = users.map((user) => ({
    ...user,
    id: user.user_id,
    blocked: user.blocked === "1" || user.blocked === 1,
    firebase_uid: user.firebase_uid || "",
  }));

  const columns = [
    {
      field: "profile_picture",
      headerName: "Foto",
      width: 70,
      renderCell: (params) => (
        <Avatar src={params.value} imgProps={{ referrerPolicy: "no-referrer" }} sx={{ width: 38, height: 38, boxShadow: 1, bgcolor: "grey.100" }} />
      ),
      sortable: false,
      filterable: false,
      align: "center",
      headerAlign: "center",
    },
    { field: "username", headerName: "Nome", width: 150 },
    { field: "email", headerName: "Email", width: 180 },
    { field: "user_id", headerName: "ID", width: 80 },
    {
      field: "role",
      headerName: "Função",
      width: 90,
      renderCell: (params) => (
        <Chip
          label={params.value}
          size="small"
          sx={{
            fontWeight: 500,
            bgcolor: params.value === "admin" ? "secondary.main" : "grey.200",
            color: params.value === "admin" ? "white" : "text.primary",
            textTransform: "capitalize",
          }}
        />
      ),
    },
    {
      field: "blocked",
      headerName: "Status",
      width: 120,
      renderCell: (params) =>
        params.value ? (
          <Chip label="Bloqueado" color="error" size="small" icon={<LockIcon fontSize="small" />} />
        ) : (
          <Chip label="Ativo" color="success" size="small" icon={<LockOpenIcon fontSize="small" />} />
        ),
      sortable: false,
      filterable: false,
    },
    {
      field: "actions",
      headerName: "Ações",
      width: 180,
      align: "center",
      headerAlign: "center",
      sortable: false,
      filterable: false,
      renderCell: (params) => (
        <Stack direction="row" spacing={1}>
          <Tooltip title="Editar">
            <span>
              <IconButton
                size="small"
                color="primary"
                onClick={() => onEdit?.(params.row)}
                disabled={loadingRow}
                aria-label={`Editar ${params.row.username}`}
              >
                <EditIcon fontSize="small" />
              </IconButton>
            </span>
          </Tooltip>
          <Tooltip title="Eliminar">
            <span>
              <IconButton
                size="small"
                color="error"
                onClick={async () => {
                  if (!params.row.id || !params.row.firebase_uid) {
                    alert("IDs inválidos!");
                    return;
                  }
                  if (window.confirm("Tem certeza que deseja deletar no Firebase Auth e MySQL?")) {
                    setLoadingRow(params.row.id);
                    try {
                      await onDelete?.(params.row.id, params.row.firebase_uid);
                    } catch (e) {
                      alert("Erro ao deletar: " + e.message);
                    }
                    setLoadingRow(null);
                  }
                }}
                disabled={loadingRow === params.row.id}
                aria-label={`Eliminar ${params.row.username}`}
              >
                {loadingRow === params.row.id ? <CircularProgress size={16} color="inherit" /> : <DeleteIcon fontSize="small" />}
              </IconButton>
            </span>
          </Tooltip>
          {params.row.blocked ? (
            <Tooltip title="Desbloquear">
              <span>
                <IconButton
                  size="small"
                  color="warning"
                  onClick={async () => {
                    if (!params.row.id || !params.row.firebase_uid) {
                      alert("IDs inválidos!");
                      return;
                    }
                    setLoadingRow(params.row.id + "_unblock");
                    try {
                      await onUnblock?.(params.row.id, params.row.firebase_uid);
                    } catch (e) {
                      alert("Erro ao desbloquear: " + e.message);
                    }
                    setLoadingRow(null);
                  }}
                  disabled={loadingRow === params.row.id + "_unblock"}
                  aria-label={`Desbloquear ${params.row.username}`}
                >
                  {loadingRow === params.row.id + "_unblock" ? <CircularProgress size={16} color="inherit" /> : <LockOpenIcon fontSize="small" />}
                </IconButton>
              </span>
            </Tooltip>
          ) : (
            <Tooltip title="Bloquear">
              <span>
                <IconButton
                  size="small"
                  color="warning"
                  onClick={async () => {
                    if (!params.row.id || !params.row.firebase_uid) {
                      alert("IDs inválidos!");
                      return;
                    }
                    setLoadingRow(params.row.id + "_block");
                    try {
                      await onBlock?.(params.row.id, params.row.firebase_uid);
                    } catch (e) {
                      alert("Erro ao bloquear: " + e.message);
                    }
                    setLoadingRow(null);
                  }}
                  disabled={loadingRow === params.row.id + "_block"}
                  aria-label={`Bloquear ${params.row.username}`}
                >
                  {loadingRow === params.row.id + "_block" ? <CircularProgress size={16} color="inherit" /> : <LockIcon fontSize="small" />}
                </IconButton>
              </span>
            </Tooltip>
          )}
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
      {/* Header e botão atualizar */}
      <Box
        sx={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          mb: 2,
        }}
      >
        <Typography variant="h6" fontWeight={700} color="primary.main">
          Utilizadores
        </Typography>
        <Tooltip title="Atualizar">
          <span>
            <Button
              variant="contained"
              color="success"
              startIcon={loading ? <CircularProgress size={18} color="inherit" /> : <RefreshIcon />}
              onClick={reloadUsers}
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
              aria-label="Atualizar utilizadores"
            >
              Atualizar
            </Button>
          </span>
        </Tooltip>
      </Box>
      <DataGrid
        rows={rows}
        columns={columns}
        pageSize={8}
        rowsPerPageOptions={[8, 20, 100]}
        disableSelectionOnClick
        autoHeight
        loading={loading}
        sx={{
          width: "100%",
          minWidth: 700,
          background: (theme) => theme.palette.background.default,
          borderRadius: 2,
          border: "none",
          "& .MuiDataGrid-row": {
            transition: "background 0.15s",
            "&:hover": {
              bgcolor: (theme) => (theme.palette.mode === "dark" ? "#23272c" : "#f6f6f9"),
              boxShadow: "0 2px 18px -10px #0002",
              zIndex: 1,
            },
          },
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
          "& .MuiDataGrid-footerContainer": {
            borderTop: "none",
            bgcolor: "background.paper",
          },
        }}
      />
    </Box>
  );
}
