import { useState, useEffect } from "react";
import { Dialog, DialogTitle, DialogContent, TextField, Button } from "@mui/material";

export default function IngredientsForm({ open, onClose, onSave, initialData, loading }) {
  const [ingredient_name, setIngredientName] = useState("");
  const [image_file, setImageFile] = useState(null);

  useEffect(() => {
    setIngredientName(initialData?.ingredient_name || "");
    setImageFile(null);
  }, [initialData, open]);

  function handleSubmit(e) {
    e.preventDefault();
    onSave({
      ingredient_id: initialData?.ingredient_id,
      ingredient_name,
      oldImage: initialData?.image_url, // envia imagem antiga (string) se existir
      newImage: image_file, // envia novo arquivo se editar
      image_file, // para criar
    });
  }

  return (
    <Dialog open={open} onClose={onClose}>
      <DialogTitle>{initialData ? "Editar Ingrediente" : "Novo Ingrediente"}</DialogTitle>
      <DialogContent>
        <form onSubmit={handleSubmit}>
          <TextField
            fullWidth
            label="Nome do Ingrediente"
            value={ingredient_name}
            onChange={(e) => setIngredientName(e.target.value)}
            margin="dense"
            required
          />
          <Button component="label" sx={{ mt: 2 }}>
            {image_file ? image_file.name : "Selecionar Imagem"}
            <input type="file" hidden accept="image/*" onChange={(e) => setImageFile(e.target.files[0])} />
          </Button>
          <Button type="submit" fullWidth variant="contained" color="primary" sx={{ mt: 2 }} disabled={loading}>
            {initialData ? "Salvar" : "Criar"}
          </Button>
        </form>
      </DialogContent>
    </Dialog>
  );
}
