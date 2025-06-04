<?php

require_once __DIR__ . '/../core/Database.php';

class UserStats
{
    /**
     * Atualiza stats do user numa categoria.
     * $field pode ser: views_count, favorites_count, finished_count.
     */
    public static function updateUserCategoryStats(int $userId, int $categoryId, string $field): bool
    {
        // Previne SQL injection
        $validFields = ['views_count', 'favorites_count', 'finished_count'];
        if (!in_array($field, $validFields)) return false;

        $db = Database::connect();
        $stmt = $db->prepare("
            INSERT INTO user_category_stats (user_id, category_id, $field)
            VALUES (?, ?, 1)
            ON DUPLICATE KEY UPDATE $field = $field + 1
        ");
        $stmt->bind_param("ii", $userId, $categoryId);
        $success = $stmt->execute();
        $stmt->close();
        return $success;
    }

    /**
     * Marca receita como finalizada pelo usuário.
     */
    public static function markRecipeAsFinished(int $userId, int $recipeId): bool
    {
        $db = Database::connect();
        $stmt = $db->prepare("
            INSERT IGNORE INTO user_recipe_finished (user_id, recipe_id)
            VALUES (?, ?)
        ");
        $stmt->bind_param("ii", $userId, $recipeId);
        $success = $stmt->execute();
        $stmt->close();
        return $success;
    }

    /**
     * Recomenda receitas para o user com base nas categorias mais vistas/favoritas/finalizadas.
     */
    public static function getRecommendedByUserId(int $userId, array $excludeIds = []): array
    {
        $db = Database::connect();

        $sql = "SELECT r.*, GROUP_CONCAT(c.category_name) AS category_names
                FROM recipes r
                JOIN recipe_category rc ON r.recipe_id = rc.recipe_id
                JOIN user_category_stats ucs ON ucs.category_id = rc.category_id
                LEFT JOIN categories c ON rc.category_id = c.category_id
                WHERE ucs.user_id = ?";

        if (!empty($excludeIds)) {
            $sql .= " AND r.recipe_id NOT IN (" . implode(',', array_map('intval', $excludeIds)) . ")";
        }

        $sql .= " GROUP BY r.recipe_id
                  ORDER BY (ucs.views_count * 1.5 + ucs.favorites_count * 3 + ucs.finished_count * 5) DESC,
                           (r.favorites_count * 2 + r.views_count) DESC LIMIT 10";

        $stmt = $db->prepare($sql);
        $stmt->bind_param("i", $userId);
        $stmt->execute();
        $result = $stmt->get_result();

        $recipes = [];
        while ($row = $result->fetch_assoc()) {
            $row['categories'] = explode(',', $row['category_names']);
            unset($row['category_names']);
            $recipes[] = $row;
        }
        $stmt->close();
        return $recipes;
    }
}
