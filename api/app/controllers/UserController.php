<?php

require_once __DIR__ . '/../models/User.php';
require_once __DIR__ . '/../core/Response.php';

class UserController
{
    public function getUser()
    {
        $firebaseUid = $_GET['firebase_uid'] ?? null;

        if (!$firebaseUid) {
            Response::json(['success' => false, 'message' => "UID é obrigatório!"], 400);
            return;
        }

        $user = User::getUserByUID($firebaseUid);

        Response::json(
            $user
                ? ["success" => true, "data" => $user]
                : ["success" => false, "message" => "Usuário não encontrado!"],
            $user ? 200 : 404
        );
    }

    public function createOrUpdateUser()
    {
        $data = json_decode(file_get_contents('php://input'), true) ?? $_POST;

        if (!isset($data['firebase_uid'], $data['username'], $data['email'], $data['profile_picture'])) {
            Response::json(["success" => false, "message" => "Dados insuficientes!"], 400);
            return;
        }

        $message = User::createOrUpdateUser(
            $data['firebase_uid'],
            $data['username'],
            $data['email'],
            $data['profile_picture']
        );

        Response::json(
            $message === "O nome de utilizador já está em uso." || $message === "O email já está em uso." ? ["success" => false, "message" => $message] : ["success" => true, "message" => $message],
            $message === "O nome de utilizador já está em uso." || $message === "O email já está em uso." ? 409 : ($message === "Conta criada" || $message === "Conta atualizada" ? 200 : 500)
        );
    }
}
