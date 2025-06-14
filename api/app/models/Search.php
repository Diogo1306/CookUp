<?php

require_once __DIR__ . '/../core/Database.php';

class Search
{
    public static function searchGlobal(string $query): array
    {
        $db = Database::connect();
        $searchTerm = '%' . strtolower(trim($query)) . '%';

        $response = [
            'recipes' => [],
            'recipe_categories' => [],
            'ingredients' => [],
        ];

        $stmt = $db->prepare("SELECT * FROM ingredients WHERE LOWER(ingredient_name) LIKE ?");
        $stmt->bind_param("s", $searchTerm);
        $stmt->execute();
        $ingredients = $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        foreach ($ingredients as &$item) {
            $img = $item['image_url'] ?? $item['image'] ?? '';
            $item['ingredient_image'] = !empty($img)
                ? BASE_URL . UPLOADS_FOLDER . INGREDIENTS_FOLDER . $img
                : BASE_URL . DEFAULT_IMAGE;

            unset($item['image_url'], $item['image']);
        }
        $response['ingredients'] = $ingredients;
        $stmt->close();

        $stmt = $db->prepare("SELECT * FROM categories WHERE LOWER(category_name) LIKE ?");
        $stmt->bind_param("s", $searchTerm);
        $stmt->execute();
        $categories = $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        foreach ($categories as &$item) {
            $img = $item['image_url'] ?? $item['category_image_url'] ?? '';
            $item['category_image_url'] = !empty($img)
                ? BASE_URL . UPLOADS_FOLDER . CATEGORIES_FOLDER . $img
                : BASE_URL . DEFAULT_IMAGE;

            unset($item['image_url'], $item['image'], $item['category_image']); // limpa restos
        }
        $response['recipe_categories'] = $categories;
        $stmt->close();

        $recipesMap = [];

        $stmt = $db->prepare("SELECT * FROM recipes WHERE LOWER(title) LIKE ?");
        $stmt->bind_param("s", $searchTerm);
        $stmt->execute();
        $res = $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        foreach ($res as $row) {
            $recipesMap[$row['recipe_id']] = $row;
        }
        $stmt->close();

        $stmt = $db->prepare("
            SELECT DISTINCT r.* FROM recipes r
            JOIN recipe_ingredients ri ON r.recipe_id = ri.recipe_id
            JOIN ingredients i ON i.ingredient_id = ri.ingredient_id
            WHERE LOWER(i.ingredient_name) LIKE ?
        ");
        $stmt->bind_param("s", $searchTerm);
        $stmt->execute();
        $res = $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        foreach ($res as $row) {
            $recipesMap[$row['recipe_id']] = $row;
        }
        $stmt->close();

        $stmt = $db->prepare("
            SELECT DISTINCT r.* FROM recipes r
            JOIN recipe_category rc ON r.recipe_id = rc.recipe_id
            JOIN categories c ON c.category_id = rc.category_id
            WHERE LOWER(c.category_name) LIKE ?
        ");
        $stmt->bind_param("s", $searchTerm);
        $stmt->execute();
        $res = $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        foreach ($res as $row) {
            $recipesMap[$row['recipe_id']] = $row;
        }
        $stmt->close();

        $recipes = array_values($recipesMap);
        foreach ($recipes as &$item) {
            $img = $item['image'] ?? '';
            $item['image'] = !empty($img)
                ? BASE_URL . UPLOADS_FOLDER . RECIPES_FOLDER . $img
                : BASE_URL . DEFAULT_IMAGE;
        }
        $response['recipes'] = $recipes;

        return $response;
    }
}
