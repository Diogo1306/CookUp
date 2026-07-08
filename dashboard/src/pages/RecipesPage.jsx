import { useEffect, useMemo, useState } from "react";
import { useRecipes } from "../hooks/useRecipes";
import { getAllCategories } from "../api/categories";
import { getRecipeById } from "../api/recipe";
import Modal from "../components/Modal";
import { IconSearch, IconPlus, IconEdit, IconTrash, IconEye, IconHeart, IconStar } from "../components/icons";
import "./Recipes.css";

const FALLBACK = "/brand/ic_cookup.png";
const DIFFS = ["Fácil", "Médio", "Difícil"];

// Estado inicial do formulário (mesma forma que o RecipeForm original espera).
const EMPTY = {
  recipe_id: "",
  author_id: "",
  title: "",
  description: "",
  instructions: "",
  difficulty: "",
  preparation_time: "",
  servings: "",
  categories: [], // array de category_id
  ingredients: [], // [{ ingredient_name, ingredient_quantity }]
  gallery: [], // File ou URL (string)
};

// Dificuldade -> classe do chip (easy/med/hard). Copiado do OverviewPage.
function stripAccents(s) {
  return (s || "").normalize("NFD").replace(/[̀-ͯ]/g, "").toLowerCase();
}
function diffClass(d) {
  const x = stripAccents(d);
  if (x.startsWith("fac")) return "easy";
  if (x.startsWith("med")) return "med";
  if (x.startsWith("dif")) return "hard";
  return "";
}
// Data -> dd/mm/aaaa
function fmt(dateStr) {
  if (!dateStr) return "—";
  const d = new Date(dateStr);
  if (isNaN(d)) return "—";
  return d.toLocaleDateString("pt-PT", { day: "2-digit", month: "2-digit", year: "numeric" });
}

