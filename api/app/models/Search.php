<?php

require_once __DIR__ . '/../core/Database.php';

class Search
{
    public static function globalSearch($query)
    {
        $conn = Database::connect();
        $searchTerm = '%' . strtolower(trim($query)) . '%';

        $response = [
            'recipes' => [],
            'recipe_categories' => [],
            'ingredients' => [],
        ];

        $stmt = $conn->prepare("SELECT * FROM ingredients WHERE LOWER(ingredient_name) LIKE ?");
        $stmt->bind_param("s", $searchTerm);
        $stmt->execute();
        $response['ingredients'] = $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        $stmt->close();

        $stmt = $conn->prepare("
            SELECT DISTINCT c.* FROM categories c
            JOIN recipe_category rc ON c.category_id = rc.category_id
            WHERE LOWER(c.category_name) LIKE ?
        ");
        $stmt->bind_param("s", $searchTerm);
        $stmt->execute();
        $response['recipe_categories'] = $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        $stmt->close();

        $stmt = $conn->prepare("SELECT * FROM recipes WHERE LOWER(title) LIKE ?");
        $stmt->bind_param("s", $searchTerm);
        $stmt->execute();
        $recipes = $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        $stmt->close();
        $response['recipes'] = array_merge($response['recipes'], $recipes);

        $stmt = $conn->prepare("
            SELECT DISTINCT r.* FROM recipes r
            JOIN recipe_ingredients ri ON r.recipe_id = ri.recipe_id
            JOIN ingredients i ON i.ingredient_id = ri.ingredient_id
            WHERE LOWER(i.ingredient_name) LIKE ?
        ");
        $stmt->bind_param("s", $searchTerm);
        $stmt->execute();
        $recipesByIngredient = $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        $stmt->close();

        foreach ($recipesByIngredient as $r) {
            if (!in_array($r, $response['recipes'])) {
                $response['recipes'][] = $r;
            }
        }

        $stmt = $conn->prepare("
            SELECT DISTINCT r.* FROM recipes r
            JOIN recipe_ingredients ri ON r.recipe_id = ri.recipe_id
            JOIN ingredients i ON i.ingredient_id = ri.ingredient_id
            JOIN ingredient_categories c ON i.category_id = c.category_id
            WHERE LOWER(c.category_name) LIKE ?
        ");
        $stmt->bind_param("s", $searchTerm);
        $stmt->execute();
        $recipesByCategory = $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        $stmt->close();

        foreach ($recipesByCategory as $r) {
            if (!in_array($r, $response['recipes'])) {
                $response['recipes'][] = $r;
            }
        }

        return $response;
    }
}
