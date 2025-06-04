<?php

require_once __DIR__ . '/../core/Database.php';

class SavedList
{
    /** Retorna todas as listas de um usuário */
    public static function getByUser(int $user_id): array
    {
        $db = Database::connect();
        $stmt = $db->prepare("SELECT * FROM saved_lists WHERE user_id = ?");
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

    /** Cria nova lista */
    public static function create(int $user_id, string $list_name, string $color): bool
    {
        $db = Database::connect();
        $stmt = $db->prepare("INSERT INTO saved_lists (user_id, list_name, color) VALUES (?, ?, ?)");
        $stmt->bind_param("iss", $user_id, $list_name, $color);
        $success = $stmt->execute();
        $stmt->close();
        return $success;
    }

    /** Atualiza lista */
    public static function update(int $list_id, string $list_name, string $color): bool
    {
        $db = Database::connect();
        $stmt = $db->prepare("UPDATE saved_lists SET list_name = ?, color = ? WHERE list_id = ?");
        $stmt->bind_param("ssi", $list_name, $color, $list_id);
        $success = $stmt->execute();
        $stmt->close();
        return $success;
    }

    /** Deleta lista */
    public static function delete(int $list_id): bool
    {
        $db = Database::connect();
        $stmt = $db->prepare("DELETE FROM saved_lists WHERE list_id = ?");
        $stmt->bind_param("i", $list_id);
        $success = $stmt->execute();
        $stmt->close();
        return $success;
    }

    /** Remove receita de uma lista */
    public static function removeRecipe(int $list_id, int $recipe_id): bool
    {
        $db = Database::connect();
        $stmt = $db->prepare("DELETE FROM saved_recipes WHERE list_id = ? AND recipe_id = ?");
        $stmt->bind_param("ii", $list_id, $recipe_id);
        $success = $stmt->execute();
        $stmt->close();
        return $success;
    }

    /** Adiciona receita a uma lista */
    public static function addRecipe(int $list_id, int $recipe_id): bool
    {
        $db = Database::connect();
        // Verifica se já existe
        $check = $db->prepare("SELECT 1 FROM saved_recipes WHERE list_id = ? AND recipe_id = ?");
        $check->bind_param("ii", $list_id, $recipe_id);
        $check->execute();
        $exists = $check->get_result()->num_rows > 0;
        $check->close();

        if ($exists) return false; // já existe

        $stmt = $db->prepare("INSERT INTO saved_recipes (list_id, recipe_id) VALUES (?, ?)");
        $stmt->bind_param("ii", $list_id, $recipe_id);
        $success = $stmt->execute();
        $stmt->close();
        return $success;
    }

    /** Retorna receitas de uma lista */
    public static function getRecipes(int $list_id): array
    {
        $db = Database::connect();
        $stmt = $db->prepare("
            SELECT r.* 
            FROM recipes r 
            JOIN saved_recipes sr ON r.recipe_id = sr.recipe_id 
            WHERE sr.list_id = ?
        ");
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

    /** Retorna IDs de listas onde uma receita está */
    public static function getListIdsByRecipe(int $recipe_id): array
    {
        $db = Database::connect();
        $stmt = $db->prepare("SELECT list_id FROM saved_recipes WHERE recipe_id = ?");
        $stmt->bind_param("i", $recipe_id);
        $stmt->execute();
        $result = $stmt->get_result();
        $listIds = [];
        while ($row = $result->fetch_assoc()) {
            $listIds[] = (int)$row['list_id'];
        }
        $stmt->close();
        return $listIds;
    }

    /** Retorna IDs de receitas salvas pelo usuário */
    public static function getRecipeIdsByUser(int $user_id): array
    {
        $db = Database::connect();
        $stmt = $db->prepare("
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
            $recipeIds[] = (int)$row['recipe_id'];
        }
        $stmt->close();
        return $recipeIds;
    }
}
