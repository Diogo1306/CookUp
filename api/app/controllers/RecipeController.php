<?php

require_once __DIR__ . '/../models/Recipe.php';
require_once __DIR__ . '/../core/Response.php';

class RecipeController
{
    public function getAll()
    {
        $recipes = Recipe::getAll();
        Response::json(
            !empty($recipes)
                ? ["success" => true, "data" => $recipes]
                : ["success" => false, "message" => "Nenhuma receita encontrada."]
        );
    }

    public function getById()
    {
        if (!isset($_GET['recipe_id'])) {
            return Response::json(['success' => false, 'message' => 'ID da receita em falta.']);
        }

        $recipe_id = intval($_GET['recipe_id']);
        $recipe = Recipe::getById($recipe_id);

        Response::json(
            $recipe
                ? ['success' => true, 'data' => $recipe]
                : ['success' => false, 'message' => 'Receita não encontrada.']
        );
    }

    public function save()
    {
        $data = json_decode(file_get_contents("php://input"), true);

        if (!$data) {
            Response::json(["success" => false, "message" => "Dados inválidos."]);
            return;
        }

        $success = Recipe::save($data);

        Response::json(
            $success
                ? ["success" => true, "message" => "Receita guardada com sucesso!"]
                : ["success" => false, "message" => "Erro ao guardar receita."]
        );
    }

    public function getPopularPaginated()
    {
        $page = isset($_GET['page']) ? intval($_GET['page']) : 1;
        $data = Recipe::getPopularWithPagination($page);
        Response::json(['success' => true, 'data' => $data]);
    }

    public function getRecommended()
    {
        $recipes = Recipe::getRecommendedRecipes();
        Response::json(
            !empty($recipes)
                ? ["success" => true, "data" => $recipes]
                : ["success" => false, "message" => "Nenhuma receita recomendada."]
        );
    }

    public function getMostFavorited()
    {
        $recipes = Recipe::getMostFavoritedRecipes();
        Response::json(
            !empty($recipes)
                ? ["success" => true, "data" => $recipes]
                : ["success" => false, "message" => "Nenhuma receita favorita encontrada."]
        );
    }

    public function getByCategory()
    {
        if (!isset($_GET['category_id'])) {
            return Response::json(['success' => false, 'message' => 'Parâmetro category_id em falta.']);
        }

        $categoryId = intval($_GET['category_id']);
        $recipes = Recipe::getRecipesByMealType($categoryId);

        Response::json(
            !empty($recipes)
                ? ["success" => true, "data" => $recipes]
                : ["success" => false, "message" => "Nenhuma receita encontrada para esta categoria."]
        );
    }
}
