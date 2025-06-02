<?php

require_once __DIR__ . '/../models/Recipe.php';
require_once __DIR__ . '/../core/Response.php';

class RecipeController
{
    public function getAllRecipes()
    {
        $recipes = Recipe::getAllRecipes();

        if (!empty($recipes)) {
            Response::json(["success" => true, "data" => $recipes]);
        } else {
            Response::json(["success" => false, "message" => "Nenhuma receita encontrada."]);
        }
    }

    public static function getRecipeDetail()
    {
        if (!isset($_GET['recipe_id'])) {
            return Response::json(['success' => false, 'message' => 'ID da receita em falta.']);
        }

        $recipe_id = intval($_GET['recipe_id']);
        $recipe = Recipe::getRecipeDetail($recipe_id);

        if ($recipe) {
            Response::json(['success' => true, 'data' => $recipe]);
        } else {
            Response::json(['success' => false, 'message' => 'Receita não encontrada.']);
        }
    }

    public function createOrUpdateRecipe()
    {
        $data = json_decode(file_get_contents("php://input"), true);

        if (!$data) {
            Response::json(["success" => false, "message" => "Dados inválidos."]);
            return;
        }

        $success = Recipe::createOrUpdateRecipe($data);

        if ($success) {
            Response::json(["success" => true, "message" => "Receita guardada com sucesso!"]);
        } else {
            Response::json(["success" => false, "message" => "Erro ao guardar receita."]);
        }
    }

    public function getPopularWithPagination()
    {
        $page = isset($_GET['page']) ? intval($_GET['page']) : 1;
        $data = Recipe::getPopularWithPagination($page);
        Response::json(['success' => true, 'data' => $data]);
    }



    public function getRecommendedRecipes()
    {
        $recipes = Recipe::getRecommendedRecipes();

        if (!empty($recipes)) {
            Response::json(["success" => true, "data" => $recipes]);
        } else {
            Response::json(["success" => false, "message" => "Nenhuma receita recomendada."]);
        }
    }

    public function getMostFavoritedRecipes()
    {
        $recipes = Recipe::getMostFavoritedRecipes();

        if (!empty($recipes)) {
            Response::json(["success" => true, "data" => $recipes]);
        } else {
            Response::json(["success" => false, "message" => "Nenhuma receita favorita encontrada."]);
        }
    }

    public function getRecipesByMealType()
    {
        if (!isset($_GET['category_id'])) {
            return Response::json(['success' => false, 'message' => 'Parâmetro category_id em falta.']);
        }

        $categoryId = intval($_GET['category_id']);
        $recipes = Recipe::getRecipesByMealType($categoryId);

        if (!empty($recipes)) {
            Response::json(["success" => true, "data" => $recipes]);
        } else {
            Response::json(["success" => false, "message" => "Nenhuma receita encontrada para esta categoria."]);
        }
    }
}
