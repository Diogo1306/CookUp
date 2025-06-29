import { DataGrid } from "@mui/x-data-grid";
import { Avatar, Button, Stack } from "@mui/material";

export default function IngredientsTable({ ingredients, onEdit, onDelete }) {
  const rows = ingredients.map((ing) => ({
    ...ing,
    id: ing.ingredient_id,
  }));

  const columns = [
    {
      field: "ingredient_image_url",
      headerName: "Foto",
      width: 70,
      renderCell: (params) => <Avatar src={params.value} imgProps={{ referrerPolicy: "no-referrer" }} sx={{ width: 40, height: 40 }} />,
      sortable: false,
      filterable: false,
    },
    { field: "ingredient_name", headerName: "Nome", width: 200 },
    {
      field: "actions",
      headerName: "Ações",
      width: 180,
      renderCell: (params) => (
        <Stack direction="row" spacing={1}>
          <Button size="small" onClick={() => onEdit(params.row)}>
            Editar
          </Button>
          <Button size="small" color="error" onClick={() => onDelete(params.row.ingredient_id)}>
            Deletar
          </Button>
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
