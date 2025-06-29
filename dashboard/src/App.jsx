import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { ThemeProvider as MuiThemeProvider, CssBaseline } from "@mui/material";
import { ThemeProviderCustom, useThemeCustom } from "./context/ThemeContext";
import { UserProvider, useUser } from "./context/UserContext";
import LoginForm from "./components/LoginForm";
import Dashboard from "./components/Dashboard";

function AppContent() {
  const { theme } = useThemeCustom();
  const { user, setUser } = useUser();

  return (
    <MuiThemeProvider theme={theme}>
      <CssBaseline />
      {!user ? <LoginForm onLogin={setUser} /> : <Dashboard />}
    </MuiThemeProvider>
  );
}

export default function App() {
  return (
    <ThemeProviderCustom>
      <UserProvider>
        <BrowserRouter>
          <Routes>
            <Route path="/dashboard/*" element={<AppContent />} />
            <Route path="/" element={<Navigate to="/dashboard/recipes" replace />} />
          </Routes>
        </BrowserRouter>
      </UserProvider>
    </ThemeProviderCustom>
  );
}
