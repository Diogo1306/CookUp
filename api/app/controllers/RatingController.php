<?php

require_once __DIR__ . '/../models/Rating.php';
require_once __DIR__ . '/../core/Response.php';

class RatingController
{
    public function submit()
    {
        $data = json_decode(file_get_contents("php://input"), true);

        if (!isset($data['user_id'], $data['recipe_id'], $data['rating'])) {
            return Response::json(["success" => false, "message" => "Dados em falta."]);
        }

        $rating = (int)$data['rating'];
        if ($rating < 1 || $rating > 5) {
            return Response::json(["success" => false, "message" => "A avaliação tem de ser entre 1 e 5."], 422);
        }

        $success = Rating::submit($data['user_id'], $data['recipe_id'], $rating);

        Response::json(
            $success
                ? ["success" => true, "message" => "Avaliação enviada com sucesso!"]
                : ["success" => false, "message" => "Erro ao enviar avaliação."]
        );
    }

    public function getAverage()
    {
        if (!isset($_GET['recipe_id'])) {
            Response::json(["success" => false, "message" => "ID da receita em falta."]);
            return;
        }

        $average = Rating::getAverage($_GET['recipe_id']);
        Response::json(["success" => true, "average" => $average]);
    }
}
