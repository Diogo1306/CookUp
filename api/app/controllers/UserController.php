<?php

require_once __DIR__ . '/../models/User.php';
require_once __DIR__ . '/../core/Response.php';

class UserController
{
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

    public function save()
    {
        $data = json_decode(file_get_contents('php://input'), true) ?? $_POST;

        if (!isset($data['firebase_uid'], $data['username'], $data['email'], $data['profile_picture'])) {
            Response::json(["success" => false, "message" => "Dados em falta!"], 400);
            return;
        }

        $result = User::save(
            $data['firebase_uid'],
            $data['username'],
            $data['email'],
            $data['profile_picture']
        );

        if ($result['success'] === false) {
            $status = ($result['message'] === "O nome de utilizador já está em uso." || $result['message'] === "O email já está em uso.") ? 409 : 500;
        } else {
            $status = 200;
        }

        Response::json($result, $status);
    }
}
