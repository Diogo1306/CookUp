<?php

require_once __DIR__ . '/../models/Recipe.php';
require_once __DIR__ . '/../models/Rating.php';
require_once __DIR__ . '/../models/Tracking.php';
require_once __DIR__ . '/../core/Response.php'; // Não esqueça!

class ProfileController
{
    /** Retorna resumo do perfil de um usuário */
    public function getSummary()
    {
        if (!isset($_GET['user_id'])) {
            Response::json(['success' => false, 'message' => 'Parâmetro user_id em falta.']);
            return;
        }

        $user_id = intval($_GET['user_id']);
        $recipeIds = Recipe::getIdsByAuthor($user_id);
        $totalRecipes = count($recipeIds);

        $averageRating = 0;
        $totalViews = 0;
        $finishedCount = 0;

        if ($totalRecipes > 0) {
            $averageRating = Rating::getAverageByRecipeIds($recipeIds);
            $totalViews = Recipe::getTotalViewsByIds($recipeIds);
            $finishedCount = Tracking::countFinishedByRecipeIds($recipeIds);
        }

        $result = [
            'total_recipes'   => $totalRecipes,
            'average_rating'  => $averageRating,
            'total_views'     => $totalViews,
            'finished_count'  => $finishedCount,
        ];

        Response::json(['success' => true, 'data' => $result]);
    }

    /** Retorna todas as receitas do usuário */
    public function getRecipes()
    {
        if (!isset($_GET['user_id'])) {
            Response::json(['success' => false, 'message' => 'Parâmetro user_id em falta.']);
            return;
        }

        $user_id = intval($_GET['user_id']);
        $recipes = Recipe::getAllByAuthor($user_id);

        Response::json(
            !empty($recipes)
                ? ['success' => true, 'recipes' => $recipes]
                : ['success' => false, 'message' => 'Nenhuma receita encontrada para este utilizador.']
        );
    }
}
