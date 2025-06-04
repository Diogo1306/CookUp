<?php

require_once __DIR__ . '/../core/Database.php';

class Search
{
    /**
     * Busca global: receitas (por título e ingrediente), categorias de receita e ingredientes
     */
    public static function searchGlobal(string $query): array
    {
        $db = Database::connect();
        $searchTerm = '%' . strtolower(trim($query)) . '%';

        $response = [
            'recipes' => [],
            'recipe_categories' => [],
            'ingredients' => [],
        ];

        // Busca ingredientes
        $stmt = $db->prepare("SELECT * FROM ingredients WHERE LOWER(ingredient_name) LIKE ?");
        $stmt->bind_param("s", $searchTerm);
        $stmt->execute();
        $response['ingredients'] = $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        $stmt->close();

        // Busca categorias de receita
        $stmt = $db->prepare("SELECT * FROM categories WHERE LOWER(category_name) LIKE ?");
        $stmt->bind_param("s", $searchTerm);
        $stmt->execute();
        $response['recipe_categories'] = $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        $stmt->close();

        // Busca receitas por título e ingrediente (sem duplicar)
        $recipesMap = [];

        // Por título
        $stmt = $db->prepare("SELECT * FROM recipes WHERE LOWER(title) LIKE ?");
        $stmt->bind_param("s", $searchTerm);
        $stmt->execute();
        $res = $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        foreach ($res as $row) {
            $recipesMap[$row['recipe_id']] = $row;
        }
        $stmt->close();

        // Por ingrediente
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

        $response['recipes'] = array_values($recipesMap);

        return $response;
    }
}
