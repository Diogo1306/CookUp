<?php

require_once __DIR__ . '/../models/UserStats.php';
require_once __DIR__ . '/../core/Response.php';

class UserStatsController
{
    public function updateStat()
    {
        if (!isset($_POST['user_id'], $_POST['category_id'], $_POST['field'])) {
            Response::json(["success" => false, "message" => "Parâmetros faltando."]);
            return;
        }

        $userId = intval($_POST['user_id']);
        $categoryId = intval($_POST['category_id']);
        $field = $_POST['field'];

        if (!in_array($field, ['views_count', 'favorites_count', 'finished_count'])) {
            Response::json(["success" => false, "message" => "Campo inválido."]);
            return;
        }

        UserStats::updateUserCategoryStats($userId, $categoryId, $field);
        Response::json(["success" => true, "message" => "Estatística atualizada."]);
    }

    public function markFinished()
    {
        if (!isset($_POST['user_id'], $_POST['recipe_id'])) {
            Response::json(["success" => false, "message" => "Parâmetros faltando."]);
            return;
        }

        $userId = intval($_POST['user_id']);
        $recipeId = intval($_POST['recipe_id']);

        UserStats::markRecipeAsFinished($userId, $recipeId);
        Response::json(["success" => true, "message" => "Receita marcada como finalizada."]);
    }

    public function getUserRecommendations()
    {
        if (!isset($_GET['user_id'])) {
            Response::json(["success" => false, "message" => "ID do utilizador em falta."]);
            return;
        }

        $userId = intval($_GET['user_id']);
        $recipes = UserStats::getUserRecommendedRecipes($userId);

        if (!empty($recipes)) {
            Response::json(["success" => true, "data" => $recipes]);
        } else {
            Response::json(["success" => false, "message" => "Nenhuma recomendação disponível."]);
        }
    }
}
