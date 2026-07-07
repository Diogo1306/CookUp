import { createContext, useContext, useMemo, useState, useEffect } from "react";
import { getTheme } from "../theme/theme";

const ThemeContext = createContext();

export function ThemeProviderCustom({ children }) {
  const [mode, setMode] = useState(() => {
    const saved = localStorage.getItem("themeMode");
    if (saved) return saved;
    // Primeira visita: segue a preferência do sistema.
    return window.matchMedia?.("(prefers-color-scheme: dark)").matches ? "dark" : "light";
  });

  useEffect(() => {
    localStorage.setItem("themeMode", mode);
    // Aplica o tema às variáveis CSS do dashboard.
    document.documentElement.setAttribute("data-theme", mode);
  }, [mode]);

  const theme = useMemo(() => getTheme(mode), [mode]);
  const toggleTheme = () => setMode((m) => (m === "light" ? "dark" : "light"));

  return <ThemeContext.Provider value={{ mode, toggleTheme, theme }}>{children}</ThemeContext.Provider>;
}

export function useThemeCustom() {
  return useContext(ThemeContext);
}
