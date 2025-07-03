import { useState } from "react";
import { DataGrid } from "@mui/x-data-grid";
import { Avatar, Box, Chip, Typography, CircularProgress, Tooltip, Stack, IconButton, Button, useTheme } from "@mui/material";
import RefreshIcon from "@mui/icons-material/Refresh";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import LockIcon from "@mui/icons-material/Lock";
import LockOpenIcon from "@mui/icons-material/LockOpen";

export default function UsersTable({ users = [], onEdit, onDelete, onBlock, onUnblock, reloadUsers, loading = false }) {
  const theme = useTheme();
  const [loadingRow, setLoadingRow] = useState(null);

  const rows = users.map((user) => ({
    ...user,
    id: user.user_id,
    firebase_uid: user.firebase_uid || "",
    blocked: user.blocked === "1" || user.blocked === 1,
  }));

  const columns = [
    {
      field: "profile_picture",
      headerName: "Foto",
      width: 60,
      renderCell: (params) => (
        <Avatar src={params.value} imgProps={{ referrerPolicy: "no-referrer" }} sx={{ width: 36, height: 36, boxShadow: 1, bgcolor: "grey.100" }} />
      ),
      sortable: false,
      filterable: false,
      align: "center",
      headerAlign: "center",
    },
    { field: "username", headerName: "Nome", minWidth: 130, flex: 1 },
    { field: "email", headerName: "Email", minWidth: 180, flex: 1.2 },
    { field: "user_id", headerName: "ID", width: 80 },
    {
      field: "firebase_uid", // Nova coluna para o firebase_uid
      headerName: "Firebase UID",
      minWidth: 150,
      flex: 1,
      renderCell: (params) => (
        <Typography variant="body2" color="textSecondary">
          {params.value}
        </Typography>
      ),
    },
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
            bgcolor: params.value === "admin" ? "secondary.main" : "green",
            color: params.value === "admin" ? "white" : "white",
            textTransform: "capitalize",
          }}
        />
      ),
    },
    {
      field: "blocked",
      headerName: "Status",
      width: 100,
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
      minWidth: 160,
      renderCell: (params) => (
        <Stack direction="row" spacing={0.5}>
          <Tooltip title="Editar">
            <span>
              <IconButton size="small" color="primary" onClick={() => onEdit?.(params.row)} disabled={loadingRow}>
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
              >
                {loadingRow === params.row.id ? <CircularProgress size={16} color="inherit" /> : <DeleteIcon fontSize="small" />}
              </IconButton>
            </span>
          </Tooltip>
          <Tooltip title={params.row.blocked ? "Desbloquear" : "Bloquear"}>
            <span>
              <IconButton
                size="small"
                color="warning"
                onClick={async () => {
                  if (!params.row.id || !params.row.firebase_uid) {
                    alert("IDs inválidos!");
                    return;
                  }
                  const action = params.row.blocked ? onUnblock : onBlock;
                  const key = params.row.id + (params.row.blocked ? "_unblock" : "_block");
                  setLoadingRow(key);
                  try {
                    await action?.(params.row.id, params.row.firebase_uid);
                  } catch (e) {
                    alert("Erro ao atualizar status: " + e.message);
                  }
                  setLoadingRow(null);
                }}
                disabled={loadingRow === params.row.id + (params.row.blocked ? "_unblock" : "_block")}
              >
                {loadingRow === params.row.id + (params.row.blocked ? "_unblock" : "_block") ? (
                  <CircularProgress size={16} color="inherit" />
                ) : params.row.blocked ? (
                  <LockOpenIcon fontSize="small" />
                ) : (
                  <LockIcon fontSize="small" />
                )}
              </IconButton>
            </span>
          </Tooltip>
        </Stack>
      ),
    },
  ];

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
      <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center", mb: 2 }}>
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
          background: theme.palette.background.default,
          borderRadius: 2,
          border: "none",
          "& .MuiDataGrid-row:hover": {
            bgcolor: theme.palette.action.hover,
            boxShadow: "0 2px 12px -6px rgba(0,0,0,0.1)",
          },
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
          "& .MuiDataGrid-footerContainer": {
            borderTop: `1px solid ${theme.palette.divider}`,
            bgcolor: "background.paper",
          },
        }}
      />
    </Box>
  );
}
