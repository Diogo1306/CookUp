import React, { useState, useEffect } from "react";
import { Dialog, DialogTitle, DialogContent, TextField, Button, DialogActions, Box, Avatar } from "@mui/material";

export default function CategoryForm({ open, onClose, onSave, initialData, loading }) {
  const [name, setName] = useState("");
  const [color, setColor] = useState("");
  const [imageFile, setImageFile] = useState(null);
  const [preview, setPreview] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    setName(initialData?.category_name || "");
    setColor(initialData?.color_hex || "");
    setImageFile(null);
    setPreview(initialData?.category_image_url || "");
    setError("");
  }, [initialData, open]);

  function handleImageChange(e) {
    const file = e.target.files[0];
    setImageFile(file);
    if (file) setPreview(URL.createObjectURL(file));
  }

  function handleSubmit(e) {
    e.preventDefault();
    if (!name) return setError("Nome obrigatório!");
    onSave({
      category_id: initialData?.category_id,
      category_name: name,
      color_hex: color,
      image_file: imageFile,
      image_url: initialData?.image_url,
    });
  }

  return (
    <Dialog open={open} onClose={onClose}>
      <form onSubmit={handleSubmit}>
        <DialogTitle>{initialData ? "Editar Categoria" : "Nova Categoria"}</DialogTitle>
        <DialogContent>
          <Box sx={{ display: "flex", flexDirection: "column", gap: 2, mt: 1 }}>
            <Avatar src={preview} sx={{ width: 56, height: 56, alignSelf: "center" }} />
            <Button variant="contained" component="label">
              Escolher imagem
              <input type="file" accept="image/*" hidden onChange={handleImageChange} />
            </Button>
            <TextField label="Nome" value={name} onChange={(e) => setName(e.target.value)} required />
            <TextField label="Cor (hex)" value={color} onChange={(e) => setColor(e.target.value)} />
            {error && <Box sx={{ color: "red", mt: 1 }}>{error}</Box>}
          </Box>
        </DialogContent>
        <DialogActions>
          <Button onClick={onClose}>Cancelar</Button>
          <Button type="submit" disabled={loading}>
            {loading ? "Salvando..." : "Salvar"}
          </Button>
        </DialogActions>
      </form>
    </Dialog>
  );
}
