import { DataGrid } from "@mui/x-data-grid";
import { Avatar, Button, Stack, Chip } from "@mui/material";
import { blockUser, unblockUser, deleteUser } from "../api/user";

export default function UsersTable({ users, onEdit, reloadUsers }) {
  const rows = users.map((user) => ({
    ...user,
    id: user.user_id,
    blocked: user.blocked === "1" || user.blocked === 1,
  }));

  const columns = [
    {
      field: "profile_picture",
      headerName: "Foto",
      width: 70,
      renderCell: (params) => <Avatar src={params.value} imgProps={{ referrerPolicy: "no-referrer" }} sx={{ width: 32, height: 32 }} />,
      sortable: false,
      filterable: false,
    },
    { field: "username", headerName: "Nome", width: 150 },
    { field: "email", headerName: "Email", width: 180 },
    { field: "user_id", headerName: "ID", width: 80 },
    { field: "role", headerName: "Role", width: 90 },
    {
      field: "blocked",
      headerName: "Status",
      width: 120,
      renderCell: (params) =>
        params.value ? <Chip label="Bloqueado" color="error" size="small" /> : <Chip label="Ativo" color="success" size="small" />,
      sortable: false,
      filterable: false,
    },
    {
      field: "actions",
      headerName: "Ações",
      width: 320,
      renderCell: (params) => (
        <Stack direction="row" spacing={1}>
          <Button size="small" onClick={() => onEdit?.(params.row)}>
            Editar
          </Button>
          <Button
            size="small"
            color="error"
            onClick={async () => {
              if (window.confirm("Tem certeza que deseja deletar no Firebase Auth e MySQL?")) {
                try {
                  await deleteUser(params.row.id, params.row.firebase_uid);
                  reloadUsers && reloadUsers();
                } catch (e) {
                  alert("Erro ao deletar: " + e.message);
                }
              }
            }}
          >
            Deletar
          </Button>
          {params.row.blocked ? (
            <Button
              size="small"
              color="warning"
              onClick={async () => {
                try {
                  await unblockUser(params.row.id, params.row.firebase_uid);
                  reloadUsers && reloadUsers();
                } catch (e) {
                  alert("Erro ao desbloquear: " + e.message);
                }
              }}
            >
              Desbloquear
            </Button>
          ) : (
            <Button
              size="small"
              color="warning"
              onClick={async () => {
                try {
                  await blockUser(params.row.id, params.row.firebase_uid);
                  reloadUsers && reloadUsers();
                } catch (e) {
                  alert("Erro ao bloquear: " + e.message);
                }
              }}
            >
              Bloquear
            </Button>
          )}
        </Stack>
      ),
      sortable: false,
      filterable: false,
    },
  ];

  return (
    <div style={{ height: 440, width: "100%" }}>
      <DataGrid rows={rows} columns={columns} pageSize={8} rowsPerPageOptions={[8, 20, 100]} disableSelectionOnClick autoHeight />
    </div>
  );
}
