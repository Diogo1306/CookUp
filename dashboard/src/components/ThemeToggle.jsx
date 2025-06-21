import { IconButton, Tooltip } from "@mui/material";
import { Brightness7, Brightness4 } from "@mui/icons-material";
import { useThemeCustom } from "../context/ThemeContext";

export default function ThemeToggle() {
  const { mode, toggleTheme } = useThemeCustom();

  return (
    <Tooltip title="Alternar tema">
      <IconButton onClick={toggleTheme} color="inherit" sx={{ ml: 1 }}>
        {mode === "dark" ? <Brightness7 /> : <Brightness4 />}
      </IconButton>
    </Tooltip>
  );
}
