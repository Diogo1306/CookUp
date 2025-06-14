<?php

require_once __DIR__ . '/../core/Database.php';

class Recipe
{
    public static function saveOrUpdate($data)
    {
        $db = Database::connect();

        $image = $data['image'] ?? 'default.png';
        $author_id = $data['author_id'] ?? 1;

        if (!empty($data['recipe_id'])) {
            // UPDATE
            $stmt = $db->prepare("UPDATE recipes SET title=?, description=?, instructions=?, difficulty=?, preparation_time=?, servings=?, image=?, updated_at=NOW() WHERE recipe_id=?");
            $stmt->bind_param(
                "sssssssi",
                $data['title'],
                $data['description'],
                $data['instructions'],
                $data['difficulty'],
                $data['preparation_time'],
                $data['servings'],
                $image,
                $data['recipe_id']
            );
            $stmt->execute();
            $stmt->close();
            return $data['recipe_id'];
        } else {
            // INSERT
            $stmt = $db->prepare("INSERT INTO recipes (author_id, title, description, instructions, difficulty, preparation_time, servings, image, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())");
            $stmt->bind_param(
                "isssssis",
                $author_id,
                $data['title'],
                $data['description'],
                $data['instructions'],
                $data['difficulty'],
                $data['preparation_time'],
                $data['servings'],
                $image
            );
            $stmt->execute();
            $id = $db->insert_id;
            $stmt->close();
            return $id;
        }
    }

    public static function updateCategories($recipeId, $categories)
    {
        $db = Database::connect();
        $db->query("DELETE FROM recipe_category WHERE recipe_id = $recipeId");
        $stmt = $db->prepare("INSERT INTO recipe_category (recipe_id, category_id) VALUES (?, ?)");
        foreach ($categories as $catId) {
            $stmt->bind_param("ii", $recipeId, $catId);
            $stmt->execute();
        }
        $stmt->close();
    }

    public static function updateIngredients($recipeId, $ingredients)
    {

        $db = Database::connect();
        $db->query("DELETE FROM recipe_ingredients WHERE recipe_id = $recipeId");
        $stmt = $db->prepare("INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity) VALUES (?, ?, ?)");
        foreach ($ingredients as $ing) {
            $name = $ing['ingredient_name'];
            $quantity = $ing['quantity'] ?? $ing['ingredient_quantity'] ?? '';
            $ingredientId = Ingredient::getOrCreate($name);
            $stmt->bind_param("iis", $recipeId, $ingredientId, $quantity);
            $stmt->execute();
        }
        $stmt->close();
    }

    public static function saveGalleryImages($recipeId, $galleryNames)
    {
        $db = Database::connect();
        $db->query("DELETE FROM recipe_gallery WHERE recipe_id = $recipeId");

        foreach ($galleryNames as $index => $imgName) {
            $stmt = $db->prepare("INSERT INTO recipe_gallery (recipe_id, image_url, ordering) VALUES (?, ?, ?)");
            $stmt->bind_param("isi", $recipeId, $imgName, $index);
            $stmt->execute();
            $stmt->close();
        }
    }


    public static function delete($recipeId): bool
    {
        $db = Database::connect();

        $stmt = $db->prepare("SELECT image_url FROM recipe_gallery WHERE recipe_id = ?");
        $stmt->bind_param("i", $recipeId);
        $stmt->execute();
        $result = $stmt->get_result();
        while ($row = $result->fetch_assoc()) {
            $file = __DIR__ . '/../../uploads/recipes/' . $row['image_url'];
            if (file_exists($file)) {
                unlink($file);
            }
        }
        $stmt->close();

        $db->query("DELETE FROM recipe_gallery WHERE recipe_id = $recipeId");

        $stmt2 = $db->prepare("SELECT image FROM recipes WHERE recipe_id = ?");
        $stmt2->bind_param("i", $recipeId);
        $stmt2->execute();
        $result2 = $stmt2->get_result();
        if ($row2 = $result2->fetch_assoc()) {
            $file = __DIR__ . '/../../uploads/recipes/' . $row2['image'];
            if (file_exists($file) && $row2['image'] !== 'default.png') {
                unlink($file);
            }
        }
        $stmt2->close();

        $stmt3 = $db->prepare("DELETE FROM recipes WHERE recipe_id = ?");
        $stmt3->bind_param("i", $recipeId);
        return $stmt3->execute();
    }

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
            $row['image'] = self::buildImageUrl($row['image']);
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
            $row['image'] = self::buildImageUrl($row['image']);
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
            $row['image'] = self::buildImageUrl($row['image']);
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
            // Aqui ajusta a URL:
            $row['image'] = self::buildImageUrl($row['image']);
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
                u.username AS author_name,   -- <--- aqui!
                GROUP_CONCAT(c.category_id) AS category_ids,
                GROUP_CONCAT(c.category_name) AS category_names,
                GROUP_CONCAT(c.color_hex) AS category_colors,
                GROUP_CONCAT(c.image_url) AS category_images
            FROM recipes r
            LEFT JOIN users u ON r.author_id = u.user_id
            LEFT JOIN recipe_category rc ON rc.recipe_id = r.recipe_id
            LEFT JOIN categories c ON c.category_id = rc.category_id
            WHERE r.recipe_id = ?
            GROUP BY r.recipe_id
        ");

