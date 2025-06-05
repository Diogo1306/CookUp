<?php

require_once __DIR__ . '/../core/Database.php';

class Recipe
{
    public static function save($data): bool
    {
        $db = Database::connect();

        $stmt = $db->prepare("
            INSERT INTO recipes (
                recipe_id, author_id, title, description, instructions,
                preparation_time, servings, image, utensils, difficulty, created_at, updated_at
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())
            ON DUPLICATE KEY UPDATE 
                title = VALUES(title),
                description = VALUES(description),
                instructions = VALUES(instructions),
                preparation_time = VALUES(preparation_time),
                servings = VALUES(servings),
                image = VALUES(image),
                utensils = VALUES(utensils),
                difficulty = VALUES(difficulty),
                updated_at = NOW()
        ");
        $stmt->bind_param(
            "iisssissss",
            $data['recipe_id'],
            $data['author_id'],
            $data['title'],
            $data['description'],
            $data['instructions'],
            $data['preparation_time'],
            $data['servings'],
            $data['image'],
            $data['utensils'],
            $data['difficulty']
        );

        $success = $stmt->execute();
        $stmt->close();

        if (!$success) return false;

        // Lida com categorias (troca todas)
        $recipeId = $data['recipe_id'] ?: $db->insert_id;
        $db->query("DELETE FROM recipe_category WHERE recipe_id = $recipeId");
        if (!empty($data['categories']) && is_array($data['categories'])) {
            $insert = $db->prepare("INSERT INTO recipe_category (recipe_id, category_id) VALUES (?, ?)");
            foreach ($data['categories'] as $catId) {
                $insert->bind_param("ii", $recipeId, $catId);
                $insert->execute();
            }
            $insert->close();
        }

        return true;
    }

    /** Deleta uma receita (e tudo relacionado via cascade) */
    public static function delete($recipeId): bool
    {
        $db = Database::connect();
        $stmt = $db->prepare("DELETE FROM recipes WHERE recipe_id = ?");
        $stmt->bind_param("i", $recipeId);
        return $stmt->execute();
    }


    /** 10 receitas com mais favoritos */
    public static function getMostFavoritedRecipes()
    {
        $db = Database::connect();
        $stmt = $db->prepare("
        SELECT r.*, 
            GROUP_CONCAT(c.category_id) AS category_ids,
            GROUP_CONCAT(c.category_name) AS category_names
        FROM recipes r
        LEFT JOIN recipe_category rc ON rc.recipe_id = r.recipe_id
        LEFT JOIN categories c ON c.category_id = rc.category_id
        GROUP BY r.recipe_id
        ORDER BY r.favorites_count DESC
        LIMIT 10
    ");
        $stmt->execute();
        $result = $stmt->get_result();
        $recipes = [];
        while ($row = $result->fetch_assoc()) {
            $recipes[] = $row;
        }
        return $recipes;
    }

    // receitas recomendadas (baseado em favoritos e visualizações e categorias)
    public static function getRecommendedRecipes()
    {
        $db = Database::connect();
        $stmt = $db->prepare("
        SELECT r.*, 
            GROUP_CONCAT(c.category_id) AS category_ids,
            GROUP_CONCAT(c.category_name) AS category_names
        FROM recipes r
        LEFT JOIN recipe_category rc ON rc.recipe_id = r.recipe_id
        LEFT JOIN categories c ON c.category_id = rc.category_id
        GROUP BY r.recipe_id
        ORDER BY (r.favorites_count * 2 + r.views_count) DESC
        LIMIT 10
    ");
        $stmt->execute();
        $result = $stmt->get_result();
        $recipes = [];
        while ($row = $result->fetch_assoc()) {
            $recipes[] = $row;
        }
        return $recipes;
    }

    /** Pega receitas por tipo de refeição (café da manhã, almoço, etc.) */

    public static function getRecipesByMealType($categoryId)
    {
        $db = Database::connect();
        $stmt = $db->prepare("
        SELECT r.*, 
            GROUP_CONCAT(c.category_id) AS category_ids,
            GROUP_CONCAT(c.category_name) AS category_names
        FROM recipes r
        LEFT JOIN recipe_category rc ON rc.recipe_id = r.recipe_id
        LEFT JOIN categories c ON c.category_id = rc.category_id
        WHERE c.category_id = ?
        GROUP BY r.recipe_id
        ORDER BY (r.favorites_count * 2 + r.views_count) DESC
        LIMIT 10
    ");
        $stmt->bind_param("i", $categoryId);
        $stmt->execute();
        $result = $stmt->get_result();
        $recipes = [];
        while ($row = $result->fetch_assoc()) {
            $recipes[] = $row;
        }
        return $recipes;
    }


    /** Retorna array de receitas (com categorias já formatadas) */
    public static function getAll(): array
    {
        $db = Database::connect();
        $query = "
            SELECT r.*, 
                GROUP_CONCAT(c.category_id) AS category_ids,
                GROUP_CONCAT(c.category_name) AS category_names,
                GROUP_CONCAT(c.color_hex) AS category_colors,
                GROUP_CONCAT(c.image_url) AS category_images
            FROM recipes r
            LEFT JOIN recipe_category rc ON rc.recipe_id = r.recipe_id
            LEFT JOIN categories c ON c.category_id = rc.category_id
            GROUP BY r.recipe_id
        ";
        $stmt = $db->prepare($query);
        $stmt->execute();
        $result = $stmt->get_result();

        $recipes = [];
        while ($row = $result->fetch_assoc()) {
            $row['categories'] = self::parseCategories($row);
            unset($row['category_ids'], $row['category_names'], $row['category_colors'], $row['category_images']);
            $recipes[] = $row;
        }
        return $recipes;
    }

    /** Detalhe de receita (inclui ingredientes e categorias) */
    public static function getById($recipeId): ?array
    {
        $db = Database::connect();
        $stmt = $db->prepare("
            SELECT r.*,
                GROUP_CONCAT(c.category_id) AS category_ids,
                GROUP_CONCAT(c.category_name) AS category_names,
                GROUP_CONCAT(c.color_hex) AS category_colors,
                GROUP_CONCAT(c.image_url) AS category_images
            FROM recipes r
            LEFT JOIN recipe_category rc ON rc.recipe_id = r.recipe_id
            LEFT JOIN categories c ON c.category_id = rc.category_id
            WHERE r.recipe_id = ?
            GROUP BY r.recipe_id
        ");
        $stmt->bind_param("i", $recipeId);
        $stmt->execute();
        $result = $stmt->get_result();
        $recipe = $result->fetch_assoc();
        if (!$recipe) return null;

        $recipe['categories'] = self::parseCategories($recipe);
        unset($recipe['category_ids'], $recipe['category_names'], $recipe['category_colors'], $recipe['category_images']);

        // Ingredientes
        $stmt2 = $db->prepare("
            SELECT ri.quantity, ri.custom_name, i.ingredient_name, i.image_url
            FROM recipe_ingredients ri
            LEFT JOIN ingredients i ON ri.ingredient_id = i.ingredient_id
            WHERE ri.recipe_id = ?
        ");
        $stmt2->bind_param("i", $recipeId);
        $stmt2->execute();
        $result2 = $stmt2->get_result();

        $ingredients = [];
        while ($row = $result2->fetch_assoc()) {
            $name = $row['custom_name'] ?: ($row['ingredient_name'] ?? 'Ingrediente');
            $image = !empty($row['image_url']) ? BASE_URL . UPLOADS_FOLDER . INGREDIENTS_FOLDER . $row['image_url'] : BASE_URL . DEFAULT_IMAGE;
            $ingredients[] = ['ingredient_name' => $name, 'ingredient_quantity' => $row['quantity'], 'ingredient_image' => $image];
        }
        $recipe['ingredients'] = $ingredients;
        return $recipe;
    }

    /** Pega receitas populares paginadas */
    public static function getPopularWithPagination($page = 1, $limit = 6): array
    {
        $offset = ($page - 1) * $limit;
        $db = Database::connect();
        $query = "
            SELECT r.*, 
                GROUP_CONCAT(DISTINCT c.category_id) AS category_ids,
                GROUP_CONCAT(DISTINCT c.category_name) AS category_names,
                GROUP_CONCAT(DISTINCT c.color_hex) AS category_colors,
                GROUP_CONCAT(DISTINCT c.image_url) AS category_images
            FROM recipes r
            LEFT JOIN recipe_category rc ON rc.recipe_id = r.recipe_id
            LEFT JOIN categories c ON c.category_id = rc.category_id
            GROUP BY r.recipe_id
            ORDER BY r.views_count DESC
            LIMIT ? OFFSET ?
        ";
        $stmt = $db->prepare($query);
        $stmt->bind_param("ii", $limit, $offset);
        $stmt->execute();
        $result = $stmt->get_result();

        $recipes = [];
        while ($row = $result->fetch_assoc()) {
            $row['categories'] = self::parseCategories($row);
            unset($row['category_ids'], $row['category_names'], $row['category_colors'], $row['category_images']);
            $recipes[] = $row;
        }
        return $recipes;
    }

    /** Helpers privados */

    private static function parseCategories($recipe)
    {
        $ids = explode(',', $recipe['category_ids'] ?? '');
        $names = explode(',', $recipe['category_names'] ?? '');
        $colors = explode(',', $recipe['category_colors'] ?? '');
        $images = explode(',', $recipe['category_images'] ?? '');

        $categories = [];
        for ($i = 0; $i < count($ids); $i++) {
            $categories[] = [
                'category_id' => (int)($ids[$i] ?? 0),
                'category_name' => $names[$i] ?? '',
                'category_color' => $colors[$i] ?? '#EEEEEE',
                'image_url' => !empty($images[$i]) ? BASE_URL . UPLOADS_FOLDER . CATEGORIES_FOLDER . $images[$i] : null
            ];
        }
        return $categories;
    }

    // Retorna todas as receitas de um autor (por user_id)
    public static function getAllByAuthor($user_id)
    {
        $db = Database::connect();
        $stmt = $db->prepare("SELECT * FROM recipes WHERE author_id = ?");
        $stmt->bind_param("i", $user_id);
        $stmt->execute();
        $result = $stmt->get_result();
        $recipes = [];
        while ($row = $result->fetch_assoc()) {
            $recipes[] = $row;
        }
        return $recipes;
    }

    // Retorna só os IDs das receitas de um autor (por user_id)
    public static function getIdsByAuthor($user_id)
    {
        $db = Database::connect();
        $stmt = $db->prepare("SELECT recipe_id FROM recipes WHERE author_id = ?");
        $stmt->bind_param("i", $user_id);
        $stmt->execute();
        $result = $stmt->get_result();
        $ids = [];
        while ($row = $result->fetch_assoc()) {
            $ids[] = $row['recipe_id'];
        }
        return $ids;
    }

    // Soma todas as views das receitas passadas no array $recipeIds
    public static function getTotalViewsByIds($recipeIds)
    {
        if (empty($recipeIds)) return 0;
        $db = Database::connect();
        $placeholders = implode(',', array_fill(0, count($recipeIds), '?'));
        $types = str_repeat('i', count($recipeIds));
        $sql = "SELECT SUM(views_count) as total_views FROM recipes WHERE recipe_id IN ($placeholders)";
        $stmt = $db->prepare($sql);
        $stmt->bind_param($types, ...$recipeIds);
        $stmt->execute();
        $res = $stmt->get_result()->fetch_assoc();
        return intval($res['total_views'] ?? 0);
    }
}
