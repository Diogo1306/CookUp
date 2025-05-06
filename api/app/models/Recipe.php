<?php

require_once __DIR__ . '/../core/Database.php';
require_once __DIR__ . '/Rating.php';

class Recipe
{
    public static function getAllRecipes()
    {
        $conn = Database::connect();
        $stmt = $conn->prepare("
            SELECT r.*, c.category_name AS category_name, c.image_url
            FROM recipes r
            LEFT JOIN categories c ON r.category_id = c.category_id
        ");
        $stmt->execute();

        $result = $stmt->get_result();
        $recipes = [];

        while ($row = $result->fetch_assoc()) {
            if (!empty($row['image_url'])) {
                $row['category_image_url'] = BASE_URL . UPLOADS_FOLDER . CATEGORIES_FOLDER . $row['image_url'];
            } else {
                $row['category_image_url'] = BASE_URL . DEFAULT_IMAGE;
            }
            unset($row['image_url']);
            $row['average_rating'] = Rating::getAverageRating($row['recipe_id']);
            $recipes[] = $row;
        }

        $stmt->close();
        return $recipes;
    }

    public static function getRecipeDetail($recipeId)
    {
        $db = Database::connect();

        $stmt = $db->prepare("
            SELECT r.*, c.category_name AS category_name, c.image_url
            FROM recipes r
            LEFT JOIN categories c ON r.category_id = c.category_id
            WHERE r.recipe_id = ?
        ");
        $stmt->bind_param("i", $recipeId);
        $stmt->execute();
        $result = $stmt->get_result();
        $recipe = $result->fetch_assoc();

        if (!$recipe) return null;

        if (!empty($recipe['image_url'])) {
            $recipe['category_image_url'] = BASE_URL . UPLOADS_FOLDER . CATEGORIES_FOLDER . $recipe['image_url'];
        } else {
            $recipe['category_image_url'] = BASE_URL . DEFAULT_IMAGE;
        }
        unset($recipe['image_url']);

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

    public static function createOrUpdateRecipe($data)
    {
        $conn = Database::connect();

        $stmt = $conn->prepare("
            INSERT INTO recipes (
                author_id, category_id, title, description, instructions,
                preparation_time, servings, image, utensils, difficulty, created_at, updated_at
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())
            ON DUPLICATE KEY UPDATE 
                category_id = VALUES(category_id),
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
            $data['author_id'],
            $data['category_id'],
            $data['title'],
            $data['description'],
            $data['instructions'],
            $data['preparation_time'],
            $data['servings'],
            $data['image'],
            $data['difficulty']
        );

        $success = $stmt->execute();
        $stmt->close();

        return $success;
    }

    public static function getPopularRecipes()
    {
        $conn = Database::connect();
        $stmt = $conn->prepare("
            SELECT r.*, c.category_name AS category_name, c.image_url
            FROM recipes r
            LEFT JOIN categories c ON r.category_id = c.category_id
            ORDER BY (r.favorites_count * 2 + r.views_count) DESC
            LIMIT 10
        ");
        $stmt->execute();
        $result = $stmt->get_result();
        $recipes = [];

        while ($row = $result->fetch_assoc()) {
            if (!empty($row['image_url'])) {
                $row['category_image_url'] = BASE_URL . UPLOADS_FOLDER . CATEGORIES_FOLDER . $row['image_url'];
            } else {
                $row['category_image_url'] = BASE_URL . DEFAULT_IMAGE;
            }
            unset($row['image_url']);
            $row['average_rating'] = Rating::getAverageRating($row['recipe_id']);
            $recipes[] = $row;
        }

        return $recipes;
    }

    public static function getRecommendedRecipes()
    {
        $conn = Database::connect();
        $stmt = $conn->prepare("
            SELECT r.*, c.category_name
            FROM recipes r
            LEFT JOIN categories c ON r.category_id = c.category_id
            ORDER BY (r.favorites_count * 2 + r.views_count) DESC
            LIMIT 10
        ");
        $stmt->execute();
        $result = $stmt->get_result();

        $recipes = [];
        while ($row = $result->fetch_assoc()) {
            $row['average_rating'] = Rating::getAverageRating($row['recipe_id']);
            $recipes[] = $row;
        }

        return $recipes;
    }

    public static function getMostFavoritedRecipes()
    {
        $conn = Database::connect();
        $stmt = $conn->prepare("
            SELECT r.*, c.category_name AS category_name, c.image_url
            FROM recipes r
            LEFT JOIN categories c ON r.category_id = c.category_id
            ORDER BY r.favorites_count DESC
            LIMIT 10
        ");
        $stmt->execute();
        $result = $stmt->get_result();
        $recipes = [];

        while ($row = $result->fetch_assoc()) {
            if (!empty($row['image_url'])) {
                $row['category_image_url'] = BASE_URL . UPLOADS_FOLDER . CATEGORIES_FOLDER . $row['image_url'];
            } else {
                $row['category_image_url'] = BASE_URL . DEFAULT_IMAGE;
            }
            unset($row['image_url']);
            $row['average_rating'] = Rating::getAverageRating($row['recipe_id']);
            $recipes[] = $row;
        }

        return $recipes;
    }

    public static function getRecipesByMealType($categoryId)
    {
        $conn = Database::connect();
        $stmt = $conn->prepare("
            SELECT r.*, c.category_name AS category_name, c.image_url
            FROM recipes r
            LEFT JOIN categories c ON r.category_id = c.category_id
            WHERE r.category_id = ?
            ORDER BY (r.favorites_count * 2 + r.views_count) DESC
            LIMIT 10
        ");
        $stmt->bind_param("i", $categoryId);
        $stmt->execute();
        $result = $stmt->get_result();
        $recipes = [];

        while ($row = $result->fetch_assoc()) {
            if (!empty($row['image_url'])) {
                $row['category_image_url'] = BASE_URL . UPLOADS_FOLDER . CATEGORIES_FOLDER . $row['image_url'];
            } else {
                $row['category_image_url'] = BASE_URL . DEFAULT_IMAGE;
            }
            unset($row['image_url']);
            $row['average_rating'] = Rating::getAverageRating($row['recipe_id']);
            $recipes[] = $row;
        }

        return $recipes;
    }
}
