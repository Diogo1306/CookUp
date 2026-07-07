import { createContext, useContext, useState, useEffect } from "react";

const ThemeContext = createContext();

export function ThemeProviderCustom({ children }) {
  // Predefinição: modo claro (como no design). Guarda a escolha do utilizador.
  const [mode, setMode] = useState(() => localStorage.getItem("themeMode") || "light");

  useEffect(() => {
    localStorage.setItem("themeMode", mode);
    // Aplica o tema às variáveis CSS do dashboard.
    document.documentElement.setAttribute("data-theme", mode);
  }, [mode]);

  const toggleTheme = () => setMode((m) => (m === "light" ? "dark" : "light"));

  return <ThemeContext.Provider value={{ mode, toggleTheme }}>{children}</ThemeContext.Provider>;
}

export function useThemeCustom() {
  return useContext(ThemeContext);
}
