import { createContext, useContext, useMemo, useState, useEffect } from "react";
import { getTheme } from "../theme/theme";

const ThemeContext = createContext();

export function ThemeProviderCustom({ children }) {
  const [mode, setMode] = useState(() => localStorage.getItem("themeMode") || "light");

  useEffect(() => {
    localStorage.setItem("themeMode", mode);
  }, [mode]);

  const theme = useMemo(() => getTheme(mode), [mode]);
  const toggleTheme = () => setMode((m) => (m === "light" ? "dark" : "light"));

  return <ThemeContext.Provider value={{ mode, toggleTheme, theme }}>{children}</ThemeContext.Provider>;
}

export function useThemeCustom() {
  return useContext(ThemeContext);
}
