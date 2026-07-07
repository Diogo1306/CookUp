import { useEffect, useMemo } from "react";
import { useRecipes } from "../hooks/useRecipes";
import { useUsers } from "../hooks/useUsers";
import { useComments } from "../hooks/useComments";
import { IconRecipes, IconUsers, IconEye, IconStar } from "../components/icons";
import "./Overview.css";

const FALLBACK = "/brand/ic_cookup.png";

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
function relTime(dateStr) {
  if (!dateStr) return "";
  const then = new Date(dateStr.replace(" ", "T")).getTime();
  const days = Math.floor((Date.now() - then) / 86400000);
  if (days <= 0) return "hoje";
  if (days === 1) return "há 1 dia";
  if (days < 30) return `há ${days} dias`;
  const months = Math.floor(days / 30);
  return months === 1 ? "há 1 mês" : `há ${months} meses`;
}
function fmtViews(n) {
  n = Number(n) || 0;
  return n >= 1000 ? (n / 1000).toFixed(1).replace(".", ",") + "k" : String(n);
}

export default function OverviewPage() {
  const { recipes, loading } = useRecipes();
  const { users, loadUsers } = useUsers();
  const { comments, loadComments } = useComments();

  useEffect(() => {
    loadUsers();
    loadComments();
  }, [loadUsers, loadComments]);

  const totalViews = useMemo(
    () => recipes.reduce((s, r) => s + (Number(r.views_count) || 0), 0),
    [recipes]
  );
  const avgRating = useMemo(() => {
    const rated = recipes.filter((r) => Number(r.ratings_count) > 0);
    if (!rated.length) return 0;
    return rated.reduce((s, r) => s + Number(r.average_rating || 0), 0) / rated.length;
  }, [recipes]);

  const weeks = useMemo(() => {
    const buckets = Array.from({ length: 8 }, () => 0);
    const now = Date.now();
    recipes.forEach((r) => {
      if (!r.created_at) return;
      const t = new Date(r.created_at.replace(" ", "T")).getTime();
      const w = Math.floor((now - t) / (7 * 86400000));
      if (w >= 0 && w < 8) buckets[7 - w]++;
    });
    return buckets;
  }, [recipes]);
  const maxWeek = Math.max(1, ...weeks);

  const topCats = useMemo(() => {
    const map = {};
    recipes.forEach((r) =>
      (r.categories || []).forEach((c) => {
        if (!c.category_name) return;
        if (!map[c.category_name]) map[c.category_name] = { name: c.category_name, count: 0, img: c.image_url };
        map[c.category_name].count++;
      })
    );
    const arr = Object.values(map).sort((a, b) => b.count - a.count).slice(0, 5);
    const max = Math.max(1, ...arr.map((a) => a.count));
    return arr.map((a) => ({ ...a, pct: Math.round((a.count / max) * 100) }));
  }, [recipes]);

  const usersById = useMemo(
    () => Object.fromEntries(users.map((u) => [String(u.user_id), u])),
    [users]
  );
  const recipesById = useMemo(
    () => Object.fromEntries(recipes.map((r) => [String(r.recipe_id), r.title])),
    [recipes]
  );

  const recentRecipes = useMemo(
    () =>
      [...recipes]
        .sort((a, b) => new Date(b.updated_at || b.created_at) - new Date(a.updated_at || a.created_at))
        .slice(0, 4),
    [recipes]
  );
  const recentComments = useMemo(
    () => [...comments].sort((a, b) => new Date(b.created_at) - new Date(a.created_at)).slice(0, 3),
    [comments]
  );

  if (loading && !recipes.length) return <div className="page-loading">A carregar…</div>;

  return (
    <div className="ov">
      <div className="kpi-grid">
        <div className="kpi">
          <div className="kpi-top">
            <div className="kpi-ico" style={{ background: "#fee7e7", color: "#f96163" }}><IconRecipes size={20} /></div>
            <div><div className="kpi-lbl">Receitas</div><div className="kpi-val">{recipes.length}</div></div>
          </div>
        </div>
        <div className="kpi">
          <div className="kpi-top">
            <div className="kpi-ico" style={{ background: "#fff4e5", color: "#e08a00" }}><IconUsers size={20} /></div>
            <div><div className="kpi-lbl">Utilizadores</div><div className="kpi-val">{users.length}</div></div>
          </div>
        </div>
        <div className="kpi">
          <div className="kpi-top">
            <div className="kpi-ico" style={{ background: "#e8f1ea", color: "#2e7d32" }}><IconEye size={20} /></div>
            <div><div className="kpi-lbl">Visualizações</div><div className="kpi-val">{fmtViews(totalViews)}</div></div>
          </div>
        </div>
        <div className="kpi">
          <div className="kpi-top">
            <div className="kpi-ico" style={{ background: "#fff4e5", color: "#ffa94d" }}><IconStar size={20} /></div>
            <div><div className="kpi-lbl">Avaliação média</div><div className="kpi-val">{avgRating ? avgRating.toFixed(1).replace(".", ",") : "—"}</div></div>
          </div>
        </div>
      </div>

      <div className="two-col">
        <div className="card">
          <div className="card-h"><span className="t">Novas receitas</span><span className="s">últimas 8 semanas</span></div>
          <div className="chart">
            {weeks.map((v, i) => (
              <div className="bar-wrap" key={i}>
                <div className="bar" style={{ height: `${Math.max(3, (v / maxWeek) * 100)}%`, opacity: 0.35 + (i / 7) * 0.65 }} />
                <span className="bar-x">S{i + 1}</span>
              </div>
            ))}
          </div>
        </div>

        <div className="card">
          <div className="card-h"><span className="t">Top categorias</span></div>
          {topCats.length === 0 && <div className="ov-empty">Sem dados</div>}
          {topCats.map((c) => (
            <div className="tc-row" key={c.name}>
              <img src={c.img || FALLBACK} alt="" onError={(e) => (e.currentTarget.src = FALLBACK)} />
              <span className="tc-name">{c.name}</span>
              <div className="tc-track"><div className="tc-fill" style={{ width: `${c.pct}%` }} /></div>
              <span className="tc-val">{c.count}</span>
            </div>
          ))}
        </div>
      </div>

      <div className="two-col">
        <div className="card">
          <div className="card-h"><span className="t">Receitas recentes</span></div>
          {recentRecipes.length === 0 && <div className="ov-empty">Sem receitas</div>}
          {recentRecipes.map((r) => (
            <div className="li" key={r.recipe_id}>
              <img className="th" src={r.image || FALLBACK} alt="" onError={(e) => (e.currentTarget.src = FALLBACK)} />
              <div className="g">
                <div className="t">{r.title}</div>
                <div className="a">por {usersById[String(r.author_id)]?.username || "—"}</div>
              </div>
              {r.difficulty && <span className={"chip sm " + diffClass(r.difficulty)}>{r.difficulty}</span>}
              <span className="time">{relTime(r.updated_at || r.created_at)}</span>
            </div>
          ))}
        </div>

        <div className="card">
          <div className="card-h"><span className="t">Últimos comentários</span></div>
          {recentComments.length === 0 && <div className="ov-empty">Sem comentários</div>}
          {recentComments.map((c) => (
            <div className="li" key={c.comment_id}>
              <img className="av" src={usersById[String(c.user_id)]?.profile_picture || FALLBACK} alt="" onError={(e) => (e.currentTarget.src = FALLBACK)} />
              <div className="g">
                <div className="t">{c.username || usersById[String(c.user_id)]?.username || "Utilizador"}</div>
                <div className="cm">{c.comment}</div>
                <div className="in">em {recipesById[String(c.recipe_id)] || `receita #${c.recipe_id}`}</div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
