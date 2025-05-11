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

    public static function getByCategory($categoryId, $excludeIds = [])
    {
        $db = Database::connect();
        $sql = "SELECT r.* FROM recipes r
                JOIN recipe_category rc ON r.recipe_id = rc.recipe_id
                WHERE rc.category_id = ?";

        if (!empty($excludeIds)) {
            $safeIds = implode(',', array_map('intval', $excludeIds));
            $sql .= " AND r.recipe_id NOT IN ($safeIds)";
        }

        $sql .= " ORDER BY (r.favorites_count * 2 + r.views_count) DESC LIMIT 10";

        $stmt = $db->prepare($sql);
        $stmt->bind_param("i", $categoryId);
        $stmt->execute();
        $result = $stmt->get_result();

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
