import { DataGrid } from "@mui/x-data-grid";
import { Avatar, Button, Stack } from "@mui/material";

export default function CategoriesTable({ categories, onEdit, onDelete }) {
  const rows = categories.map((cat) => ({ ...cat, id: cat.category_id }));

  const columns = [
    {
      field: "category_image_url",
      headerName: "Imagem",
      width: 70,
      renderCell: (params) => <Avatar src={params.value} sx={{ width: 32, height: 32 }} />,
    },
    { field: "category_name", headerName: "Nome", width: 180 },
    { field: "color_hex", headerName: "Cor", width: 100 },
    {
      field: "actions",
      headerName: "Ações",
      width: 200,
      renderCell: (params) => (
        <Stack direction="row" spacing={1}>
          <Button size="small" onClick={() => onEdit(params.row)}>
            Editar
          </Button>
          <Button size="small" color="error" onClick={() => onDelete(params.row.category_id)}>
            Deletar
          </Button>
        </Stack>
      ),
    },
  ];

  return (
    <div style={{ height: 440, width: "100%" }}>
      <DataGrid rows={rows} columns={columns} pageSize={8} rowsPerPageOptions={[8, 20]} disableSelectionOnClick autoHeight />
    </div>
  );
}
