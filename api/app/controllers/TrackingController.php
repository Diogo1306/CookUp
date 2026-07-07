<?php

require_once __DIR__ . '/../core/Response.php';
require_once __DIR__ . '/../models/Tracking.php';

class TrackingController
{
    public function trackInteraction()
    {
        $data = json_decode(file_get_contents('php://input'), true);

        if (!isset($data['user_id'], $data['recipe_id'], $data['interaction_type'])) {
            return Response::json(["success" => false, "message" => "Parâmetros em falta."], 422);
        }

        $userId = (int) $data['user_id'];
        $recipeId = (int) $data['recipe_id'];
        $type = $data['interaction_type'];

        $success = Tracking::registerInteraction($userId, $recipeId, $type);

        Response::json(
            $success
                ? ["success" => true, "message" => "Interação registada com sucesso."]
                : ["success" => false, "message" => "Erro ao registar interação."],
            $success ? 200 : 500
        );
    }
}
