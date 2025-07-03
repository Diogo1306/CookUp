import React, { useState } from "react";
import { useNavigate, useLocation, Routes, Route, Navigate } from "react-router-dom";
import { useUser } from "../context/UserContext";
import logo from "../assets/default.png";
import {
  Box,
  Drawer,
  List,
  ListItem,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Avatar,
  Typography,
  Chip,
  Divider,
  AppBar,
  Toolbar,
  IconButton,
  useTheme,
  useMediaQuery,
} from "@mui/material";
import { Group, RestaurantMenu, Category, LocalGroceryStore, Settings, Comment, Menu as MenuIcon } from "@mui/icons-material";
import ThemeToggle from "./ThemeToggle";
import UsersPage from "../pages/UsersPage";
import RecipesPage from "../pages/RecipesPage";
import CategoriesPage from "../pages/CategoriesPage";
import IngredientsPage from "../pages/IngredientsPage";
import SettingsPage from "../pages/SettingsPage";
import CommentsPage from "../pages/CommentsPage";

const MENU = [
  {
    grupo: "Gestão",
    items: [
      { label: "Utilizadores", icon: <Group />, page: "users" },
      { label: "Receitas", icon: <RestaurantMenu />, page: "recipes" },
      { label: "Categorias", icon: <Category />, page: "categories" },
      { label: "Ingredientes", icon: <LocalGroceryStore />, page: "ingredients" },
      { label: "Comentários", icon: <Comment />, page: "comments" },
    ],
  },
  {
    grupo: "Configurações",
    items: [{ label: "Definições", icon: <Settings />, page: "settings" }],
  },
];

const drawerWidth = 260;

