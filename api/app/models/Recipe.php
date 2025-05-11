<?php

require_once __DIR__ . '/../core/Database.php';
require_once __DIR__ . '/Rating.php';

class Recipe
{

    public static function createOrUpdateRecipe($data)
    {
        $conn = Database::connect();

        $stmt = $conn->prepare("
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

        $recipeId = $data['recipe_id'];
        if (!$recipeId) {
            $recipeId = $conn->insert_id;
        }

        $deleteStmt = $conn->prepare("DELETE FROM recipe_category WHERE recipe_id = ?");
        $deleteStmt->bind_param("i", $recipeId);
        $deleteStmt->execute();
        $deleteStmt->close();

        if (!empty($data['categories']) && is_array($data['categories'])) {
            $insertStmt = $conn->prepare("INSERT INTO recipe_category (recipe_id, category_id) VALUES (?, ?)");
            foreach ($data['categories'] as $categoryId) {
                $insertStmt->bind_param("ii", $recipeId, $categoryId);
                $insertStmt->execute();
            }
            $insertStmt->close();
        }

        return true;
    }

    public static function deleteRecipe($recipeId)
    {
        $conn = Database::connect();
        $conn->begin_transaction();

        try {
            $stmt = $conn->prepare("DELETE FROM recipes WHERE recipe_id = ?");
            $stmt->bind_param("i", $recipeId);
            $stmt->execute();
            $stmt->close();

            $conn->commit();
            return true;
        } catch (Exception $e) {
            $conn->rollback();
            return false;
        }
    }

    private static function parseCategories($recipe)
    {
        $ids = explode(',', $recipe['category_ids'] ?? '');
        $names = explode(',', $recipe['category_names'] ?? '');
        $colors = explode(',', $recipe['category_colors'] ?? []);

        $categories = [];
        for ($i = 0; $i < count($ids); $i++) {
            $categories[] = [
                'category_id' => (int)$ids[$i],
                'category_name' => $names[$i] ?? '',
                'category_color' => $colors[$i] ?? '#EEEEEE'
            ];
        }

        return $categories;
    }

    public static function getAllRecipes()
    {

        $db = Database::connect();

        $query = "
            SELECT 
                r.*, 
                GROUP_CONCAT(c.category_id) AS category_ids,
                GROUP_CONCAT(c.category_name) AS category_names
            FROM recipes r
            LEFT JOIN recipe_category rc ON rc.recipe_id = r.recipe_id
            LEFT JOIN categories c ON c.category_id = rc.category_id
            GROUP BY r.recipe_id
        ";

        $stmt = $db->prepare($query);
        $stmt->execute();

        $recipes = [];

        while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
            $row['categories'] = self::parseCategories($row);
            unset($row['category_ids']);
            unset($row['category_names']);
            $recipes[] = $row;
        }

        return $recipes;
    }

    public static function getRecipeDetail($recipeId)
    {
        $db = Database::connect();

        $stmt = $db->prepare("
            SELECT r.*, 
                GROUP_CONCAT(c.category_id) AS category_ids,
                GROUP_CONCAT(c.category_name) AS category_names,
                GROUP_CONCAT(c.color_hex) AS category_colors
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
        unset($recipe['category_ids']);
        unset($recipe['category_names']);

        $recipe['average_rating'] = Rating::getAverageRating($recipeId);

        $stmt2 = $db->prepare("
            SELECT 
                ri.quantity,
                ri.custom_name,
                i.ingredient_name,
                i.image_url
            FROM recipe_ingredients ri
            LEFT JOIN ingredients i ON ri.ingredient_id = i.ingredient_id
            WHERE ri.recipe_id = ?
        ");
        $stmt2->bind_param("i", $recipeId);
        $stmt2->execute();
        $result2 = $stmt2->get_result();

        $ingredients = [];
        while ($row = $result2->fetch_assoc()) {
            $name = $row['custom_name'] ?? $row['ingredient_name'] ?? 'Ingredient';
            $image = (!empty($row['image_url']))
                ? BASE_URL . UPLOADS_FOLDER . INGREDIENTS_FOLDER . $row['image_url']
                : BASE_URL . DEFAULT_IMAGE;
            $quantity = $row['quantity'] ?? '';
            $ingredients[] = ['name' => $name, 'quantity' => $quantity, 'image' => $image];
        }

        $recipe['ingredients'] = $ingredients;
        return $recipe;
    }

    public static function getPopularRecipes()
    {
        $db = Database::connect();
        $stmt = $db->prepare("
            SELECT 
                r.*, 
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
            $row['categories'] = self::parseCategories($row);
            unset($row['category_ids']);
            unset($row['category_names']);
            $row['average_rating'] = Rating::getAverageRating($row['recipe_id']);
            $recipes[] = $row;
        }

        return $recipes;
    }

    public static function getRecommendedRecipes()
    {
        $db = Database::connect();
        $stmt = $db->prepare("
            SELECT 
                r.*, 
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
            $row['categories'] = self::parseCategories($row);
            unset($row['category_ids']);
            unset($row['category_names']);
            $row['average_rating'] = Rating::getAverageRating($row['recipe_id']);
            $recipes[] = $row;
        }

        return $recipes;
    }

    public static function getMostFavoritedRecipes()
    {
        $db = Database::connect();
        $stmt = $db->prepare("
            SELECT 
                r.*, 
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
            $row['categories'] = self::parseCategories($row);
            unset($row['category_ids']);
            unset($row['category_names']);
            $row['average_rating'] = Rating::getAverageRating($row['recipe_id']);
            $recipes[] = $row;
        }

        return $recipes;
    }

    public static function getRecipesByMealType($categoryId)
    {
        $db = Database::connect();
        $stmt = $db->prepare("
            SELECT 
                r.*, 
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
            $row['categories'] = self::parseCategories($row);
            unset($row['category_ids']);
            unset($row['category_names']);
            $row['average_rating'] = Rating::getAverageRating($row['recipe_id']);
            $recipes[] = $row;
        }

        return $recipes;
    }
}
