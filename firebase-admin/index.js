// Carrega variáveis de ambiente de um .env, se o pacote dotenv existir.
try { require("dotenv").config(); } catch (_) { /* dotenv é opcional */ }

const express = require("express");
const cors = require("cors");
const admin = require("firebase-admin");
const app = express();

// CORS: por omissão só aceita origens em localhost (qualquer porta).
// Em produção define ADMIN_CORS_ORIGIN com a origem exata do dashboard.
const allowedOrigin = process.env.ADMIN_CORS_ORIGIN;
app.use(cors({ origin: allowedOrigin || /^http:\/\/(localhost|127\.0\.0\.1)(:\d+)?$/ }));
app.use(express.json());

const serviceAccount = require("./serviceAccountKey.json");
admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
});

// UIDs de administradores autorizados (env ADMIN_UIDS, separados por vírgula).
const ADMIN_UIDS = (process.env.ADMIN_UIDS || "")
  .split(",")
  .map((u) => u.trim())
  .filter(Boolean);

if (ADMIN_UIDS.length === 0) {
  console.warn(
    "[AVISO] ADMIN_UIDS não definido: qualquer utilizador Firebase autenticado " +
    "consegue usar a API de administração. Define ADMIN_UIDS com o(s) UID(s) de admin."
  );
}

// Middleware: exige um token Firebase válido e, se ADMIN_UIDS estiver definido,
// que o utilizador seja administrador. Bloqueia acessos anónimos.
async function requireAdmin(req, res, next) {
  try {
    const header = req.headers.authorization || "";
    const token = header.startsWith("Bearer ") ? header.slice(7) : null;
    if (!token) {
      return res.status(401).json({ success: false, error: "Autenticação em falta." });
    }
    const decoded = await admin.auth().verifyIdToken(token);
    if (ADMIN_UIDS.length > 0 && !ADMIN_UIDS.includes(decoded.uid)) {
      return res.status(403).json({ success: false, error: "Sem permissões de administrador." });
    }
    req.adminUid = decoded.uid;
    next();
  } catch (err) {
    return res.status(401).json({ success: false, error: "Token inválido." });
  }
}

app.delete("/user/:uid", requireAdmin, async (req, res) => {
  try {
    await admin.auth().deleteUser(req.params.uid);
    res.json({ success: true });
  } catch (err) {
    res.status(500).json({ success: false, error: err.message });
  }
});

app.post("/user/:uid/email", requireAdmin, async (req, res) => {
  try {
    const { email } = req.body;
    const userId = req.params.uid;

    if (!email || !userId) {
      return res.status(400).json({ success: false, error: "Email ou UID ausente!" });
    }

    await admin.auth().updateUser(userId, { email });
    res.json({ success: true });
  } catch (err) {
    res.status(500).json({ success: false, error: err.message });
  }
});

app.post("/user/:uid/password", requireAdmin, async (req, res) => {
  try {
    const { password } = req.body;
    const userId = req.params.uid;

    if (!password || password.length < 6) {
      return res.status(400).json({ success: false, error: "A senha deve ter pelo menos 6 caracteres" });
    }

    await admin.auth().updateUser(userId, { password });
    res.json({ success: true });
  } catch (err) {
    res.status(500).json({ success: false, error: err.message });
  }
});

app.post("/user/:uid/block", requireAdmin, async (req, res) => {
  try {
    await admin.auth().updateUser(req.params.uid, { disabled: true });
    res.json({ success: true });
  } catch (err) {
    res.status(500).json({ success: false, error: err.message });
  }
});

app.post("/user/:uid/unblock", requireAdmin, async (req, res) => {
  try {
    await admin.auth().updateUser(req.params.uid, { disabled: false });
    res.json({ success: true });
  } catch (err) {
    res.status(500).json({ success: false, error: err.message });
  }
});

const PORT = process.env.ADMIN_PORT || 4000;
app.listen(PORT, () => {
  console.log(`Admin API a correr em http://localhost:${PORT}`);
});
