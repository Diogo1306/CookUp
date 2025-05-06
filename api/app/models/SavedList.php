<?php

require_once __DIR__ . '/../core/Database.php';

class SavedList
{
    public static function getListsByUser($user_id)
    {
        $conn = Database::connect();
        $stmt = $conn->prepare("SELECT * FROM saved_lists WHERE user_id = ?");
        $stmt->bind_param("i", $user_id);
        $stmt->execute();

        $result = $stmt->get_result();
        $lists = [];

        while ($row = $result->fetch_assoc()) {
            $lists[] = $row;
        }

        $stmt->close();
        return $lists;
    }

    public static function createList($user_id, $list_name, $color)
    {
        $conn = Database::connect();
        $stmt = $conn->prepare("INSERT INTO saved_lists (user_id, list_name, color) VALUES (?, ?, ?)");
        $stmt->bind_param("iss", $user_id, $list_name, $color);

        $success = $stmt->execute();
        $stmt->close();

        return $success
            ? ["success" => true, "message" => "Lista criada com sucesso"]
            : ["success" => false, "message" => "Erro ao criar lista"];
    }

    public static function updateList($list_id, $list_name, $color)
    {
        $conn = Database::connect();
        $stmt = $conn->prepare("UPDATE saved_lists SET list_name = ?, color = ? WHERE list_id = ?");
        $stmt->bind_param("ssi", $list_name, $color, $list_id);

        $success = $stmt->execute();
        $stmt->close();

        return $success
            ? ["success" => true, "message" => "Lista atualizada com sucesso"]
            : ["success" => false, "message" => "Erro ao atualizar a lista"];
    }

    public static function deleteList($list_id)
    {
        $conn = Database::connect();
        $stmt = $conn->prepare("DELETE FROM saved_lists WHERE list_id = ?");
        $stmt->bind_param("i", $list_id);

        $success = $stmt->execute();
        $stmt->close();

        return $success
            ? ["success" => true, "message" => "Lista deletada com sucesso"]
            : ["success" => false, "message" => "Erro ao deletar lista"];
    }

    public static function deleteRecipeFromList($list_id, $recipe_id)
    {
        $conn = Database::connect();
        $stmt = $conn->prepare("DELETE FROM saved_recipes WHERE list_id = ? AND recipe_id = ?");
        $stmt->bind_param("ii", $list_id, $recipe_id);

        $success = $stmt->execute();
        $stmt->close();

        return $success
            ? ["success" => true, "message" => "Receita removida com sucesso"]
            : ["success" => false, "message" => "Erro ao remover receita"];
    }

    public static function addRecipeToList($list_id, $recipe_id)
    {
        $conn = Database::connect();

        $check = $conn->prepare("SELECT * FROM saved_recipes WHERE list_id = ? AND recipe_id = ?");
        $check->bind_param("ii", $list_id, $recipe_id);
        $check->execute();
        $exists = $check->get_result()->num_rows > 0;
        $check->close();

        if ($exists) {
            return ["success" => false, "message" => "A receita já está nesta lista"];
        }

        $stmt = $conn->prepare("INSERT INTO saved_recipes (list_id, recipe_id) VALUES (?, ?)");
        $stmt->bind_param("ii", $list_id, $recipe_id);

        $success = $stmt->execute();
        $stmt->close();

        return $success
            ? ["success" => true, "message" => "Receita adicionada com sucesso"]
            : ["success" => false, "message" => "Erro ao adicionar receita"];
    }


    public static function getRecipesFromList($list_id)
    {
        $conn = Database::connect();
        $stmt = $conn->prepare("SELECT r.* FROM recipes r JOIN saved_recipes sr ON r.recipe_id = sr.recipe_id WHERE sr.list_id = ?");
        $stmt->bind_param("i", $list_id);
        $stmt->execute();

        $result = $stmt->get_result();
        $recipes = [];

        while ($row = $result->fetch_assoc()) {
            $recipes[] = $row;
        }

        $stmt->close();
        return $recipes;
    }

    public static function getListIdsWithRecipe($recipe_id)
    {
        $conn = Database::connect();
        $stmt = $conn->prepare("SELECT list_id FROM saved_recipes WHERE recipe_id = ?");
        $stmt->bind_param("i", $recipe_id);
        $stmt->execute();

        $result = $stmt->get_result();
        $listIds = [];

        while ($row = $result->fetch_assoc()) {
            $listIds[] = (int) $row['list_id'];
        }

        $stmt->close();
        return $listIds;
    }

    public static function getRecipeIdsByUser($user_id)
    {
        $conn = Database::connect();
        $stmt = $conn->prepare("
            SELECT DISTINCT sr.recipe_id 
            FROM saved_recipes sr
            JOIN saved_lists sl ON sr.list_id = sl.list_id
            WHERE sl.user_id = ?
        ");
        $stmt->bind_param("i", $user_id);
        $stmt->execute();

        $result = $stmt->get_result();
        $recipeIds = [];

        while ($row = $result->fetch_assoc()) {
            $recipeIds[] = (int) $row['recipe_id'];
        }

        $stmt->close();
        return $recipeIds;
    }
}
