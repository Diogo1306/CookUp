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
        $user = $result->num_rows > 0 ? $result->fetch_assoc() : null;
        $stmt->close();
        return $user;
    }

    /** Cria ou atualiza usuário (mensagem em PT-PT) */
    public static function save(string $firebase_uid, string $username, string $email, string $profile_picture): array
    {
        $db = Database::connect();
        $existingUser = self::getByUID($firebase_uid);

        // Username único (exceto para o próprio)
        $stmt = $db->prepare("SELECT firebase_uid FROM users WHERE username = ? AND firebase_uid != ?");
        $stmt->bind_param("ss", $username, $firebase_uid);
        $stmt->execute();
        $res = $stmt->get_result();
        $stmt->close();
        if ($res->num_rows > 0) {
            return ["success" => false, "message" => "O nome de utilizador já está em uso."];
        }

        // Email único (exceto para o próprio)
        $stmt = $db->prepare("SELECT firebase_uid FROM users WHERE email = ? AND firebase_uid != ?");
        $stmt->bind_param("ss", $email, $firebase_uid);
        $stmt->execute();
        $res = $stmt->get_result();
        $stmt->close();
        if ($res->num_rows > 0) {
            return ["success" => false, "message" => "O email já está em uso."];
        }

        if ($existingUser) {
            $stmt = $db->prepare("UPDATE users SET username = ?, email = ?, profile_picture = ?, updated_at = NOW() WHERE firebase_uid = ?");
            $stmt->bind_param("ssss", $username, $email, $profile_picture, $firebase_uid);
            $success = $stmt->execute();
            $stmt->close();
            return $success
                ? ["success" => true, "message" => "Conta atualizada com sucesso."]
                : ["success" => false, "message" => "Erro ao atualizar conta."];
        } else {
            $stmt = $db->prepare("INSERT INTO users (firebase_uid, username, email, profile_picture, created_at, updated_at) VALUES (?, ?, ?, ?, NOW(), NOW())");
            $stmt->bind_param("ssss", $firebase_uid, $username, $email, $profile_picture);
            $success = $stmt->execute();
            $stmt->close();
            return $success
                ? ["success" => true, "message" => "Conta criada com sucesso."]
                : ["success" => false, "message" => "Erro ao criar conta."];
        }
    }
}