export default function RecipesPage() {
  const { recipes, loading, error, handleDelete, handleSave } = useRecipes();

  const [allCategories, setAllCategories] = useState([]);

  // Filtros client-side
  const [query, setQuery] = useState("");
  const [catFilter, setCatFilter] = useState("");
  const [diffFilter, setDiffFilter] = useState("");

  // Modal de criar/editar
  const [open, setOpen] = useState(false);
  const [editing, setEditing] = useState(false);
  const [form, setForm] = useState(EMPTY);
  const [saving, setSaving] = useState(false);
  const [loadingEdit, setLoadingEdit] = useState(false);

  useEffect(() => {
    getAllCategories()
      .then(setAllCategories)
      .catch(() => setAllCategories([]));
  }, []);

  // Categorias distintas presentes nas receitas (para o filtro).
  const catOptions = useMemo(() => {
    const set = new Set();
    recipes.forEach((r) => (r.categories || []).forEach((c) => c.category_name && set.add(c.category_name)));
    return [...set].sort((a, b) => a.localeCompare(b, "pt"));
  }, [recipes]);

  // Aplica pesquisa (título) + filtro de categoria + filtro de dificuldade.
  const filtered = useMemo(() => {
    const q = query.trim().toLowerCase();
    return recipes.filter((r) => {
      const okQ = !q || (r.title || "").toLowerCase().includes(q);
      const okCat = !catFilter || (r.categories || []).some((c) => c.category_name === catFilter);
      const okDiff = !diffFilter || r.difficulty === diffFilter;
      return okQ && okCat && okDiff;
    });
  }, [recipes, query, catFilter, diffFilter]);

  // ---- Abrir / fechar modal ----
  function openNew() {
    setEditing(false);
    setForm(EMPTY);
    setOpen(true);
  }

  async function openEdit(recipe) {
    // Busca a receita completa (inclui ingredientes/galeria), tal como a página original.
    setLoadingEdit(true);
    try {
      const dataCompleta = await getRecipeById(recipe.recipe_id);
      const d = dataCompleta?.data;
      if (d) {
        setForm({
          recipe_id: d.recipe_id || "",
          author_id: d.author_id || "",
          title: d.title || "",
          description: d.description || "",
          instructions: d.instructions || "",
          difficulty: d.difficulty || "",
          preparation_time: d.preparation_time || "",
          servings: d.servings || "",
          categories: (d.categories || []).map((c) => c.category_id),
          ingredients: d.ingredients
            ? d.ingredients.map((ing) => ({
                ingredient_name: ing.ingredient_name || "",
                ingredient_quantity: ing.ingredient_quantity || "",
              }))
            : [],
          gallery: d.gallery || [],
        });
      } else {
        setForm(EMPTY);
      }
      setEditing(true);
      setOpen(true);
    } finally {
      setLoadingEdit(false);
    }
  }

  // ---- Guardar (mesmo formato/chamada que a página original) ----
  async function save(e) {
    e?.preventDefault();
    if (!form.title.trim()) return;
    if (form.gallery.length > 6) {
      alert("Máximo 6 imagens permitidas.");
      return;
    }
    setSaving(true);
    const data = { ...form, categories: form.categories.map(Number) };
    await handleSave(data, editing);
    setSaving(false);
    setOpen(false);
  }

  function remove(recipe) {
    if (window.confirm(`Eliminar a receita "${recipe.title}"?`)) handleDelete(recipe.recipe_id);
  }

  // ---- Ingredientes ----
  function setIngredient(i, field, value) {
    setForm((f) => ({
      ...f,
      ingredients: f.ingredients.map((ing, idx) => (idx === i ? { ...ing, [field]: value } : ing)),
    }));
  }
  function addIngredient() {
    setForm((f) => ({ ...f, ingredients: [...f.ingredients, { ingredient_name: "", ingredient_quantity: "" }] }));
  }
  function removeIngredient(i) {
    setForm((f) => ({ ...f, ingredients: f.ingredients.filter((_, idx) => idx !== i) }));
  }

  // ---- Galeria ----
  function addImage(file) {
    if (!file) return;
    setForm((f) => (f.gallery.length >= 6 ? f : { ...f, gallery: [...f.gallery, file] }));
  }
  function removeImage(i) {
    setForm((f) => ({ ...f, gallery: f.gallery.filter((_, idx) => idx !== i) }));
  }

  // ---- Categorias (toggle) ----
  function toggleCategory(cat) {
    setForm((f) => {
      const sel = f.categories.includes(cat.category_id);
      return {
        ...f,
        categories: sel ? f.categories.filter((id) => id !== cat.category_id) : [...f.categories, cat.category_id],
      };
    });
  }

  return (
    <div>
      <div className="page-head">
        <h2>Receitas</h2>
        <span className="count-badge">{recipes.length}</span>
      </div>

      {/* Barra de ações: pesquisa + filtros + nova receita */}
      <div className="actions">
        <div className="field search">
          <IconSearch />
          <input placeholder="Pesquisar receitas…" value={query} onChange={(e) => setQuery(e.target.value)} />
        </div>
        <select className="select" value={catFilter} onChange={(e) => setCatFilter(e.target.value)}>
          <option value="">Todas as categorias</option>
          {catOptions.map((name) => (
            <option key={name} value={name}>{name}</option>
          ))}
        </select>
        <select className="select" value={diffFilter} onChange={(e) => setDiffFilter(e.target.value)}>
          <option value="">Todas as dificuldades</option>
          {DIFFS.map((d) => (
            <option key={d} value={d}>{d}</option>
          ))}
        </select>
        <button className="btn-new" onClick={openNew}>
          <IconPlus /> Nova Receita
        </button>
      </div>

      {loading && !recipes.length ? (
        <div className="page-loading">A carregar…</div>
      ) : (
        <div className="tbl">
          <div className="tbl-scroll">
            <table>
              <thead>
                <tr>
                  <th>RECEITA</th>
                  <th>CATEGORIAS</th>
                  <th>DIFICULDADE</th>
                  <th>MÉTRICAS</th>
                  <th>ATUALIZADA</th>
                  <th style={{ textAlign: "right" }}>AÇÕES</th>
                </tr>
              </thead>
              <tbody>
                {filtered.map((r) => (
                  <tr key={r.recipe_id}>
                    <td>
                      <div className="cell-media">
                        <img src={r.image || FALLBACK} alt="" onError={(e) => (e.currentTarget.src = FALLBACK)} />
                        <div>
                          <div className="t">{r.title}</div>
                          <div className="s">#{r.recipe_id}</div>
                        </div>
                      </div>
                    </td>
                    <td>
                      <div className="chips">
                        {(r.categories || []).map((c) => (
                          <span
                            key={c.category_id}
                            className="chip"
                            style={{ background: (c.category_color || "#eee") + "22", color: c.category_color || "#666" }}
                          >
                            {c.category_name}
                          </span>
                        ))}
                      </div>
                    </td>
                    <td>
                      {r.difficulty ? <span className={"chip sm " + diffClass(r.difficulty)}>{r.difficulty}</span> : "—"}
                    </td>
                    <td>
                      <div className="metrics">
                        <span><IconEye /> {r.views_count ?? 0}</span>
                        <span><IconHeart /> {r.favorites_count ?? 0}</span>
                        <span><IconStar /> {r.average_rating ? Number(r.average_rating).toFixed(1) : "—"}</span>
                      </div>
                    </td>
                    <td>{fmt(r.updated_at)}</td>
                    <td>
                      <div className="row-actions" style={{ justifyContent: "flex-end" }}>
                        <button className="iact" onClick={() => openEdit(r)} title="Editar"><IconEdit /></button>
                        <button className="iact del" onClick={() => remove(r)} title="Eliminar"><IconTrash /></button>
                      </div>
                    </td>
                  </tr>
                ))}
                {filtered.length === 0 && (
                  <tr><td colSpan={6} className="page-empty">Nenhuma receita encontrada.</td></tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {error && <div className="page-error">{error}</div>}
      {loadingEdit && <div className="page-loading">A carregar receita…</div>}

      {open && (
        <Modal
          title={editing ? "Editar Receita" : "Nova Receita"}
          onClose={() => setOpen(false)}
          footer={
            <>
              <button type="button" className="btn ghost" onClick={() => setOpen(false)}>Cancelar</button>
              <button type="submit" form="recipe-form" className="btn primary" disabled={saving}>
                {saving ? "A guardar…" : "Guardar"}
              </button>
            </>
          }
        >
          <form id="recipe-form" onSubmit={save}>
            {/* Galeria de imagens */}
            <div className="field-group">
              <label>Galeria de imagens (até 6)</label>
              <div className="rf-gallery">
                {form.gallery.map((img, i) => (
                  <div className="rf-thumb" key={i}>
                    <img
                      src={img instanceof File ? URL.createObjectURL(img) : img}
                      alt=""
                      onError={(e) => (e.currentTarget.src = FALLBACK)}
                    />
                    <button type="button" className="rf-thumb-del" onClick={() => removeImage(i)} title="Remover">×</button>
                  </div>
                ))}
                {form.gallery.length < 6 && (
                  <label className="rf-add" title="Adicionar imagem">
                    <IconPlus />
                    <input
                      hidden
                      type="file"
                      accept="image/*"
                      onChange={(e) => {
                        addImage(e.target.files[0]);
                        e.target.value = "";
                      }}
                    />
                  </label>
                )}
              </div>
            </div>

            {/* Título */}
            <div className="field-group">
              <label>Título</label>
              <input
                className="input"
                value={form.title}
                required
                placeholder="Ex.: Bolo de chocolate"
                onChange={(e) => setForm((f) => ({ ...f, title: e.target.value }))}
              />
            </div>

            {/* Dificuldade + tempo + doses */}
            <div className="rf-row">
              <div className="field-group">
                <label>Dificuldade</label>
                <select
                  value={form.difficulty}
                  required
                  onChange={(e) => setForm((f) => ({ ...f, difficulty: e.target.value }))}
                >
                  <option value="" disabled>Selecionar…</option>
                  {DIFFS.map((d) => (
                    <option key={d} value={d}>{d}</option>
                  ))}
                </select>
              </div>
              <div className="field-group">
                <label>Tempo (min)</label>
                <input
                  className="input"
                  type="number"
                  min="0"
                  value={form.preparation_time}
                  required
                  onChange={(e) => setForm((f) => ({ ...f, preparation_time: e.target.value }))}
                />
              </div>
              <div className="field-group">
                <label>Doses</label>
                <input
                  className="input"
                  type="number"
                  min="0"
                  value={form.servings}
                  required
                  onChange={(e) => setForm((f) => ({ ...f, servings: e.target.value }))}
                />
              </div>
            </div>

            {/* Descrição */}
            <div className="field-group">
              <label>Descrição</label>
              <textarea
                className="textarea"
                rows={3}
                value={form.description}
                required
                onChange={(e) => setForm((f) => ({ ...f, description: e.target.value }))}
              />
            </div>

            {/* Instruções */}
            <div className="field-group">
              <label>Instruções</label>
              <textarea
                className="textarea"
                rows={5}
                value={form.instructions}
                required
                onChange={(e) => setForm((f) => ({ ...f, instructions: e.target.value }))}
              />
            </div>

            {/* Ingredientes */}
            <div className="field-group">
              <label>Ingredientes</label>
              {form.ingredients.map((ing, i) => (
                <div className="rf-ing" key={i}>
                  <input
                    className="input"
                    placeholder="Ingrediente"
                    value={ing.ingredient_name}
                    required
                    onChange={(e) => setIngredient(i, "ingredient_name", e.target.value)}
                  />
                  <input
                    className="input"
                    placeholder="Quantidade"
                    value={ing.ingredient_quantity}
                    required
                    onChange={(e) => setIngredient(i, "ingredient_quantity", e.target.value)}
                  />
                  <button type="button" className="iact del" onClick={() => removeIngredient(i)} title="Remover">
                    <IconTrash />
                  </button>
                </div>
              ))}
              <button type="button" className="btn ghost rf-add-ing" onClick={addIngredient}>
                + Adicionar ingrediente
              </button>
            </div>

            {/* Categorias (toggle) */}
            <div className="field-group">
              <label>Categorias</label>
              <div className="chips">
                {allCategories.map((cat) => {
                  const sel = form.categories.includes(cat.category_id);
                  const color = cat.color_hex || cat.category_color || "#888";
                  return (
                    <button
                      type="button"
                      key={cat.category_id}
                      className="chip rf-cat"
                      onClick={() => toggleCategory(cat)}
                      style={sel ? { background: color + "22", color, boxShadow: `inset 0 0 0 1.5px ${color}` } : undefined}
                    >
                      {cat.category_name}
                    </button>
                  );
                })}
                {allCategories.length === 0 && <span className="rf-muted">Sem categorias disponíveis.</span>}
              </div>
            </div>
          </form>
        </Modal>
      )}
    </div>
  );
}
