<?php

require_once __DIR__ . '/../core/Database.php';

class Tracking
{
    /**
     * Registra uma interação do usuário com uma receita.
     * Tipos: view, favorite, finish
     */
    public static function registerInteraction(int $userId, int $recipeId, string $type): bool
    {
        $db = Database::connect();

        // Busca categorias da receita
        $stmt = $db->prepare("SELECT category_id FROM recipe_category WHERE recipe_id = ?");
        $stmt->bind_param("i", $recipeId);
        $stmt->execute();
        $result = $stmt->get_result();

        $categoryIds = [];
        while ($row = $result->fetch_assoc()) {
            $categoryIds[] = $row['category_id'];
        }
        $stmt->close();

        if (empty($categoryIds)) return false;

        switch ($type) {
            case 'view':
                $stmt = $db->prepare("INSERT INTO recipe_views (user_id, recipe_id, viewed_at) VALUES (?, ?, NOW())");
                $stmt->bind_param("ii", $userId, $recipeId);
                $stmt->execute();
                $stmt->close();
                foreach ($categoryIds as $categoryId) {
                    self::updateUserCategoryStats($db, $userId, $categoryId, 'views_count');
                }
                break;

            case 'favorite':
                $update = $db->prepare("UPDATE recipes SET favorites_count = favorites_count + 1 WHERE recipe_id = ?");
                $update->bind_param("i", $recipeId);
                $update->execute();
                $update->close();
                foreach ($categoryIds as $categoryId) {
                    self::updateUserCategoryStats($db, $userId, $categoryId, 'favorites_count');
                }
                break;

            case 'finish':
                $stmt = $db->prepare("INSERT INTO user_recipe_finished (user_id, recipe_id, finished_at) VALUES (?, ?, NOW())");
                $stmt->bind_param("ii", $userId, $recipeId);
                $stmt->execute();
                $stmt->close();
                foreach ($categoryIds as $categoryId) {
                    self::updateUserCategoryStats($db, $userId, $categoryId, 'finished_count');
                }
                break;

            default:
                return false;
        }

        return true;
    }

    /** Atualiza estatísticas por categoria e tipo (privado) */
    private static function updateUserCategoryStats($db, int $userId, int $categoryId, string $field): void
    {
        $fields = ['views_count' => 0, 'favorites_count' => 0, 'finished_count' => 0];
        if (!isset($fields[$field])) return;
        $fields[$field] = 1;

        $sql = "
            INSERT INTO user_category_stats (user_id, category_id, views_count, favorites_count, finished_count)
            VALUES (?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE $field = $field + 1
        ";

        $stmt = $db->prepare($sql);
        $stmt->bind_param(
            "iiiii",
            $userId,
            $categoryId,
            $fields['views_count'],
            $fields['favorites_count'],
            $fields['finished_count']
        );
        $stmt->execute();
        $stmt->close();
    }

    // Conta quantas vezes receitas desse autor foram finalizadas
    public static function countFinishedByRecipeIds($recipeIds)
    {
        if (empty($recipeIds)) return 0;
        $db = Database::connect();
        $placeholders = implode(',', array_fill(0, count($recipeIds), '?'));
        $types = str_repeat('i', count($recipeIds));
        $sql = "SELECT COUNT(*) as finished_count FROM user_recipe_finished WHERE recipe_id IN ($placeholders)";
        $stmt = $db->prepare($sql);
        $stmt->bind_param($types, ...$recipeIds);
        $stmt->execute();
        $res = $stmt->get_result()->fetch_assoc();
        return intval($res['finished_count'] ?? 0);
    }

    // Conta quantas receitas o user já finalizou (de qualquer autor)
    public static function countFinishedByUserId($user_id)
    {
        $db = Database::connect();
        $stmt = $db->prepare("SELECT COUNT(*) as finished_count FROM user_recipe_finished WHERE user_id = ?");
        $stmt->bind_param("i", $user_id);
        $stmt->execute();
        $res = $stmt->get_result()->fetch_assoc();
        return intval($res['finished_count'] ?? 0);
    }

    public static function countUniqueUsersFinishedRecipe($recipeId)
    {
        $db = Database::connect();
        $stmt = $db->prepare("SELECT COUNT(DISTINCT user_id) as user_count FROM user_recipe_finished WHERE recipe_id = ?");
        $stmt->bind_param("i", $recipeId);
        $stmt->execute();
        $res = $stmt->get_result()->fetch_assoc();
        return intval($res['user_count'] ?? 0);
    }
}
