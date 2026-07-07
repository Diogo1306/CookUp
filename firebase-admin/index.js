const express = require("express");
const cors = require("cors");
const admin = require("firebase-admin");
const app = express();

app.use(cors());
app.use(express.json());

const serviceAccount = require("./serviceAccountKey.json");
admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
});

app.delete("/user/:uid", async (req, res) => {
  try {
    await admin.auth().deleteUser(req.params.uid);
    res.json({ success: true });
  } catch (err) {
    res.status(500).json({ success: false, error: err.message });
  }
});

app.post("/user/:uid/email", async (req, res) => {
  try {
    const { email } = req.body;
    const userId = req.params.uid;

    console.log("Atualizando email para o UID:", userId);

    if (!email || !userId) {
      return res.status(400).json({ success: false, error: "Email ou UID ausente!" });
    }

    const user = await admin.auth().updateUser(userId, { email });
    console.log("Email atualizado:", user);
    res.json({ success: true });
  } catch (err) {
    console.error("Erro ao atualizar email:", err.message);
    res.status(500).json({ success: false, error: err.message });
  }
});

app.post("/user/:uid/password", async (req, res) => {
  try {
    const { password } = req.body;
    const userId = req.params.uid;

    if (password.length < 6) {
      return res.status(400).json({ success: false, error: "A senha deve ter pelo menos 6 caracteres" });
    }

    console.log("Atualizando senha para o UID:", userId);

    const user = await admin.auth().updateUser(userId, { password });
    console.log("Senha atualizada:", user);
    res.json({ success: true });
  } catch (err) {
    console.error("Erro ao atualizar senha:", err.message);
    res.status(500).json({ success: false, error: err.message });
  }
});

app.post("/user/:uid/block", async (req, res) => {
  try {
    await admin.auth().updateUser(req.params.uid, { disabled: true });
    res.json({ success: true });
  } catch (err) {
    res.status(500).json({ success: false, error: err.message });
  }
});

app.post("/user/:uid/unblock", async (req, res) => {
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
