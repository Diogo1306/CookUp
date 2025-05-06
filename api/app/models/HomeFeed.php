<?php

require_once __DIR__ . '/../core/Database.php';
require_once __DIR__ . '/Rating.php';

class HomeFeed
{
    public static function getWeeklyRecipes($excludeIds = [])
    {
        $db = Database::connect();
        $sql = "SELECT * FROM recipes WHERE created_at >= DATE_SUB(NOW(), INTERVAL 7 DAY)";

        if (!empty($excludeIds)) {
            $sql .= " AND recipe_id NOT IN (" . implode(',', array_map('intval', $excludeIds)) . ")";
        }

        $sql .= " ORDER BY (favorites_count * 2 + views_count) DESC LIMIT 10";

        $stmt = $db->prepare($sql);
        $stmt->execute();
        $result = $stmt->get_result();
        $recipes = [];

        while ($row = $result->fetch_assoc()) {
            $row['average_rating'] = Rating::getAverageRating($row['recipe_id']);
            $recipes[] = $row;
        }

        return $recipes;
    }

    public static function getPopularRecipesFiltered($excludeIds = [])
    {
        $db = Database::connect();
        $sql = "SELECT * FROM recipes";

        if (!empty($excludeIds)) {
            $sql .= " WHERE recipe_id NOT IN (" . implode(',', array_map('intval', $excludeIds)) . ")";
        }

        $sql .= " ORDER BY views_count DESC LIMIT 10";

        $stmt = $db->prepare($sql);
        $stmt->execute();
        $result = $stmt->get_result();
        $recipes = [];

        while ($row = $result->fetch_assoc()) {
            $row['average_rating'] = Rating::getAverageRating($row['recipe_id']);
            $recipes[] = $row;
        }

        return $recipes;
    }

    public static function getTopUserCategories($userId)
    {
        $conn = Database::connect();

        $stmt = $conn->prepare("SELECT category_id FROM user_category_stats WHERE user_id = ? ORDER BY (views_count + favorites_count * 2 + finished_count * 3) DESC LIMIT 3");
        $stmt->bind_param("i", $userId);
        $stmt->execute();
        $result = $stmt->get_result();
        $categories = [];

        while ($row = $result->fetch_assoc()) {
            $categories[] = $row['category_id'];
        }

        if (count($categories) < 3) {
            $idsString = implode(",", $categories ?: [0]);
            $limit = 3 - count($categories);

            $stmt2 = $conn->prepare("SELECT category_id FROM recipes WHERE category_id NOT IN ($idsString) GROUP BY category_id ORDER BY COUNT(*) DESC LIMIT $limit");
            $stmt2->execute();
            $result2 = $stmt2->get_result();

            while ($row = $result2->fetch_assoc()) {
                $categories[] = $row['category_id'];
            }
        }

        return $categories;
    }

    public static function getByCategory($categoryId, $excludeIds = [])
    {
        $db = Database::connect();
        $sql = "SELECT * FROM recipes WHERE category_id = " . intval($categoryId);

        if (!empty($excludeIds)) {
            $safeIds = implode(',', array_map('intval', $excludeIds));
            $sql .= " AND recipe_id NOT IN ($safeIds)";
        }

        $sql .= " ORDER BY (favorites_count * 2 + views_count) DESC LIMIT 10";

        $result = $db->query($sql);
        $recipes = [];

        while ($row = $result->fetch_assoc()) {
            $row['average_rating'] = Rating::getAverageRating($row['recipe_id']);
            $recipes[] = $row;
        }

        return $recipes;
    }

    public static function getFallbackRecipes($limit = 10, $excludeIds = [])
    {
        $db = Database::connect();
        $sql = "SELECT * FROM recipes";

        if (!empty($excludeIds)) {
            $sql .= " WHERE recipe_id NOT IN (" . implode(',', array_map('intval', $excludeIds)) . ")";
        }

        $sql .= " ORDER BY created_at DESC LIMIT $limit";

        $stmt = $db->prepare($sql);
        $stmt->execute();
        $result = $stmt->get_result();
        $recipes = [];

        while ($row = $result->fetch_assoc()) {
            $row['average_rating'] = Rating::getAverageRating($row['recipe_id']);
            $recipes[] = $row;
        }

        return $recipes;
    }
}
