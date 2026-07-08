import { useUser } from "../context/UserContext";
import { useThemeCustom } from "../context/ThemeContext";
import { IconMoon, IconLock, IconLogout } from "../components/icons";
import "./Settings.css";

const FALLBACK_AVATAR = "/brand/ic_cookup.png";

export default function SettingsPage() {
  const { user, setUser } = useUser();
  const { mode, toggleTheme } = useThemeCustom() || {};
  const isDark = mode === "dark";

  // Termina a sessão do administrador (funcionalidade real já existente)
  function handleLogout() {
    setUser(null);
  }

  return (
    <div>
      <div className="page-head">
        <h2>Definições</h2>
      </div>

      <div className="settings">
        {/* Cartão 1 — Perfil do administrador (só de leitura) */}
        <section className="set-card">
          <div className="set-card-head">
            <div className="set-title">Perfil do administrador</div>
            <div className="set-sub">Dados da conta com que iniciaste sessão.</div>
          </div>

          <div className="set-body set-profile">
            <img
              className="set-avatar"
              src={user?.profile_picture || FALLBACK_AVATAR}
              alt=""
              onError={(e) => (e.currentTarget.src = FALLBACK_AVATAR)}
            />
            <div className="set-fields">
              <div className="field-group">
                <label>Nome</label>
                <input className="input" value={user?.username || "—"} readOnly />
              </div>
              <div className="field-group">
                <label>Email</label>
                <input className="input" value={user?.email || "—"} readOnly />
              </div>
            </div>
          </div>
        </section>

        {/* Cartão 2 — Aparência (interruptor de tema) */}
        <section className="set-card">
          <div className="set-card-head">
            <div className="set-title">Aparência</div>
            <div className="set-sub">Personaliza o aspeto do painel.</div>
          </div>

          <div className="set-body">
            <div className="set-row">
              <div className="set-row-text">
                <span className="set-row-label"><IconMoon /> Modo escuro</span>
                <span className="set-row-sub">Alterna entre o tema claro e escuro.</span>
              </div>
              {/* Interruptor ligado ao tema global */}
              <label className="switch">
                <input type="checkbox" checked={isDark} onChange={toggleTheme} />
                <span className="track"><span className="thumb" /></span>
              </label>
            </div>
          </div>
        </section>

        {/* Cartão 3 — Segurança */}
        <section className="set-card">
          <div className="set-card-head">
            <div className="set-title">Segurança</div>
            <div className="set-sub">Gere o acesso à tua conta.</div>
          </div>

          <div className="set-body set-stack">
            <div className="set-row">
              <div className="set-row-text">
                <span className="set-row-label"><IconLock /> Alterar palavra-passe</span>
                <span className="set-row-sub">Define uma nova palavra-passe de acesso.</span>
              </div>
              {/* Placeholder — sem lógica associada por agora */}
              <button className="btn ghost" type="button">Alterar</button>
            </div>

            <div className="set-row">
              <div className="set-row-text">
                <span className="set-row-label"><IconLogout /> Terminar sessão</span>
                <span className="set-row-sub">Sai da conta neste dispositivo.</span>
              </div>
              <button className="btn ghost set-danger" type="button" onClick={handleLogout}>Sair</button>
            </div>
          </div>
        </section>
      </div>
    </div>
  );
}
