<?php

require_once __DIR__ . '/../core/Database.php';

class HomeFeed
{
    /**
     * Retorna receitas criadas nos últimos 7 dias, excluindo os IDs informados.
     */
    public static function getWeekly(array $excludeIds = []): array
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
            $row['image'] = self::buildImageUrl($row['image']);
            $recipes[] = $row;
        }
        return $recipes;
    }

    /**
     * Retorna receitas mais populares, excluindo os IDs informados.
     */
    public static function getPopular(array $excludeIds = []): array
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
            $row['image'] = self::buildImageUrl($row['image']);
            $recipes[] = $row;
        }
        return $recipes;
    }

    /**
     * Retorna receitas de uma categoria, excluindo os IDs informados.
     */
    public static function getByCategory(int $categoryId, array $excludeIds = []): array
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
            $row['image'] = self::buildImageUrl($row['image']);
            $recipes[] = $row;
        }
        return $recipes;
    }

    /**
     * Retorna receitas mais recentes (fallback), excluindo os IDs informados.
     */
    public static function getFallback(int $limit = 10, array $excludeIds = []): array
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
            $row['image'] = self::buildImageUrl($row['image']);
            $recipes[] = $row;
        }
        return $recipes;
    }

    private static function buildImageUrl($image)
    {
        if (empty($image)) return BASE_URL . DEFAULT_IMAGE;
        if (substr($image, 0, 4) === 'http') return $image;
        return BASE_URL . UPLOADS_FOLDER . RECIPES_FOLDER . $image;
    }
}
