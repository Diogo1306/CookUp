import { Button, Box } from "@mui/material";
import { Logout } from "@mui/icons-material";
import { useUser } from "../context/userContext";

export default function SettingsPage() {
  const { setUser } = useUser();
  function handleLogout() {
    setUser(null);
  }
  return (
    <Box>
      <Button variant="contained" color="error" startIcon={<Logout />} onClick={handleLogout}>
        Sair da Conta
      </Button>
    </Box>
  );
}
