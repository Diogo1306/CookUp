import { useEffect, useMemo, useState } from "react";
import { useIngredients } from "../hooks/useIngredients";
import Modal from "../components/Modal";
import { IconSearch, IconEdit, IconTrash, IconPlus } from "../components/icons";

const FALLBACK = "/brand/ic_cookup.png";
const EMPTY = { ingredient_name: "", image_file: null };

export default function IngredientsPage() {
  const { ingredients, loading, error, handleCreate, handleEdit, handleDelete, loadIngredients } = useIngredients();
  const [query, setQuery] = useState("");
  const [open, setOpen] = useState(false);
  const [editing, setEditing] = useState(null);
  const [form, setForm] = useState(EMPTY);
  const [saving, setSaving] = useState(false);

  useEffect(() => { loadIngredients(); }, [loadIngredients]);

  const filtered = useMemo(
    () => ingredients.filter((i) => i.ingredient_name?.toLowerCase().includes(query.toLowerCase())),
    [ingredients, query]
  );

  // Campo de imagem: a API usa ingredient_image_url (tabela) ou image_url (form).
  const imageOf = (ing) => ing.ingredient_image_url || ing.image_url || null;

  function openNew() {
    setEditing(null);
    setForm(EMPTY);
    setOpen(true);
  }
  function openEdit(ing) {
    setEditing(ing);
    setForm({ ingredient_name: ing.ingredient_name, image_file: null });
    setOpen(true);
  }
  async function save() {
    if (!form.ingredient_name.trim()) return;
    setSaving(true);
    if (editing) {
      await handleEdit({
        ingredient_id: editing.ingredient_id,
        ingredient_name: form.ingredient_name,
        oldImage: imageOf(editing), // mantém imagem se não houver nova
        newImage: form.image_file,
      });
    } else {
      await handleCreate({ ingredient_name: form.ingredient_name, image_file: form.image_file });
    }
    setSaving(false);
    setOpen(false);
    loadIngredients();
  }
  function remove(ing) {
    if (window.confirm(`Eliminar o ingrediente "${ing.ingredient_name}"?`)) handleDelete(ing.ingredient_id);
  }

  return (
    <div>
      <div className="page-head">
        <h2>Ingredientes</h2>
        <span className="count-badge">{ingredients.length}</span>
      </div>

      <div className="actions">
        <div className="field search">
          <IconSearch />
          <input placeholder="Pesquisar ingredientes…" value={query} onChange={(e) => setQuery(e.target.value)} />
        </div>
        <button className="btn-new" onClick={openNew}><IconPlus /> Novo Ingrediente</button>
      </div>

      {loading && !ingredients.length ? (
        <div className="page-loading">A carregar…</div>
      ) : (
        <div className="tbl">
          <div className="tbl-scroll">
            <table>
              <thead>
                <tr><th>INGREDIENTE</th><th style={{ textAlign: "right" }}>AÇÕES</th></tr>
              </thead>
              <tbody>
                {filtered.map((i) => (
                  <tr key={i.ingredient_id}>
                    <td>
                      <div className="cell-media">
                        <img src={imageOf(i) || FALLBACK} alt="" onError={(e) => (e.currentTarget.src = FALLBACK)} />
                        <div className="t">{i.ingredient_name}</div>
                      </div>
                    </td>
                    <td>
                      <div className="row-actions" style={{ justifyContent: "flex-end" }}>
                        <button className="iact" onClick={() => openEdit(i)} title="Editar"><IconEdit /></button>
                        <button className="iact del" onClick={() => remove(i)} title="Eliminar"><IconTrash /></button>
                      </div>
                    </td>
                  </tr>
                ))}
                {filtered.length === 0 && (
                  <tr><td colSpan={2} className="page-empty">Nenhum ingrediente encontrado.</td></tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {error && <div className="page-error">{error}</div>}

      {open && (
        <Modal
          title={editing ? "Editar ingrediente" : "Novo ingrediente"}
          onClose={() => setOpen(false)}
          footer={
            <>
              <button className="btn ghost" onClick={() => setOpen(false)}>Cancelar</button>
              <button className="btn primary" onClick={save} disabled={saving}>{saving ? "A guardar…" : "Guardar"}</button>
            </>
          }
        >
          <div className="field-group">
            <label>Nome</label>
            <input className="input" value={form.ingredient_name} onChange={(e) => setForm({ ...form, ingredient_name: e.target.value })} placeholder="Ex.: Tomate" />
          </div>
          <div className="field-group">
            <label>Imagem {editing && "(deixa vazio para manter)"}</label>
            <input className="input" type="file" accept="image/*" onChange={(e) => setForm({ ...form, image_file: e.target.files[0] || null })} style={{ paddingTop: 9 }} />
          </div>
        </Modal>
      )}
    </div>
  );
}
