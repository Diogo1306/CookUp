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
        require_once __DIR__ . '/Recipe.php'; // importante garantir que Recipe está incluso

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
            $row['image'] = self::buildImageUrl($row['image']);
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

    public static function getListsWithRecipesByUser(int $user_id): array
    {
        require_once __DIR__ . '/Recipe.php';
        $db = Database::connect();

        $stmt = $db->prepare("
        SELECT 
            sl.list_id,
            sl.user_id,
            sl.list_name,
            sl.color,
            r.recipe_id,
            r.title,
            r.image
        FROM saved_lists sl
        LEFT JOIN saved_recipes sr ON sl.list_id = sr.list_id
        LEFT JOIN recipes r ON sr.recipe_id = r.recipe_id
        WHERE sl.user_id = ?
        ORDER BY sl.list_id DESC
    ");
        $stmt->bind_param("i", $user_id);
        $stmt->execute();
        $result = $stmt->get_result();

        $lists = [];
        while ($row = $result->fetch_assoc()) {
            $list_id = $row['list_id'];
            if (!isset($lists[$list_id])) {
                $lists[$list_id] = [
                    'list_id' => $list_id,
                    'user_id' => $row['user_id'],
                    'list_name' => $row['list_name'],
                    'color' => $row['color'],
                    'recipes' => []
                ];
            }

            if (!empty($row['recipe_id'])) {
                $lists[$list_id]['recipes'][] = [
                    'recipe_id' => $row['recipe_id'],
                    'title' => $row['title'],
                    'image' => self::buildImageUrl($row['image'])
                ];
            }
        }
        $stmt->close();

        return array_values($lists);
    }


    private static function buildImageUrl($image)
    {
        if (empty($image)) return BASE_URL . DEFAULT_IMAGE;
        if (substr($image, 0, 4) === 'http') return $image;
        return BASE_URL . UPLOADS_FOLDER . RECIPES_FOLDER . $image;
    }
}
