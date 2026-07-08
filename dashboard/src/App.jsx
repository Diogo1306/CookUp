import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { ThemeProviderCustom } from "./context/ThemeContext";
import { UserProvider, useUser } from "./context/UserContext";
import LoginForm from "./components/LoginForm";
import Dashboard from "./components/Dashboard";

function AppContent() {
  const { user, setUser } = useUser();
  return !user ? <LoginForm onLogin={setUser} /> : <Dashboard />;
}

export default function App() {
  return (
    <ThemeProviderCustom>
      <UserProvider>
        <BrowserRouter>
          <Routes>
            <Route path="/dashboard/*" element={<AppContent />} />
            <Route path="/" element={<Navigate to="/dashboard/overview" replace />} />
          </Routes>
        </BrowserRouter>
      </UserProvider>
    </ThemeProviderCustom>
  );
}
