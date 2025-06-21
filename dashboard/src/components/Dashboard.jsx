import { useState } from "react";
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
} from "@mui/material";
import { Group, RestaurantMenu, Category, LocalGroceryStore, Settings } from "@mui/icons-material";
import { useUser } from "../context/userContext";
import ThemeToggle from "./ThemeToggle";
import UsersPage from "../pages/UsersPage";
import RecipesPage from "../pages/RecipesPage";
import CategoriesPage from "../pages/CategoriesPage";
import IngredientsPage from "../pages/IngredientsPage";
import SettingsPage from "../pages/SettingsPage";

const MENU = [
  {
    group: "Gestão",
    items: [
      { label: "Usuários", icon: <Group />, page: "users" },
      { label: "Receitas", icon: <RestaurantMenu />, page: "recipes" },
      { label: "Categorias", icon: <Category />, page: "categories" },
      { label: "Ingredientes", icon: <LocalGroceryStore />, page: "ingredients" },
    ],
  },
  { group: "Configurações", items: [{ label: "Configurações", icon: <Settings />, page: "settings" }] },
];

export default function Dashboard() {
  const { user } = useUser();
  const [selectedPage, setSelectedPage] = useState("recipes");

  function renderPage() {
    switch (selectedPage) {
      case "users":
        return <UsersPage />;
      case "recipes":
        return <RecipesPage />;
      case "categories":
        return <CategoriesPage />;
      case "ingredients":
        return <IngredientsPage />;
      case "settings":
        return <SettingsPage />;
      default:
        return <Box>Selecione uma opção.</Box>;
    }
  }

  return (
    <Box sx={{ display: "flex", minHeight: "100vh", bgcolor: "background.default" }}>
      <Drawer
        variant="permanent"
        sx={{
          width: 260,
          [`& .MuiDrawer-paper`]: {
            width: 260,
            bgcolor: "background.card",
            color: "text.primary",
            boxSizing: "border-box",
          },
        }}
      >
        <Box sx={{ display: "flex", flexDirection: "column", alignItems: "center", mt: 4, mb: 2 }}>
          <Avatar src={user?.profile_picture || "https://i.pravatar.cc/100"} alt={user?.username || "Perfil"} sx={{ width: 72, height: 72, mb: 1 }} />
          <Typography variant="h6">{user?.username}</Typography>
          <Typography variant="body2" sx={{ color: "text.secondary" }}>
            {user?.email}
          </Typography>
          <Chip label={user?.role?.toUpperCase() || "SEM PAPEL"} color={user?.role === "admin" ? "success" : "default"} size="small" sx={{ mt: 1 }} />
        </Box>
        <Divider />
        {MENU.map((section) => (
          <Box key={section.group}>
            <Typography variant="caption" sx={{ pl: 2, pt: 2, pb: 0.5, color: "text.secondary", fontWeight: 700 }}>
              {section.group}
            </Typography>
            <List>
              {section.items.map((item) => (
                <ListItem disablePadding key={item.page}>
                  <ListItemButton selected={selectedPage === item.page} onClick={() => setSelectedPage(item.page)}>
                    <ListItemIcon sx={{ color: "inherit" }}>{item.icon}</ListItemIcon>
                    <ListItemText primary={item.label} />
                  </ListItemButton>
                </ListItem>
              ))}
            </List>
          </Box>
        ))}
      </Drawer>
      <Box sx={{ flex: 1, minHeight: "100vh", bgcolor: "background.default" }}>
        <AppBar position="static" color="default" elevation={0} sx={{ bgcolor: "background.card", color: "text.primary" }}>
          <Toolbar sx={{ justifyContent: "flex-end" }}>
            <ThemeToggle />
          </Toolbar>
        </AppBar>
        <Box sx={{ p: 3 }}>{renderPage()}</Box>
      </Box>
    </Box>
  );
}
