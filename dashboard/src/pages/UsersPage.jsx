import { useEffect, useMemo, useState } from "react";
import { useUsers } from "../hooks/useUsers";
import Modal from "../components/Modal";
import { IconSearch, IconEdit, IconTrash, IconLock, IconUnlock, IconPlus } from "../components/icons";

// Imagem de recurso quando o utilizador não tem foto (ou falha o carregamento).
const FALLBACK = "/brand/ic_cookup.png";
// Estado inicial do formulário do modal.
const EMPTY = { username: "", email: "", role: "user", password: "", photo: null };

// blocked pode vir como 1/0 (número) ou "1"/"0" (string) da API.
const isBlocked = (u) => u.blocked === 1 || u.blocked === "1";

// Formata a data de registo em MM/AAAA.
function fmtDate(v) {
  if (!v) return "—";
  const d = new Date(v);
  if (isNaN(d.getTime())) return "—";
  return `${String(d.getMonth() + 1).padStart(2, "0")}/${d.getFullYear()}`;
}

// Mapeia a função para o chip (classe + rótulo).
function roleChip(role) {
  if (role === "admin") return { cls: "admin", label: "Admin" };
  if (role === "moderador") return { cls: "mod", label: "Moderador" };
  return { cls: "usr", label: "Utilizador" };
}

