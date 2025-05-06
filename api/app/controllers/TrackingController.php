<?php

require_once __DIR__ . '/../core/Response.php';
require_once __DIR__ . '/../models/Tracking.php';


class TrackingController
{
    public function trackInteraction()
    {
        $input = json_decode(file_get_contents("php://input"), true);
        $userId = $input['user_id'] ?? null;
        $recipeId = $input['recipe_id'] ?? null;
        $type = $input['type'] ?? null;

        if (!$userId || !$recipeId || !$type) {
            Response::json(["success" => false, "message" => "Parâmetros em falta."]);
            return;
        }

        $success = Tracking::registerInteraction($userId, $recipeId, $type);

        if ($success) {
            Response::json(["success" => true, "message" => "Interação registada com sucesso."]);
        } else {
            Response::json(["success" => false, "message" => "Erro ao registar interação."]);
        }
    }
}
