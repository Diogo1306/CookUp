import { useEffect, useMemo, useState } from "react";
import { useCategories } from "../hooks/useCategories";
import Modal from "../components/Modal";
import { IconSearch, IconEdit, IconTrash, IconPlus } from "../components/icons";

const FALLBACK = "/brand/ic_cookup.png";
const EMPTY = { category_name: "", color_hex: "#f96163", image_file: null };

export default function CategoriesPage() {
  const { categories, loading, error, handleCreate, handleEdit, handleDelete, loadCategories } = useCategories();
  const [query, setQuery] = useState("");
  const [open, setOpen] = useState(false);
  const [editing, setEditing] = useState(null);
  const [form, setForm] = useState(EMPTY);
  const [saving, setSaving] = useState(false);

  useEffect(() => { loadCategories(); }, [loadCategories]);

  const filtered = useMemo(
    () => categories.filter((c) => c.category_name?.toLowerCase().includes(query.toLowerCase())),
    [categories, query]
  );

  function openNew() {
    setEditing(null);
    setForm(EMPTY);
    setOpen(true);
  }
  function openEdit(cat) {
    setEditing(cat);
    setForm({ category_name: cat.category_name, color_hex: cat.color_hex || "#f96163", image_file: null });
    setOpen(true);
  }
  async function save() {
    if (!form.category_name.trim()) return;
    setSaving(true);
    if (editing) {
      await handleEdit({ ...editing, ...form });
    } else {
      await handleCreate(form);
    }
    setSaving(false);
    setOpen(false);
    loadCategories();
  }
  function remove(cat) {
    if (window.confirm(`Eliminar a categoria "${cat.category_name}"?`)) handleDelete(cat.category_id);
  }

  return (
    <div>
      <div className="page-head">
        <h2>Categorias</h2>
        <span className="count-badge">{categories.length}</span>
      </div>

      <div className="actions">
        <div className="field search">
          <IconSearch />
          <input placeholder="Pesquisar categorias…" value={query} onChange={(e) => setQuery(e.target.value)} />
        </div>
        <button className="btn-new" onClick={openNew}><IconPlus /> Nova Categoria</button>
      </div>

      {loading && !categories.length ? (
        <div className="page-loading">A carregar…</div>
      ) : (
        <div className="tbl">
          <div className="tbl-scroll">
            <table>
              <thead>
                <tr><th>CATEGORIA</th><th>COR</th><th style={{ textAlign: "right" }}>AÇÕES</th></tr>
              </thead>
              <tbody>
                {filtered.map((c) => (
                  <tr key={c.category_id}>
                    <td>
                      <div className="cell-media">
                        <img src={c.category_image_url || FALLBACK} alt="" style={{ borderRadius: 8 }} onError={(e) => (e.currentTarget.src = FALLBACK)} />
                        <div className="t">{c.category_name}</div>
                      </div>
                    </td>
                    <td>
                      <span className="chip" style={{ background: (c.color_hex || "#eee") + "22", color: c.color_hex || "#666" }}>
                        {c.color_hex || "—"}
                      </span>
                    </td>
                    <td>
                      <div className="row-actions" style={{ justifyContent: "flex-end" }}>
                        <button className="iact" onClick={() => openEdit(c)} title="Editar"><IconEdit /></button>
                        <button className="iact del" onClick={() => remove(c)} title="Eliminar"><IconTrash /></button>
                      </div>
                    </td>
                  </tr>
                ))}
                {filtered.length === 0 && (
                  <tr><td colSpan={3} className="page-empty">Nenhuma categoria encontrada.</td></tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {error && <div className="page-error">{error}</div>}

      {open && (
        <Modal
          title={editing ? "Editar categoria" : "Nova categoria"}
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
            <input className="input" value={form.category_name} onChange={(e) => setForm({ ...form, category_name: e.target.value })} placeholder="Ex.: Sobremesas" />
          </div>
          <div className="field-group">
            <label>Cor</label>
            <input className="input" type="color" value={form.color_hex} onChange={(e) => setForm({ ...form, color_hex: e.target.value })} style={{ padding: 4, height: 42 }} />
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
