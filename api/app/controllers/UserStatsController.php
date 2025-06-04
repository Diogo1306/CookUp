<?php

require_once __DIR__ . '/../models/UserStats.php';
require_once __DIR__ . '/../core/Response.php';

class UserStatsController
{
    public function updateStat()
    {
        if (!isset($_POST['user_id'], $_POST['category_id'], $_POST['field'])) {
            return Response::json(["success" => false, "message" => "Parâmetros em falta."], 400);
        }

        $userId = intval($_POST['user_id']);
        $categoryId = intval($_POST['category_id']);
        $field = $_POST['field'];

        if (!in_array($field, ['views_count', 'favorites_count', 'finished_count'])) {
            return Response::json(["success" => false, "message" => "Campo inválido."], 422);
        }

        $success = UserStats::updateUserCategoryStats($userId, $categoryId, $field);

        return Response::json(
            $success
                ? ["success" => true, "message" => "Estatística atualizada com sucesso."]
                : ["success" => false, "message" => "Erro ao atualizar estatística."],
            $success ? 200 : 500
        );
    }

    public function markFinished()
    {
        if (!isset($_POST['user_id'], $_POST['recipe_id'])) {
            return Response::json(["success" => false, "message" => "Parâmetros em falta."], 400);
        }

        $userId = intval($_POST['user_id']);
        $recipeId = intval($_POST['recipe_id']);

        $success = UserStats::markRecipeAsFinished($userId, $recipeId);

        return Response::json(
            $success
                ? ["success" => true, "message" => "Receita marcada como finalizada."]
                : ["success" => false, "message" => "Erro ao marcar receita como finalizada."],
            $success ? 200 : 500
        );
    }
}
