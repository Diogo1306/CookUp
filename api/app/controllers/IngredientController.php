<?php

require_once __DIR__ . '/../models/ingredient.php';
require_once __DIR__ . '/../core/Database.php';

class IngredientController
{
    public function addIngredientToRecipe()
    {
        $data = json_decode(file_get_contents('php://input'), true);

        if (!isset($data['ingredient_name'], $data['quantity'], $data['recipe_id'])) {
            return Response::json(["success" => false, "message" => "Parâmetros obrigatórios em falta."], 422);
        }

        $ingredientName = trim($data['ingredient_name']);
        $quantity = trim($data['quantity']);
        $recipeId = (int)$data['recipe_id'];

        $result = Ingredient::addToRecipe($ingredientName, $quantity, $recipeId);

        return Response::json($result);
    }

    public function autocomplete()
    {
        $input = isset($_GET['q']) ? trim($_GET['q']) : '';
        if (strlen($input) < 2) {
            return Response::json([], 200);
        }
        $pdo = Database::connect();
        $results = Ingredient::autocomplete($pdo, $input);
        return Response::json($results, 200);
    }
}
