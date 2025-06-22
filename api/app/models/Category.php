<?php

require_once __DIR__ . '/../core/Database.php';

/**
 * Helper para montar URLs de imagem de categoria
 */

function buildCategoryImageUrl($image_url)
{
    return !empty($image_url)
        ? BASE_URL . UPLOADS_FOLDER . CATEGORIES_FOLDER . $image_url
        : BASE_URL . DEFAULT_IMAGE;
}

class Category
{
    /**
     * Retorna todas as categorias com URL de imagem já montada
     */

    public static function getAll(): array
    {
        $conn = Database::connect();
        $stmt = $conn->prepare("SELECT * FROM categories");
        $stmt->execute();
        $result = $stmt->get_result();

        $categories = [];
        while ($row = $result->fetch_assoc()) {
            $row['category_image_url'] = buildCategoryImageUrl($row['image_url'] ?? null);
            unset($row['image_url']);
            $categories[] = $row;
        }
        return $categories;
    }

    /**
     * Retorna as categorias mais populares (mais receitas relacionadas)
     */

    public static function getPopular($limit = 10): array
    {
        $conn = Database::connect();
        $stmt = $conn->prepare("SELECT c.*, COUNT(rc.recipe_id) AS total
            FROM categories c
            LEFT JOIN recipe_category rc ON rc.category_id = c.category_id
            GROUP BY c.category_id
            ORDER BY total DESC
            LIMIT ?");
        $stmt->bind_param("i", $limit);
        $stmt->execute();
        $result = $stmt->get_result();

        $categories = [];
        while ($row = $result->fetch_assoc()) {
            $row['category_image_url'] = buildCategoryImageUrl($row['image_url'] ?? null);
            unset($row['image_url']);
            $categories[] = $row;
        }
        return $categories;
    }

    /**
     * Retorna categorias "favoritas" de um usuário, preenchendo até o limite com as mais populares se faltar
     */

    public static function getUserTop($userId, $limit = 10): array
    {
        $conn = Database::connect();
        $stmt = $conn->prepare("SELECT c.category_id, c.category_name, c.image_url,
                   (ucs.views_count + ucs.favorites_count * 2 + ucs.finished_count * 3) AS score
            FROM user_category_stats ucs
            JOIN categories c ON ucs.category_id = c.category_id
            WHERE ucs.user_id = ?
            ORDER BY score DESC
            LIMIT ?");
        $stmt->bind_param("ii", $userId, $limit);
        $stmt->execute();
        $result = $stmt->get_result();

        $categories = [];
        while ($row = $result->fetch_assoc()) {
            $row['category_image_url'] = buildCategoryImageUrl($row['image_url'] ?? null);
            unset($row['image_url']);
            $categories[] = $row;
        }

        if (count($categories) < $limit) {
            $fetchedIds = array_column($categories, 'category_id');
            $whereNotIn = '';
            if (!empty($fetchedIds)) {
                $placeholders = implode(',', array_fill(0, count($fetchedIds), '?'));
                $whereNotIn = "WHERE c.category_id NOT IN ($placeholders) ";
            }
            $query = "SELECT c.category_id, c.category_name, c.image_url,
                       COUNT(rc.recipe_id) AS total
                FROM categories c
                LEFT JOIN recipe_category rc ON rc.category_id = c.category_id
                $whereNotIn
                GROUP BY c.category_id
                ORDER BY total DESC
                LIMIT ?";
            $params = [];
            $types = '';
            if (!empty($fetchedIds)) {
                $params = $fetchedIds;
                $types = str_repeat('i', count($fetchedIds));
            }
            $params[] = $limit - count($categories);
            $types .= 'i';
            $stmt2 = $conn->prepare($query);
            $stmt2->bind_param($types, ...$params);
            $stmt2->execute();
            $result2 = $stmt2->get_result();

            while ($row = $result2->fetch_assoc()) {
                $row['category_image_url'] = buildCategoryImageUrl($row['image_url'] ?? null);
                unset($row['image_url']);
                $categories[] = $row;
            }
        }
        return $categories;
    }

    /**
     * Retorna as categorias de uma receita específica
     */

    public static function getByRecipe($recipeId): array
    {
        $conn = Database::connect();
        $stmt = $conn->prepare("
            SELECT c.category_id, c.category_name
            FROM recipe_category rc
            JOIN categories c ON rc.category_id = c.category_id
            WHERE rc.recipe_id = ?
        ");
        $stmt->bind_param("i", $recipeId);
        $stmt->execute();
        $result = $stmt->get_result();
        return $result->fetch_all(MYSQLI_ASSOC);
    }

    /**
     * Retorna o nome da categoria (ou 'Sem Nome')
     */

    public static function getName($categoryId): string
    {
        $conn = Database::connect();
        $stmt = $conn->prepare("SELECT category_name FROM categories WHERE category_id = ?");
        $stmt->bind_param("i", $categoryId);
        $stmt->execute();
        $result = $stmt->get_result();

        if ($row = $result->fetch_assoc()) {
            return $row['category_name'];
        }
        return 'Sem Nome';
    }

    public static function create($category_name, $image_url, $color_hex)
    {
        $conn = Database::connect();
        $stmt = $conn->prepare("INSERT INTO categories (category_name, image_url, color_hex) VALUES (?, ?, ?)");
        $stmt->bind_param("sss", $category_name, $image_url, $color_hex);
        $ok = $stmt->execute();
        $stmt->close();
        return $ok;
    }


    public static function update($category_id, $name, $image_url, $color_hex = null)
    {
        $conn = Database::connect();
        $stmt = $conn->prepare("UPDATE categories SET category_name = ?, image_url = ?, color_hex = ? WHERE category_id = ?");
        $stmt->bind_param("sssi", $name, $image_url, $color_hex, $category_id);
        $ok = $stmt->execute();
        $stmt->close();
        return $ok;
    }

    public static function delete($category_id)
    {
        $conn = Database::connect();
        $stmt = $conn->prepare("DELETE FROM categories WHERE category_id = ?");
        $stmt->bind_param("i", $category_id);
        $ok = $stmt->execute();
        $stmt->close();
        return $ok;
    }
}
