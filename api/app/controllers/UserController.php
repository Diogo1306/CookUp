<?php

require_once __DIR__ . '/../models/User.php';
require_once __DIR__ . '/../core/Response.php';

class UserController
{

    private $defaultPhoto = 'default.png';

    public function getUser()
    {
        $firebaseUid = $_GET['firebase_uid'] ?? null;

        if (!$firebaseUid) {
            Response::json(['success' => false, 'message' => "UID em falta!"], 400);
            return;
        }

        $user = User::getByUID($firebaseUid);

        Response::json(
            $user
                ? ["success" => true, "data" => $user]
                : ["success" => false, "message" => "Utilizador não encontrado!"],
            $user ? 200 : 404
        );
    }

    public function getAll()
    {
        require_once __DIR__ . '/../models/User.php';
        $users = User::getAllUsers();
        Response::json(['success' => true, 'data' => $users]);
    }


    public function save()
    {
        $data = json_decode(file_get_contents('php://input'), true) ?? $_POST;

        if (!isset($data['firebase_uid'], $data['username'], $data['email'])) {
            Response::json(["success" => false, "message" => "Dados em falta!"], 400);
            return;
        }

        $profile_picture = $data['profile_picture'] ?? $this->defaultPhoto;
        $role = $data['role'] ?? 'user';
        $blocked = isset($data['blocked']) ? (int)$data['blocked'] : 0;

        $result = User::save(
            $data['firebase_uid'],
            $data['username'],
            $data['email'],
            $profile_picture,
            $role,
            $blocked
        );

        if ($result['success'] === false) {
            $status = ($result['message'] === "O nome de utilizador já está em uso." || $result['message'] === "O email já está em uso.") ? 409 : 500;
        } else {
            $status = 200;
        }

        Response::json($result, $status);
    }

    public function updateUserAdmin()
    {
        if (empty($_POST['user_id']) || empty($_POST['username']) || empty($_POST['email']) || empty($_POST['role'])) {
            return Response::json(['success' => false, 'message' => 'Parâmetros obrigatórios faltando.']);
        }

        $userId = $_POST['user_id'];
        $username = $_POST['username'];
        $email = $_POST['email'];
        $role = $_POST['role'];

        $oldPhoto = User::getProfilePhoto($userId);
        $newPhoto = $oldPhoto ?: $this->defaultPhoto;

        if (isset($_FILES['profile_picture']) && $_FILES['profile_picture']['error'] === UPLOAD_ERR_OK) {
            $ext = pathinfo($_FILES['profile_picture']['name'], PATHINFO_EXTENSION);
            $newPhoto = uniqid('profile_', true) . '_' . bin2hex(random_bytes(4)) . '.' . $ext;
            $uploadPath = __DIR__ . '/../../uploads/profile_pictures/' . $newPhoto;

            if (!file_exists(dirname($uploadPath))) {
                mkdir(dirname($uploadPath), 0777, true);
            }

            if (move_uploaded_file($_FILES['profile_picture']['tmp_name'], $uploadPath)) {
                if ($oldPhoto && $oldPhoto !== $this->defaultPhoto && file_exists(__DIR__ . '/../../uploads/profile_pictures/' . $oldPhoto)) {
                    unlink(__DIR__ . '/../../uploads/profile_pictures/' . $oldPhoto);
                }
            } else {
                return Response::json(['success' => false, 'message' => 'Falha ao salvar nova imagem. Caminho: ' . $uploadPath]);
            }
        }

        if (empty($newPhoto)) {
            $newPhoto = $this->defaultPhoto;
        }

        $ok = User::updateAllAdmin($userId, $username, $email, $role, $newPhoto);

        return Response::json($ok ?
            ['success' => true, 'photo' => $newPhoto, 'message' => 'Usuário atualizado com sucesso!']
            : ['success' => false, 'message' => 'Erro ao atualizar usuário.']);
    }

    public function updateProfile()
    {
        if (empty($_POST['user_id']) || empty($_POST['username'])) {
            return Response::json(['success' => false, 'message' => 'Parâmetros obrigatórios faltando.']);
        }

        $userId = $_POST['user_id'];
        $username = $_POST['username'];

        $oldPhoto = User::getProfilePhoto($userId);
        $newPhoto = $oldPhoto ?: $this->defaultPhoto;

        if (isset($_FILES['profile_picture']) && $_FILES['profile_picture']['error'] === UPLOAD_ERR_OK) {
            $ext = pathinfo($_FILES['profile_picture']['name'], PATHINFO_EXTENSION);
            $newPhoto = uniqid('profile_', true) . '_' . bin2hex(random_bytes(4)) . '.' . $ext;
            $uploadPath = __DIR__ . '/../../uploads/profile_pictures/' . $newPhoto;

            if (!file_exists(dirname($uploadPath))) {
                mkdir(dirname($uploadPath), 0777, true);
            }

            if (move_uploaded_file($_FILES['profile_picture']['tmp_name'], $uploadPath)) {
                if ($oldPhoto && $oldPhoto !== $this->defaultPhoto && file_exists(__DIR__ . '/../../uploads/profile_pictures/' . $oldPhoto)) {
                    unlink(__DIR__ . '/../../uploads/profile_pictures/' . $oldPhoto);
                }
            } else {
                return Response::json(['success' => false, 'message' => 'Falha ao salvar nova imagem. Caminho: ' . $uploadPath]);
            }
        }

        if (empty($newPhoto)) {
            $newPhoto = $this->defaultPhoto;
        }

        $ok = User::updateProfile($userId, $username, $newPhoto);

        return Response::json($ok ?
            ['success' => true, 'photo' => $newPhoto, 'message' => 'Perfil atualizado com sucesso!']
            : ['success' => false, 'message' => 'Erro ao atualizar perfil.']);
    }

    public function delete()
    {
        $userId = $_POST['user_id'] ?? $_GET['user_id'] ?? null;
        if (empty($userId)) {
            return Response::json(['success' => false, 'message' => 'ID do usuário faltando.']);
        }

        $photo = User::getProfilePhoto($userId);

        if ($photo && $photo !== $this->defaultPhoto && file_exists(__DIR__ . '/../../uploads/profile_pictures/' . $photo)) {
            unlink(__DIR__ . '/../../uploads/profile_pictures/' . $photo);
        }

        $ok = User::deleteUser($userId);

        return Response::json($ok ?
            ['success' => true, 'message' => 'Usuário deletado!']
            : ['success' => false, 'message' => 'Erro ao deletar usuário.']);
    }

    public function setBlocked()
    {
        $data = $_POST;
        $user_id = isset($data['user_id']) ? (int)$data['user_id'] : null;
        $blocked = isset($data['blocked']) ? (int)$data['blocked'] : 0;

        if (!$user_id) {
            Response::json(["success" => false, "message" => "ID do usuário faltando!"], 400);
            return;
        }

        $ok = User::setBlocked($user_id, $blocked);

        Response::json(["success" => $ok]);
    }
}
