<?php

require_once __DIR__ . '/../models/SavedList.php';
require_once __DIR__ . '/../core/Response.php';

class SavedListController
{
    public function getLists()
    {
        $user_id = $_GET['user_id'] ?? null;
        if (!$user_id) {
            return Response::json(["success" => false, "message" => "ID do utilizador em falta."], 422);
        }
        $lists = SavedList::getByUser($user_id);
        return Response::json(["success" => true, "data" => $lists]);
    }

    public function create()
    {
        $data = json_decode(file_get_contents('php://input'), true) ?? $_POST;
        $user_id = $data['user_id'] ?? null;
        $list_name = $data['list_name'] ?? null;
        $color = $data['color'] ?? null;
        if (!$user_id || !$list_name) {
            return Response::json(["success" => false, "message" => "Dados em falta."], 422);
        }
        $success = SavedList::create($user_id, $list_name, $color);
        return Response::json(
            $success
                ? ["success" => true, "message" => "Lista criada com sucesso."]
                : ["success" => false, "message" => "Erro ao criar lista."],
            $success ? 200 : 500
        );
    }

    public function update()
    {
        $data = json_decode(file_get_contents('php://input'), true) ?? $_POST;
        $list_id = $data['list_id'] ?? null;
        $list_name = $data['list_name'] ?? null;
        $color = $data['color'] ?? null;
        if (!$list_id || !$list_name || !$color) {
            return Response::json(["success" => false, "message" => "Dados em falta."], 422);
        }
        $success = SavedList::update($list_id, $list_name, $color);
        return Response::json(
            $success
                ? ["success" => true, "message" => "Lista atualizada com sucesso."]
                : ["success" => false, "message" => "Erro ao atualizar a lista."],
            $success ? 200 : 500
        );
    }

    public function addRecipe()
    {
        $data = json_decode(file_get_contents('php://input'), true) ?? $_POST;
        $list_id = $data['list_id'] ?? null;
        $recipe_id = $data['recipe_id'] ?? null;
        if (!$list_id || !$recipe_id) {
            return Response::json(["success" => false, "message" => "Dados em falta."], 422);
        }
        $success = SavedList::addRecipe($list_id, $recipe_id);
        return Response::json(
            $success
                ? ["success" => true, "message" => "Receita adicionada com sucesso."]
                : ["success" => false, "message" => "A receita já está nesta lista ou erro ao adicionar."],
            $success ? 200 : 500
        );
    }

    public function getRecipes()
    {
        $list_id = $_GET['list_id'] ?? null;
        if (!$list_id) {
            return Response::json(["success" => false, "message" => "ID da lista em falta."], 422);
        }
        $recipes = SavedList::getRecipes($list_id);
        return Response::json(["success" => true, "data" => $recipes]);
    }

    public function delete()
    {
        $list_id = $_GET['list_id'] ?? null;
        if (!$list_id) {
            return Response::json(["success" => false, "message" => "ID da lista em falta."], 422);
        }
        $success = SavedList::delete($list_id);
        return Response::json(
            $success
                ? ["success" => true, "message" => "Lista eliminada com sucesso."]
                : ["success" => false, "message" => "Erro ao eliminar a lista."],
            $success ? 200 : 500
        );
    }

    public function removeRecipe()
    {
        $data = json_decode(file_get_contents('php://input'), true) ?? $_POST;
        $list_id = $data['list_id'] ?? null;
        $recipe_id = $data['recipe_id'] ?? null;
        if (!$list_id || !$recipe_id) {
            return Response::json(["success" => false, "message" => "Dados em falta."], 422);
        }
        $success = SavedList::removeRecipe($list_id, $recipe_id);
        return Response::json(
            $success
                ? ["success" => true, "message" => "Receita removida da lista com sucesso."]
                : ["success" => false, "message" => "Erro ao remover receita da lista."],
            $success ? 200 : 500
        );
    }

    public function getRecipeLists()
    {
        $recipe_id = $_GET['recipe_id'] ?? null;
        if (!$recipe_id) {
            return Response::json(["success" => false, "message" => "ID da receita em falta."], 422);
        }
        $listIds = SavedList::getListIdsByRecipe($recipe_id);
        return Response::json(["success" => true, "data" => $listIds]);
    }

    public function getUserSavedRecipeIds()
    {
        $user_id = $_GET['user_id'] ?? null;
        if (!$user_id) {
            return Response::json(["success" => false, "message" => "ID do utilizador em falta."], 422);
        }
        $recipeIds = SavedList::getRecipeIdsByUser($user_id);
        return Response::json(["success" => true, "data" => $recipeIds]);
    }

    public function getListsWithRecipes()
    {
        $user_id = $_GET['user_id'] ?? null;
        if (!$user_id) {
            return Response::json(["success" => false, "message" => "ID do utilizador em falta."], 422);
        }
        $lists = SavedList::getListsWithRecipesByUser($user_id);
        return Response::json(["success" => true, "data" => $lists]);
    }
}
