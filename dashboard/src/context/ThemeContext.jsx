import { createContext, useContext, useState, useEffect } from "react";

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

  const toggleTheme = () => setMode((m) => (m === "light" ? "dark" : "light"));

  return <ThemeContext.Provider value={{ mode, toggleTheme }}>{children}</ThemeContext.Provider>;
}

export function useThemeCustom() {
  return useContext(ThemeContext);
}
