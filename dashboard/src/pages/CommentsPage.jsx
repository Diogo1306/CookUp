import { useEffect, useMemo, useState } from "react";
import { useComments } from "../hooks/useComments";
import { IconSearch, IconTrash } from "../components/icons";

const FALLBACK = "/brand/ic_cookup.png";

// Formata a data para dd/mm/aaaa (devolve "—" se inválida)
function formatDate(value) {
  if (!value) return "—";
  const d = new Date(value);
  if (Number.isNaN(d.getTime())) return "—";
  const dd = String(d.getDate()).padStart(2, "0");
  const mm = String(d.getMonth() + 1).padStart(2, "0");
  return `${dd}/${mm}/${d.getFullYear()}`;
}

export default function CommentsPage() {
  const { comments, loading, error, loadComments, handleDelete } = useComments();
  const [query, setQuery] = useState("");

  useEffect(() => { loadComments(); }, [loadComments]);

  // Filtro client-side por texto do comentário ou username
  const filtered = useMemo(() => {
    const q = query.toLowerCase();
    return comments.filter(
      (c) =>
        c.comment?.toLowerCase().includes(q) ||
        c.username?.toLowerCase().includes(q)
    );
  }, [comments, query]);

  // Elimina após confirmação
  function remove(c) {
    if (window.confirm(`Eliminar o comentário #${c.comment_id}?`)) handleDelete(c.comment_id);
  }

  return (
    <div>
      <div className="page-head">
        <h2>Comentários</h2>
        <span className="count-badge">{comments.length}</span>
      </div>

      <div className="actions">
        <div className="field search">
          <IconSearch />
          <input
            placeholder="Pesquisar comentários…"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
          />
        </div>
      </div>

      {loading && !comments.length ? (
        <div className="page-loading">A carregar…</div>
      ) : (
        <div className="tbl">
          <div className="tbl-scroll">
            <table>
              <thead>
                <tr>
                  <th>UTILIZADOR</th>
                  <th>COMENTÁRIO</th>
                  <th>RECEITA</th>
                  <th>DATA</th>
                  <th style={{ textAlign: "right" }}>AÇÕES</th>
                </tr>
              </thead>
              <tbody>
                {filtered.map((c) => (
                  <tr key={c.comment_id}>
                    <td>
                      <div className="user-cell">
                        <img src={FALLBACK} alt="" onError={(e) => (e.currentTarget.src = FALLBACK)} />
                        <div className="nm">{c.username || "—"}</div>
                      </div>
                    </td>
                    <td>{c.comment}</td>
                    <td>
                      <span style={{ color: "var(--primary-dark)", fontWeight: 600 }}>
                        receita #{c.recipe_id}
                      </span>
                    </td>
                    <td>{formatDate(c.created_at)}</td>
                    <td>
                      <div className="row-actions" style={{ justifyContent: "flex-end" }}>
                        <button className="iact del" onClick={() => remove(c)} title="Eliminar"><IconTrash /></button>
                      </div>
                    </td>
                  </tr>
                ))}
                {filtered.length === 0 && (
                  <tr><td colSpan={5} className="page-empty">Nenhum comentário encontrado.</td></tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {error && <div className="page-error">{error}</div>}
    </div>
  );
}
