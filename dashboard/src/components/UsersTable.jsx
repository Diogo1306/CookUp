import { Table, TableHead, TableBody, TableRow, TableCell, Avatar, Button } from "@mui/material";

export default function UsersTable({ users, onDelete, onEdit }) {
  return (
    <Table>
      <TableHead>
        <TableRow>
          <TableCell>Foto</TableCell>
          <TableCell>Nome</TableCell>
          <TableCell>Email</TableCell>
          <TableCell>ID</TableCell>
          <TableCell>Role</TableCell>
          <TableCell>Ações</TableCell>
        </TableRow>
      </TableHead>
      <TableBody>
        {users.map((user) => (
          <TableRow key={user.user_id}>
            <TableCell>
              <Avatar src={user.profile_picture} imgProps={{ referrerPolicy: "no-referrer" }} />
            </TableCell>
            <TableCell>{user.username}</TableCell>
            <TableCell>{user.email}</TableCell>
            <TableCell>{user.user_id}</TableCell>
            <TableCell>{user.role}</TableCell>
            <TableCell>
              <Button size="small" onClick={() => onEdit?.(user)}>
                Editar
              </Button>
              <Button size="small" color="error" onClick={() => onDelete?.(user.user_id)}>
                Deletar
              </Button>
            </TableCell>
          </TableRow>
        ))}
      </TableBody>
    </Table>
  );
}
