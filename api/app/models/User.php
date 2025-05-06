<?php

require_once __DIR__ . '/../core/Database.php';

class User
{
    public static function getUserByUID($firebase_uid)
    {
        $conn = Database::connect();

        $stmt = $conn->prepare("SELECT * FROM users WHERE firebase_uid = ?");
        $stmt->bind_param("s", $firebase_uid);
        $stmt->execute();

        $result = $stmt->get_result();
        $user = $result->num_rows > 0 ? $result->fetch_assoc() : null;

        $stmt->close();
        return $user;
    }

    public static function createOrUpdateUser($firebase_uid, $username, $email, $profile_picture)
    {
        $conn = Database::connect();
        $existingUser = self::getUserByUID($firebase_uid);

        $stmtCheckUsername = $conn->prepare("SELECT firebase_uid FROM users WHERE username = ? AND firebase_uid != ?");
        $stmtCheckUsername->bind_param("ss", $username, $firebase_uid);
        $stmtCheckUsername->execute();
        $resultUsername = $stmtCheckUsername->get_result();
        $stmtCheckUsername->close();

        if ($resultUsername->num_rows > 0) {
            return "O nome de utilizador já está em uso.";
        }

        $stmtCheckEmail = $conn->prepare("SELECT firebase_uid FROM users WHERE email = ? AND firebase_uid != ?");
        $stmtCheckEmail->bind_param("ss", $email, $firebase_uid);
        $stmtCheckEmail->execute();
        $resultEmail = $stmtCheckEmail->get_result();
        $stmtCheckEmail->close();

        if ($resultEmail->num_rows > 0) {
            return "O email já está em uso.";
        }

        if ($existingUser) {
            $stmtUpdate = $conn->prepare("UPDATE users SET username = ?, email = ?, profile_picture = ?, updated_at = NOW() WHERE firebase_uid = ?");
            $stmtUpdate->bind_param("ssss", $username, $email, $profile_picture, $firebase_uid);
            $success = $stmtUpdate->execute();
            $stmtUpdate->close();

            return $success ? "Conta atualizada" : "Erro ao atualizar conta";
        } else {
            $stmtInsert = $conn->prepare("INSERT INTO users (firebase_uid, username, email, profile_picture, created_at, updated_at) VALUES (?, ?, ?, ?, NOW(), NOW())");
            $stmtInsert->bind_param("ssss", $firebase_uid, $username, $email, $profile_picture);
            $success = $stmtInsert->execute();
            $stmtInsert->close();

            return $success ? "Conta criada" : "Erro ao criar conta";
        }
    }
}
