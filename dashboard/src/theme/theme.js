import { createTheme } from "@mui/material/styles";
import { lightColors, darkColors } from "./colors";

function buildTheme(mode) {
  const colors = mode === "light" ? lightColors : darkColors;

  return createTheme({
    palette: {
      mode,
      primary: { main: colors.primary, light: colors.primary_light, dark: colors.primary_dark },
      secondary: { main: colors.accent_orange },
      background: {
        default: colors.background,
        paper: colors.background_card,
        secondary: colors.background_secondary,
        soft: colors.background_soft,
        elevated: colors.background_elevated,
        overlay: colors.background_overlay,
      },
      text: {
        primary: colors.text_primary,
        secondary: colors.text_secondary,
      },
      error: { main: colors.red },
      warning: { main: colors.accent_orange },
      success: { main: colors.green },
      custom: {
        handle: colors.handle,
        overlay_background: colors.overlay_background,
        white: colors.white,
        black: colors.black,
        gray: colors.gray,
        transparent: colors.transparent,
        semi_transparent: colors.semi_transparent,
      },
    },
  });
}

export const getTheme = buildTheme;