        $stmt->bind_param("i", $recipeId);
        $stmt->execute();
        $result = $stmt->get_result();
        $recipe = $result->fetch_assoc();
        $stmt->close();
        if (!$recipe) return null;

        if (!empty($recipe['image'])) {
            $recipe['image'] = BASE_URL . UPLOADS_FOLDER . RECIPES_FOLDER . $recipe['image'];
        } else {
            $recipe['image'] = BASE_URL . DEFAULT_IMAGE;
        }

        $recipe['categories'] = self::parseCategories($recipe);
        unset($recipe['category_ids'], $recipe['category_names'], $recipe['category_colors'], $recipe['category_images']);

        $stmt2 = $db->prepare("
            SELECT ri.quantity, i.ingredient_name, i.image_url
            FROM recipe_ingredients ri
            LEFT JOIN ingredients i ON ri.ingredient_id = i.ingredient_id
            WHERE ri.recipe_id = ?
    ");
        $stmt2->bind_param("i", $recipeId);
        $stmt2->execute();
        $result2 = $stmt2->get_result();

        $ingredients = [];
        while ($row = $result2->fetch_assoc()) {
            $name = $row['ingredient_name'] ?? 'Ingrediente';
            $image = !empty($row['image_url'])
                ? BASE_URL . UPLOADS_FOLDER . INGREDIENTS_FOLDER . $row['image_url']
                : BASE_URL . DEFAULT_IMAGE;
            $ingredients[] = [
                'ingredient_name' => $name,
                'ingredient_quantity' => $row['quantity'],
                'ingredient_image' => $image
            ];
        }
        $stmt2->close();
        $recipe['ingredients'] = $ingredients;

        $stmt3 = $db->prepare("
        SELECT image_url
        FROM recipe_gallery
        WHERE recipe_id = ?
        ORDER BY ordering ASC
    ");
        $stmt3->bind_param("i", $recipeId);
        $stmt3->execute();
        $result3 = $stmt3->get_result();

        $gallery = [];
        while ($row = $result3->fetch_assoc()) {
            $gallery[] = BASE_URL . UPLOADS_FOLDER . RECIPES_FOLDER . $row['image_url'];
        }
        $stmt3->close();
        $recipe['gallery'] = $gallery;

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
            $row['image'] = self::buildImageUrl($row['image']);
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
            $row['image'] = self::buildImageUrl($row['image']);
            $row['finished_count'] = Tracking::countUniqueUsersFinishedRecipe($row['recipe_id']);
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

    public static function getGalleryImages($recipeId)
    {
        $db = Database::connect();
        $images = [];
        $stmt = $db->prepare("SELECT image_url FROM recipe_gallery WHERE recipe_id = ?");
        $stmt->bind_param("i", $recipeId);
        $stmt->execute();
        $result = $stmt->get_result();
        while ($row = $result->fetch_assoc()) {
            $images[] = $row['image_url'];
        }
        $stmt->close();
        return $images;
    }

    private static function buildImageUrl($image)
    {
        if (empty($image)) return BASE_URL . DEFAULT_IMAGE;
        if (substr($image, 0, 4) === 'http') return $image;
        return BASE_URL . UPLOADS_FOLDER . RECIPES_FOLDER . $image;
    }
}
