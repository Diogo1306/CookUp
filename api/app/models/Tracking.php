<?php

require_once __DIR__ . '/../core/Database.php';

class Tracking
{
    public static function registerInteraction($userId, $recipeId, $type)
    {
        $conn = Database::connect();

        $stmt = $conn->prepare("SELECT category_id FROM recipes WHERE recipe_id = ?");
        $stmt->bind_param("i", $recipeId);
        $stmt->execute();
        $result = $stmt->get_result();
        $category = $result->fetch_assoc();
        $categoryId = $category ? $category['category_id'] : null;

        if (!$categoryId) return false;

        switch ($type) {
            case 'view':
                $stmt = $conn->prepare("INSERT INTO recipe_views (user_id, recipe_id, viewed_at) VALUES (?, ?, NOW())");
                $stmt->bind_param("ii", $userId, $recipeId);
                $stmt->execute();

                $conn->query("UPDATE recipes SET views_count = views_count + 1 WHERE recipe_id = $recipeId");

                self::updateUserCategoryStats($conn, $userId, $categoryId, 'views_count');
                break;

            case 'favorite':
                $conn->query("UPDATE recipes SET favorites_count = favorites_count + 1 WHERE recipe_id = $recipeId");

                self::updateUserCategoryStats($conn, $userId, $categoryId, 'favorites_count');
                break;

            case 'finish':
                $stmt = $conn->prepare("INSERT INTO user_recipe_finished (user_id, recipe_id, finished_at) VALUES (?, ?, NOW())");
                $stmt->bind_param("ii", $userId, $recipeId);
                $stmt->execute();

                self::updateUserCategoryStats($conn, $userId, $categoryId, 'finished_count');
                break;

            default:
                return false;
        }

        return true;
    }

    private static function updateUserCategoryStats($conn, $userId, $categoryId, $field)
    {
        $check = $conn->prepare("SELECT * FROM user_category_stats WHERE user_id = ? AND category_id = ?");
        $check->bind_param("ii", $userId, $categoryId);
        $check->execute();
        $result = $check->get_result();

        if ($result->num_rows > 0) {
            $conn->query("UPDATE user_category_stats SET $field = $field + 1 WHERE user_id = $userId AND category_id = $categoryId");
        } else {
            $base = ['views_count' => 0, 'favorites_count' => 0, 'finished_count' => 0];
            $base[$field] = 1;
            $stmt = $conn->prepare("
                INSERT INTO user_category_stats (user_id, category_id, views_count, favorites_count, finished_count)
                VALUES (?, ?, ?, ?, ?)
            ");
            $stmt->bind_param("iiiii", $userId, $categoryId, $base['views_count'], $base['favorites_count'], $base['finished_count']);
            $stmt->execute();
        }
    }
}
