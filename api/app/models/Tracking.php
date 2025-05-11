<?php

require_once __DIR__ . '/../core/Database.php';

class Tracking
{
    public static function registerInteraction($userId, $recipeId, $type)
    {
        $conn = Database::connect();

        $stmt = $conn->prepare("SELECT category_id FROM recipe_category WHERE recipe_id = ?");
        $stmt->bind_param("i", $recipeId);
        $stmt->execute();
        $result = $stmt->get_result();

        $categoryIds = [];
        while ($row = $result->fetch_assoc()) {
            $categoryIds[] = $row['category_id'];
        }

        if (empty($categoryIds)) return false;

        switch ($type) {
            case 'view':
                $stmt = $conn->prepare("INSERT INTO recipe_views (user_id, recipe_id, viewed_at) VALUES (?, ?, NOW())");
                $stmt->bind_param("ii", $userId, $recipeId);
                $stmt->execute();

                foreach ($categoryIds as $categoryId) {
                    self::updateUserCategoryStats($conn, $userId, $categoryId, 'views_count');
                }
                break;

            case 'favorite':
                $conn->query("UPDATE recipes SET favorites_count = favorites_count + 1 WHERE recipe_id = $recipeId");

                foreach ($categoryIds as $categoryId) {
                    self::updateUserCategoryStats($conn, $userId, $categoryId, 'favorites_count');
                }
                break;

            case 'finish':
                $stmt = $conn->prepare("INSERT INTO user_recipe_finished (user_id, recipe_id, finished_at) VALUES (?, ?, NOW())");
                $stmt->bind_param("ii", $userId, $recipeId);
                $stmt->execute();

                foreach ($categoryIds as $categoryId) {
                    self::updateUserCategoryStats($conn, $userId, $categoryId, 'finished_count');
                }
                break;

            default:
                return false;
        }

        return true;
    }

    private static function updateUserCategoryStats($conn, $userId, $categoryId, $field)
    {
        $base = ['views_count' => 0, 'favorites_count' => 0, 'finished_count' => 0];
        $base[$field] = 1;

        $updateSql = "";
        switch ($field) {
            case 'views_count':
                $updateSql = "views_count = views_count + 1";
                break;
            case 'favorites_count':
                $updateSql = "favorites_count = favorites_count + 1";
                break;
            case 'finished_count':
                $updateSql = "finished_count = finished_count + 1";
                break;
            default:
                return;
        }

        $sql = "
            INSERT INTO user_category_stats (user_id, category_id, views_count, favorites_count, finished_count)
            VALUES (?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE $updateSql
        ";

        $stmt = $conn->prepare($sql);
        $stmt->bind_param(
            "iiiii",
            $userId,
            $categoryId,
            $base['views_count'],
            $base['favorites_count'],
            $base['finished_count']
        );
        $stmt->execute();
    }
}