export default function Dashboard() {
  const { user } = useUser();
  const navigate = useNavigate();
  const location = useLocation();
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down("md"));
  const [drawerOpen, setDrawerOpen] = useState(!isMobile);

  const path = location.pathname.split("/")[2] || "recipes";

  function handleNav(page) {
    navigate(`/dashboard/${page}`);
    if (isMobile) setDrawerOpen(false);
  }

  function getRoleColor(role) {
    if (role === "admin") return "success";
    if (role === "moderador") return "info";
    return "default";
  }

  return (
    <Box sx={{ display: "flex", minHeight: "100vh", bgcolor: "background.default" }}>
      <AppBar
        position="fixed"
        color="default"
        elevation={0}
        sx={{
          zIndex: (t) => t.zIndex.drawer + 1,
          bgcolor: "background.paper",
        }}
      >
        <Toolbar sx={{ justifyContent: "space-between", px: 2, minHeight: 72 }}>
          <Box sx={{ display: "flex", alignItems: "center", gap: 2 }}>
            {isMobile && (
              <IconButton edge="start" onClick={() => setDrawerOpen(true)}>
                <MenuIcon />
              </IconButton>
            )}
            <img src={logo} alt="Logo CookUp" style={{ height: 100, marginLeft: 50 }} />
          </Box>
          <Box sx={{ display: "flex", alignItems: "center", gap: 2 }}>
            <ThemeToggle />
            <Divider orientation="vertical" flexItem sx={{ mx: 2, display: { xs: "none", md: "block" } }} />
            <Box sx={{ display: "flex", alignItems: "center", gap: 1.5 }}>
              <Avatar
                src={user?.profile_picture || "/assets/default-profile.png"}
                alt={user?.username || "Perfil"}
                sx={{ width: 44, height: 44, border: "2px solid", borderColor: "primary.main" }}
              />
              <Box sx={{ display: "flex", alignItems: "center", gap: 1 }}>
                <Typography
                  variant="subtitle1"
                  fontWeight={700}
                  sx={{
                    color: "text.primary",
                    whiteSpace: "nowrap",
                    overflow: "hidden",
                    textOverflow: "ellipsis",
                    maxWidth: { xs: 80, sm: 150, md: 200 },
                  }}
                >
                  {user?.username}
                </Typography>
              </Box>
            </Box>
          </Box>
        </Toolbar>
      </AppBar>

      <Drawer
        variant={isMobile ? "temporary" : "permanent"}
        open={drawerOpen}
        onClose={() => setDrawerOpen(false)}
        sx={{
          width: drawerWidth,
          flexShrink: 0,
          [`& .MuiDrawer-paper`]: {
            width: drawerWidth,
            bgcolor: "background.paper",
            color: "text.primary",
            boxSizing: "border-box",
            borderRight: "1px solid",
            borderColor: "divider",
          },
        }}
        ModalProps={{
          keepMounted: true,
        }}
      >
        <Box sx={{ display: "flex", flexDirection: "column", height: "100%" }}>
          <Box sx={{ py: 3, textAlign: "center" }}>
            <Typography variant="h6" fontWeight={700} sx={{ color: "primary.main", letterSpacing: 1 }}>
              Menu
            </Typography>
          </Box>
          <Divider />
          <Box sx={{ flexGrow: 1, mt: 1 }}>
            {MENU.map((section, idx) => (
              <React.Fragment key={section.grupo}>
                <Box sx={{ mt: idx === 0 ? 2 : 4 }}>
                  <Typography
                    variant="caption"
                    sx={{
                      pl: 3,
                      pb: 1,
                      color: "text.secondary",
                      fontWeight: 700,
                      textTransform: "uppercase",
                      letterSpacing: 1,
                      fontSize: "0.8rem",
                    }}
                  >
                    {section.grupo}
                  </Typography>
                  <List>
                    {section.items.map((item) => {
                      const selected = path === item.page;
                      return (
                        <ListItem disablePadding key={item.page} sx={{ mb: 0.5 }}>
                          <ListItemButton
                            selected={selected}
                            onClick={() => handleNav(item.page)}
                            sx={{
                              borderRadius: 2,
                              mx: 1.5,
                              py: 1.3,
                              px: 2,
                              mb: 0.5,
                              transition: "background 0.15s, color 0.15s",
                              bgcolor: selected ? "primary.100" : undefined,
                              color: selected ? "primary.main" : undefined,
                              fontWeight: selected ? 800 : 600,
                              boxShadow: selected ? 2 : undefined,
                              "&:hover": {
                                bgcolor: "primary.50",
                                color: "primary.main",
                              },
                            }}
                          >
                            <ListItemIcon
                              sx={{
                                color: selected ? "primary.main" : "inherit",
                                minWidth: 38,
                                mr: 1,
                                fontSize: "1.7rem",
                                transition: "color 0.15s",
                              }}
                            >
                              {item.icon}
                            </ListItemIcon>
                            <ListItemText
                              primary={
                                <Typography variant="body1" fontWeight={selected ? 700 : 600}>
                                  {item.label}
                                </Typography>
                              }
                            />
                          </ListItemButton>
                        </ListItem>
                      );
                    })}
                  </List>
                </Box>
                {idx === 0 && <Divider sx={{ my: 2 }} />}
              </React.Fragment>
            ))}
          </Box>
        </Box>
      </Drawer>

      {/* CONTEÚDO */}
      <Box
        component="main"
        sx={{
          flex: 1,
          bgcolor: "background.default",
          p: { xs: 2, md: 4 },
          mt: { xs: 7, md: 8 },
          minHeight: "100vh",
          transition: "padding 0.2s",
        }}
      >
        <Routes>
          <Route path="users" element={<UsersPage />} />
          <Route path="recipes" element={<RecipesPage />} />
          <Route path="categories" element={<CategoriesPage />} />
          <Route path="ingredients" element={<IngredientsPage />} />
          <Route path="comments" element={<CommentsPage />} />
          <Route path="settings" element={<SettingsPage />} />
          <Route path="*" element={<Navigate to="/dashboard/recipes" replace />} />
        </Routes>
      </Box>
    </Box>
  );
}
