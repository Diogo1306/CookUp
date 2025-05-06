<?php

require_once __DIR__ . '/../models/SavedList.php';
require_once __DIR__ . '/../core/Response.php';

class SavedListController
{
    public function getLists()
    {
        $user_id = $_GET['user_id'] ?? null;

        if (!$user_id) {
            return Response::json(["success" => false, "message" => "ID do utilizador em falta"], 422);
        }

        $lists = SavedList::getListsByUser($user_id);
        return Response::json(["success" => true, "data" => $lists]);
    }

    public function createList()
    {
        $data = json_decode(file_get_contents('php://input'), true) ?? $_POST;

        $user_id = $data['user_id'] ?? null;
        $list_name = $data['list_name'] ?? null;
        $color = $data['color'] ?? null;

        if (!$user_id || !$list_name) {
            return Response::json(["success" => false, "message" => "Dados incompletos"], 422);
        }

        $result = SavedList::createList($user_id, $list_name, $color);
        return Response::json($result, $result['success'] ? 200 : 500);
    }

    public function updateList()
    {
        $data = json_decode(file_get_contents('php://input'), true) ?? $_POST;

        $list_id = $data['list_id'] ?? null;
        $list_name = $data['list_name'] ?? null;
        $color = $data['color'] ?? null;

        if (!$list_id || !$list_name || !$color) {
            return Response::json(["success" => false, "message" => "Dados incompletos"], 422);
        }

        $result = SavedList::updateList($list_id, $list_name, $color);
        return Response::json($result, $result['success'] ? 200 : 500);
    }

    public function addRecipe()
    {
        $data = json_decode(file_get_contents('php://input'), true) ?? $_POST;

        $list_id = $data['list_id'] ?? null;
        $recipe_id = $data['recipe_id'] ?? null;

        if (!$list_id || !$recipe_id) {
            return Response::json(["success" => false, "message" => "Dados incompletos"], 422);
        }

        $result = SavedList::addRecipeToList($list_id, $recipe_id);
        return Response::json($result, $result['success'] ? 200 : 500);
    }

    public function getRecipes()
    {
        $list_id = $_GET['list_id'] ?? null;

        if (!$list_id) {
            return Response::json(["success" => false, "message" => "Erro ao eliminar a lista"], 422);
        }

        $recipes = SavedList::getRecipesFromList($list_id);
        return Response::json(["success" => true, "data" => $recipes]);
    }

    public function deleteList()
    {
        $list_id = $_GET['list_id'] ?? null;

        if (!$list_id) {
            return Response::json(["success" => false, "message" => "Erro ao eliminar a lista"], 422);
        }

        $result = SavedList::deleteList($list_id);
        return Response::json(
            $result['success'] ?? false
                ? ["success" => true, "message" => "Lista eliminada"]
                : ["success" => false, "message" => "Erro ao eliminar a lista"],
            $result['success'] ?? false ? 200 : 500
        );
    }

    public function deleteRecipeFromList()
    {
        $list_id = $_POST['list_id'] ?? null;
        $recipe_id = $_POST['recipe_id'] ?? null;

        if (!$list_id || !$recipe_id) {
            return Response::json(["success" => false, "message" => "Dados incompletos"], 422);
        }

        $result = SavedList::deleteRecipeFromList($list_id, $recipe_id);
        return Response::json($result, $result['success'] ? 200 : 500);
    }

    public function getRecipeLists()
    {
        $recipe_id = $_GET['recipe_id'] ?? null;

        if (!$recipe_id) {
            return Response::json(["success" => false, "message" => "ID da receita em falta"], 422);
        }

        $listIds = SavedList::getListIdsWithRecipe($recipe_id);
        return Response::json(["success" => true, "data" => $listIds]);
    }

    public function getUserSavedRecipeIds()
    {
        $user_id = $_GET['user_id'] ?? null;

        if (!$user_id) {
            return Response::json(["success" => false, "message" => "ID do utilizador em falta"], 422);
        }

        $recipeIds = SavedList::getRecipeIdsByUser($user_id);
        return Response::json(["success" => true, "data" => $recipeIds]);
    }
}
