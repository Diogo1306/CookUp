<?php

require_once __DIR__ . '/../models/Rating.php';
require_once __DIR__ . '/../core/Response.php';

class RatingController
{
    public static function submitRating()
    {
        $data = json_decode(file_get_contents("php://input"), true);

        if (!isset($data['user_id'], $data['recipe_id'], $data['rating'])) {
            Response::json(["success" => false, "message" => "Dados em falta."]);
            return;
        }

        $success = Rating::submitRating($data['user_id'], $data['recipe_id'], $data['rating']);

        if ($success) {
            Response::json(["success" => true, "message" => "Avaliação enviada com sucesso!"]);
        } else {
            Response::json(["success" => false, "message" => "Erro ao enviar avaliação."]);
        }
    }

    public static function getAverage()
    {
        if (!isset($_GET['recipe_id'])) {
            Response::json(["success" => false, "message" => "ID da receita em falta."]);
            return;
        }

        $media = Rating::getAverageRating($_GET['recipe_id']);
        Response::json(["success" => true, "average" => $media]);
    }
}
