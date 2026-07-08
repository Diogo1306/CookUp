import { useNavigate, useLocation, Routes, Route, Navigate } from "react-router-dom";
import { useUser } from "../context/UserContext";
import { useThemeCustom } from "../context/ThemeContext";
import {
  IconOverview, IconRecipes, IconUsers, IconCategories,
  IconIngredients, IconComments, IconSettings, IconLogout, IconMoon, IconSun,
} from "./icons";
import OverviewPage from "../pages/OverviewPage";
import RecipesPage from "../pages/RecipesPage";
import UsersPage from "../pages/UsersPage";
import CategoriesPage from "../pages/CategoriesPage";
import IngredientsPage from "../pages/IngredientsPage";
import CommentsPage from "../pages/CommentsPage";
import SettingsPage from "../pages/SettingsPage";
import "./Dashboard.css";

const NAV = [
  { page: "overview", label: "Visão Geral", Icon: IconOverview },
  { page: "recipes", label: "Receitas", Icon: IconRecipes },
  { page: "users", label: "Utilizadores", Icon: IconUsers },
  { page: "categories", label: "Categorias", Icon: IconCategories },
  { page: "ingredients", label: "Ingredientes", Icon: IconIngredients },
  { page: "comments", label: "Comentários", Icon: IconComments },
];

const TITLES = {
  overview: "Visão Geral", recipes: "Receitas", users: "Utilizadores",
  categories: "Categorias", ingredients: "Ingredientes", comments: "Comentários",
  settings: "Definições",
};

function roleLabel(role) {
  if (role === "admin") return "Administrador";
  if (role === "moderador") return "Moderador";
  return "Utilizador";
}

const FALLBACK_AVATAR = "/brand/ic_cookup.png";

export default function Dashboard() {
  const { user, setUser } = useUser();
  const { mode, toggleTheme } = useThemeCustom() || {};
  const navigate = useNavigate();
  const location = useLocation();

  const current = location.pathname.split("/")[2] || "overview";
  const go = (page) => navigate(`/dashboard/${page}`);
  const avatar = user?.profile_picture || FALLBACK_AVATAR;

  return (
    <div className="dash">
      <aside className="sidebar">
        <div className="sb-logo">
          <img src="/brand/logo256.png" alt="" />
          <b>CookUp</b>
          <span className="sb-badge">ADMIN</span>
        </div>

        <nav className="sb-menu">
          {NAV.map(({ page, label, Icon }) => (
            <button
              key={page}
              className={"sb-item" + (current === page ? " active" : "")}
              onClick={() => go(page)}
            >
              <Icon /> {label}
            </button>
          ))}
        </nav>

        <div className="sb-spacer" />

        <button
          className={"sb-item sb-foot-item" + (current === "settings" ? " active" : "")}
          onClick={() => go("settings")}
        >
          <IconSettings /> Definições
        </button>

        <div className="sb-user">
          <img src={avatar} alt="" onError={(e) => (e.currentTarget.src = FALLBACK_AVATAR)} />
          <div className="sb-user-info">
            <div className="nm">{user?.username || "Admin"}</div>
            <div className="rl">{roleLabel(user?.role)}</div>
          </div>
          <button className="sb-logout" onClick={() => setUser(null)} title="Terminar sessão">
            <IconLogout />
          </button>
        </div>
      </aside>

      <div className="main">
        <header className="topbar">
          <h1>{TITLES[current] || "CookUp"}</h1>
          <div className="tb-spacer" />
          <button className="tb-icon" onClick={toggleTheme} title="Alternar tema">
            {mode === "dark" ? <IconSun /> : <IconMoon />}
          </button>
          <img className="tb-avatar" src={avatar} alt="" onError={(e) => (e.currentTarget.src = FALLBACK_AVATAR)} />
        </header>

        <main className="content">
          <div className="page-view" key={current}>
          <Routes>
            <Route path="overview" element={<OverviewPage />} />
            <Route path="recipes" element={<RecipesPage />} />
            <Route path="users" element={<UsersPage />} />
            <Route path="categories" element={<CategoriesPage />} />
            <Route path="ingredients" element={<IngredientsPage />} />
            <Route path="comments" element={<CommentsPage />} />
            <Route path="settings" element={<SettingsPage />} />
            <Route path="*" element={<Navigate to="/dashboard/overview" replace />} />
          </Routes>
          </div>
        </main>
      </div>
    </div>
  );
}
