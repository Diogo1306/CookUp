<?php

require_once __DIR__ . '/../core/Database.php';

class User
{
    /** Busca user pelo UID do Firebase */
    public static function getByUID(string $firebase_uid): ?array
    {
        $db = Database::connect();
        $stmt = $db->prepare("SELECT * FROM users WHERE firebase_uid = ?");
        $stmt->bind_param("s", $firebase_uid);
        $stmt->execute();
        $result = $stmt->get_result();

        if ($result->num_rows > 0) {
            $user = $result->fetch_assoc();

            if (empty($user['profile_picture'])) {
                $photoUrl = BASE_URL . UPLOADS_FOLDER . 'profile_pictures/default.png';
            } else if (filter_var($user['profile_picture'], FILTER_VALIDATE_URL)) {
                $photoUrl = $user['profile_picture'];
            } else {
                $photoUrl = BASE_URL . UPLOADS_FOLDER . 'profile_pictures/' . $user['profile_picture'];
            }

            $user['profile_picture'] = $photoUrl;

            return $user;
        }
        return null;
    }

    public static function getAllUsers()
    {
        $db = Database::connect();
        $result = $db->query("SELECT * FROM users ORDER BY created_at DESC");
        $users = [];
        while ($row = $result->fetch_assoc()) {
            if (empty($row['profile_picture'])) {
                $row['profile_picture'] = BASE_URL . UPLOADS_FOLDER . 'profile_pictures/default.png';
            } else if (filter_var($row['profile_picture'], FILTER_VALIDATE_URL)) {
            } else {
                $row['profile_picture'] = BASE_URL . UPLOADS_FOLDER . 'profile_pictures/' . $row['profile_picture'];
            }
            $users[] = $row;
        }
        return $users;
    }

    /** 
     * Cria ou atualiza usuário 
     */
    public static function save(string $firebase_uid, string $username, string $email, string $profile_picture, string $role = "user", int $blocked = 0): array
    {
        $db = Database::connect();
        $existingUser = self::getByUID($firebase_uid);

        $stmt = $db->prepare("SELECT firebase_uid FROM users WHERE username = ? AND firebase_uid != ?");
        $stmt->bind_param("ss", $username, $firebase_uid);
        $stmt->execute();
        $res = $stmt->get_result();
        $stmt->close();
        if ($res->num_rows > 0) {
            if (!empty($profile_picture)) {
                $username = $email;
            } else {
                return ["success" => false, "message" => "O nome de utilizador já está em uso."];
            }
        }

        $stmt = $db->prepare("SELECT firebase_uid FROM users WHERE email = ? AND firebase_uid != ?");
        $stmt->bind_param("ss", $email, $firebase_uid);
        $stmt->execute();
        $res = $stmt->get_result();
        $stmt->close();
        if ($res->num_rows > 0) {
            return ["success" => false, "message" => "O email já está em uso."];
        }

        if ($existingUser) {
            $stmt = $db->prepare("UPDATE users SET username = ?, email = ?, profile_picture = ?, role = ?, blocked = ?, updated_at = NOW() WHERE firebase_uid = ?");
            $stmt->bind_param("ssssss", $username, $email, $profile_picture, $role, $blocked, $firebase_uid);
            $success = $stmt->execute();
            $stmt->close();
            return $success
                ? ["success" => true, "message" => "Conta atualizada com sucesso."]
                : ["success" => false, "message" => "Erro ao atualizar conta."];
        } else {
            $stmt = $db->prepare("INSERT INTO users (firebase_uid, username, email, profile_picture, role, blocked, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, NOW(), NOW())");
            $stmt->bind_param("sssssi", $firebase_uid, $username, $email, $profile_picture, $role, $blocked);
            $success = $stmt->execute();
            $stmt->close();
            return $success
                ? ["success" => true, "message" => "Conta criada com sucesso."]
                : ["success" => false, "message" => "Erro ao criar conta."];
        }
    }

    public static function updateAllAdmin($userId, $username, $email, $role, $photo)
    {
        $db = Database::connect();
        $stmt = $db->prepare("UPDATE users SET username = ?, email = ?, role = ?, profile_picture = ?, updated_at = NOW() WHERE user_id = ?");
        $stmt->bind_param("ssssi", $username, $email, $role, $photo, $userId);
        $ok = $stmt->execute();
        $stmt->close();
        return $ok;
    }

    /**
     * Retorna o nome do arquivo da foto de perfil do usuário
     */
    public static function getProfilePhoto($userId): ?string
    {
        $db = Database::connect();
        $stmt = $db->prepare("SELECT profile_picture FROM users WHERE user_id = ?");
        $stmt->bind_param("i", $userId);
        $stmt->execute();
        $result = $stmt->get_result();

        if ($row = $result->fetch_assoc()) {
            return $row['profile_picture'];
        }
        return null;
    }

    /**
     * Atualiza o nome de usuário e a foto de perfil
     */

    public static function updateProfile($userId, $username, $photo)
    {
        $db = Database::connect();
        $stmt = $db->prepare("UPDATE users SET username = ?, profile_picture = ? WHERE user_id = ?");
        $stmt->bind_param("ssi", $username, $photo, $userId);
        $ok = $stmt->execute();
        $stmt->close();
        return $ok;
    }

    /**
     * Deleta o usuário do banco
     */

    public static function deleteUser($userId)
    {
        $db = Database::connect();
        $stmt = $db->prepare("DELETE FROM users WHERE user_id = ?");
        $stmt->bind_param("i", $userId);
        $ok = $stmt->execute();
        $stmt->close();
        return $ok;
    }

    public static function setBlocked($userId, $blocked)
    {
        $db = Database::connect();
        $stmt = $db->prepare("UPDATE users SET blocked = ?, updated_at = NOW() WHERE user_id = ?");
        $stmt->bind_param("ii", $blocked, $userId);
        $stmt->execute();
        $ok = $stmt->affected_rows > 0;
        $stmt->close();
        return $ok;
    }
}