export default function UsersPage() {
  const { users, loading, error, loadUsers, handleCreate, handleEdit, handleDelete, handleBlock, handleUnblock } = useUsers();

  // Filtros client-side.
  const [query, setQuery] = useState("");
  const [roleFilter, setRoleFilter] = useState("all");
  const [stateFilter, setStateFilter] = useState("all");

  // Modal / formulário.
  const [open, setOpen] = useState(false);
  const [editing, setEditing] = useState(null);
  const [form, setForm] = useState(EMPTY);
  const [preview, setPreview] = useState(FALLBACK);
  const [formError, setFormError] = useState("");
  const [saving, setSaving] = useState(false);

  useEffect(() => { loadUsers(); }, [loadUsers]);

  // Cartões de resumo.
  const stats = useMemo(() => ({
    total: users.length,
    active: users.filter((u) => !isBlocked(u)).length,
    blocked: users.filter((u) => isBlocked(u)).length,
    admins: users.filter((u) => u.role === "admin").length,
  }), [users]);

  // Lista filtrada (pesquisa + função + estado).
  const filtered = useMemo(() => {
    const q = query.trim().toLowerCase();
    return users.filter((u) => {
      const matchQ = !q || u.username?.toLowerCase().includes(q) || u.email?.toLowerCase().includes(q);
      const matchRole = roleFilter === "all" || u.role === roleFilter;
      const blk = isBlocked(u);
      const matchState = stateFilter === "all" || (stateFilter === "blocked" ? blk : !blk);
      return matchQ && matchRole && matchState;
    });
  }, [users, query, roleFilter, stateFilter]);

  // ---- Modal ----
  function openNew() {
    setEditing(null);
    setForm(EMPTY);
    setPreview(FALLBACK);
    setFormError("");
    setOpen(true);
  }

  function openEdit(u) {
    setEditing(u);
    setForm({ username: u.username || "", email: u.email || "", role: u.role || "user", password: "", photo: null });
    setPreview(u.profile_picture || FALLBACK);
    setFormError("");
    setOpen(true);
  }

  function onPhotoChange(e) {
    const file = e.target.files[0] || null;
    setForm((f) => ({ ...f, photo: file }));
    if (file) setPreview(URL.createObjectURL(file));
  }

  // Guarda: mesma lógica da página anterior (handleEdit em edição, handleCreate em criação).
  async function save() {
    setFormError("");
    if (!form.email || !/^[\w-.]+@([\w-]+\.)+[\w-]{2,4}$/.test(form.email)) {
      setFormError("E-mail inválido.");
      return;
    }
    if (!editing && form.password.length < 6) {
      setFormError("A palavra-passe deve ter no mínimo 6 caracteres.");
      return;
    }

    // Monta o payload tal como o UsersFrom original.
    const payload = { username: form.username, role: form.role, email: form.email };
    if (editing?.user_id) payload.user_id = editing.user_id;
    if (form.password && form.password.length >= 6) payload.password = form.password;
    if (editing && form.photo) payload.profile_picture = form.photo; // só troca foto em edição

    setSaving(true);
    if (editing) {
      await handleEdit({ ...editing, ...payload });
    } else {
      await handleCreate(payload);
    }
    setSaving(false);
    setOpen(false);
    setEditing(null);
  }

  // ---- Ações da linha ----
  async function onBlockToggle(u) {
    if (!u.user_id || !u.firebase_uid) return;
    if (isBlocked(u)) await handleUnblock(u.user_id, u.firebase_uid);
    else await handleBlock(u.user_id, u.firebase_uid);
  }

  function onRemove(u) {
    if (!u.user_id || !u.firebase_uid) return;
    if (window.confirm(`Eliminar o utilizador "${u.username}"? Remove-o do Firebase e da base de dados.`)) {
      handleDelete(u.user_id, u.firebase_uid);
    }
  }

  return (
    <div>
      <div className="page-head">
        <h2>Utilizadores</h2>
        <span className="count-badge">{users.length}</span>
      </div>

      {/* Cartões de resumo */}
      <div className="sum-grid">
        <div className="sum"><div className="l">Total</div><div className="v">{stats.total}</div></div>
        <div className="sum"><div className="l">Ativos</div><div className="v">{stats.active}</div></div>
        <div className="sum"><div className="l">Bloqueados</div><div className="v" style={{ color: "var(--danger)" }}>{stats.blocked}</div></div>
        <div className="sum"><div className="l">Administradores</div><div className="v">{stats.admins}</div></div>
      </div>

      {/* Barra de ações: pesquisa + filtros + novo */}
      <div className="actions">
        <div className="field search">
          <IconSearch />
          <input placeholder="Pesquisar por nome ou e-mail…" value={query} onChange={(e) => setQuery(e.target.value)} />
        </div>
        <select className="select" value={roleFilter} onChange={(e) => setRoleFilter(e.target.value)}>
          <option value="all">Todas</option>
          <option value="admin">Admin</option>
          <option value="moderador">Moderador</option>
          <option value="user">Utilizador</option>
        </select>
        <select className="select" value={stateFilter} onChange={(e) => setStateFilter(e.target.value)}>
          <option value="all">Todos</option>
          <option value="active">Ativo</option>
          <option value="blocked">Bloqueado</option>
        </select>
        <button className="btn-new" onClick={openNew}><IconPlus /> Novo Utilizador</button>
      </div>

      {loading && !users.length ? (
        <div className="page-loading">A carregar…</div>
      ) : (
        <div className="tbl">
          <div className="tbl-scroll">
            <table>
              <thead>
                <tr>
                  <th>UTILIZADOR</th>
                  <th>FUNÇÃO</th>
                  <th>ESTADO</th>
                  <th>REGISTADO</th>
                  <th style={{ textAlign: "right" }}>AÇÕES</th>
                </tr>
              </thead>
              <tbody>
                {filtered.map((u) => {
                  const blk = isBlocked(u);
                  const chip = roleChip(u.role);
                  return (
                    <tr key={u.user_id} style={blk ? { background: "#fdf7f5" } : undefined}>
                      <td>
                        <div className="user-cell">
                          <img
                            src={u.profile_picture || FALLBACK}
                            alt=""
                            referrerPolicy="no-referrer"
                            onError={(e) => (e.currentTarget.src = FALLBACK)}
                            style={blk ? { filter: "grayscale(1)" } : undefined}
                          />
                          <div>
                            <div className="nm">{u.username}</div>
                            <div className="em">{u.email}</div>
                          </div>
                        </div>
                      </td>
                      <td>
                        <span className={`chip sm ${chip.cls}`}>{chip.label}</span>
                      </td>
                      <td>
                        <span className={`state ${blk ? "blk" : "act"}`}>
                          <span className="dot" />{blk ? "Bloqueado" : "Ativo"}
                        </span>
                      </td>
                      <td>{fmtDate(u.created_at)}</td>
                      <td>
                        <div className="row-actions" style={{ justifyContent: "flex-end" }}>
                          <button className="iact" onClick={() => openEdit(u)} title="Editar"><IconEdit /></button>
                          {blk ? (
                            <button className="iact ok" onClick={() => onBlockToggle(u)} title="Desbloquear"><IconUnlock /></button>
                          ) : (
                            <button className="iact" onClick={() => onBlockToggle(u)} title="Bloquear"><IconLock /></button>
                          )}
                          <button className="iact del" onClick={() => onRemove(u)} title="Eliminar"><IconTrash /></button>
                        </div>
                      </td>
                    </tr>
                  );
                })}
                {filtered.length === 0 && (
                  <tr><td colSpan={5} className="page-empty">Nenhum utilizador encontrado.</td></tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {error && <div className="page-error">{error}</div>}

      {open && (
        <Modal
          title={editing ? "Editar utilizador" : "Novo utilizador"}
          onClose={() => setOpen(false)}
          footer={
            <>
              <button className="btn ghost" onClick={() => setOpen(false)}>Cancelar</button>
              <button className="btn primary" onClick={save} disabled={saving}>{saving ? "A guardar…" : "Guardar"}</button>
            </>
          }
        >
          {/* Pré-visualização da foto */}
          <div className="field-group" style={{ alignItems: "center" }}>
            <img
              src={preview}
              alt=""
              referrerPolicy="no-referrer"
              onError={(e) => (e.currentTarget.src = FALLBACK)}
              style={{ width: 64, height: 64, borderRadius: "50%", objectFit: "cover", border: "2px solid var(--border)" }}
            />
          </div>

          <div className="field-group">
            <label>Nome</label>
            <input className="input" value={form.username} onChange={(e) => setForm({ ...form, username: e.target.value })} placeholder="Nome do utilizador" />
          </div>

          <div className="field-group">
            <label>E-mail</label>
            <input className="input" type="email" autoComplete="off" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} placeholder="email@exemplo.com" />
          </div>

          <div className="field-group">
            <label>Função</label>
            <select className="input" value={form.role} onChange={(e) => setForm({ ...form, role: e.target.value })}>
              <option value="user">Utilizador</option>
              <option value="moderador">Moderador</option>
              <option value="admin">Admin</option>
            </select>
          </div>

          <div className="field-group">
            <label>{editing ? "Nova palavra-passe" : "Palavra-passe"}</label>
            <input
              className="input"
              type="password"
              value={form.password}
              onChange={(e) => setForm({ ...form, password: e.target.value })}
              placeholder={editing ? "Deixa em branco para não alterar" : "Mínimo 6 caracteres"}
            />
          </div>

          {/* Só permite trocar foto em edição (igual à página anterior) */}
          {editing && (
            <div className="field-group">
              <label>Foto (deixa vazio para manter)</label>
              <input className="input" type="file" accept="image/*" onChange={onPhotoChange} style={{ paddingTop: 9 }} />
            </div>
          )}

          {formError && <div className="page-error">{formError}</div>}
        </Modal>
      )}
    </div>
  );
}
