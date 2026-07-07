import { useState } from "react";
import { useLogin } from "../hooks/useLogin";
import { useUser } from "../context/UserContext";
import "./LoginForm.css";

function MailIcon() {
  return (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round">
      <rect x="2" y="4" width="20" height="16" rx="2" />
      <path d="m22 7-10 6L2 7" />
    </svg>
  );
}

function LockIcon() {
  return (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round">
      <rect x="3" y="11" width="18" height="11" rx="2" />
      <path d="M7 11V7a5 5 0 0 1 10 0v4" />
    </svg>
  );
}

function EyeIcon({ off }) {
  return off ? (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round">
      <path d="M9.9 4.24A9.1 9.1 0 0 1 12 4c7 0 10 8 10 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24" />
      <path d="M1 1l22 22M6.61 6.61A18.5 18.5 0 0 0 2 12s3 8 10 8a9.1 9.1 0 0 0 5.39-1.61" />
    </svg>
  ) : (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round">
      <path d="M2 12s3-8 10-8 10 8 10 8-3 8-10 8-10-8-10-8Z" />
      <circle cx="12" cy="12" r="3" />
    </svg>
  );
}

export default function LoginForm({ onLogin }) {
  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [showPass, setShowPass] = useState(false);
  const { setUser } = useUser() || {};
  const { login, loading, error } = useLogin();

  async function handleSubmit(e) {
    e.preventDefault();
    const user = await login(email, senha);
    if (user && setUser) {
      setUser(user);
      onLogin && onLogin(user);
    }
  }

  return (
    <div className="login">
      <aside className="login__brand">
        <div className="login__logo">
          <img src="/brand/logo256.png" alt="CookUp" />
        </div>
        <h1 className="login__brand-name">CookUp</h1>
        <p className="login__brand-sub">DASHBOARD DE ADMINISTRAÇÃO</p>
        <p className="login__brand-tagline">
          Gere receitas, utilizadores, categorias e comentários da comunidade num só sítio.
        </p>
      </aside>

      <main className="login__panel">
        <form className="login__form" onSubmit={handleSubmit}>
          <h2 className="login__title">Bem-vindo de volta</h2>
          <p className="login__subtitle">Inicia sessão com a tua conta de administrador</p>

          <label className="login__label" htmlFor="login-email">E-mail</label>
          <div className="login__field">
            <span className="login__field-icon"><MailIcon /></span>
            <input
              id="login-email"
              type="email"
              placeholder="exemplo@email.com"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              autoComplete="email"
              required
            />
          </div>

          <label className="login__label" htmlFor="login-pass">Palavra-passe</label>
          <div className="login__field">
            <span className="login__field-icon"><LockIcon /></span>
            <input
              id="login-pass"
              type={showPass ? "text" : "password"}
              placeholder="••••••••"
              value={senha}
              onChange={(e) => setSenha(e.target.value)}
              autoComplete="current-password"
              required
            />
            <button
              type="button"
              className="login__eye"
              onClick={() => setShowPass((v) => !v)}
              aria-label={showPass ? "Ocultar palavra-passe" : "Mostrar palavra-passe"}
            >
              <EyeIcon off={showPass} />
            </button>
          </div>

          {error && <div className="login__error">{error}</div>}

          <button className="login__submit" type="submit" disabled={loading}>
            {loading ? "A entrar…" : "Entrar"}
          </button>

          <button type="button" className="login__forgot">Esqueceste-te da palavra-passe?</button>
          <p className="login__foot">Acesso reservado a administradores e moderadores</p>
        </form>
      </main>
    </div>
  );
}
