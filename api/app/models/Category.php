<?php

require_once __DIR__ . '/../core/Database.php';

class Category
{
    public static function getAllCategories()
    {
        $conn = Database::connect();
        $stmt = $conn->prepare("SELECT * FROM categories");
        $stmt->execute();

        $result = $stmt->get_result();
        $categories = [];

        while ($row = $result->fetch_assoc()) {
            $row['category_image_url'] = !empty($row['image_url'])
                ? BASE_URL . UPLOADS_FOLDER . CATEGORIES_FOLDER . $row['image_url']
                : BASE_URL . DEFAULT_IMAGE;
            unset($row['image_url']);
            $categories[] = $row;
        }

        return $categories;
    }

    public static function getPopularCategories()
    {
        $conn = Database::connect();
        $stmt = $conn->prepare("
            SELECT c.*, COUNT(r.recipe_id) AS total
            FROM categories c
            LEFT JOIN recipes r ON r.category_id = c.category_id
            GROUP BY c.category_id
            ORDER BY total DESC
            LIMIT 10
        ");
        $stmt->execute();
        $result = $stmt->get_result();

        $categories = [];
        while ($row = $result->fetch_assoc()) {
            $row['category_image_url'] = !empty($row['image_url'])
                ? BASE_URL . UPLOADS_FOLDER . CATEGORIES_FOLDER . $row['image_url']
                : BASE_URL . DEFAULT_IMAGE;
            unset($row['image_url']);
            $categories[] = $row;
        }

        return $categories;
    }

    public static function getTopCategoriesByUser($userId, $limit = 10)
    {
        $conn = Database::connect();

        $stmt = $conn->prepare("
            SELECT c.category_id, c.category_name, c.image_url,
                   (ucs.views_count + ucs.favorites_count * 2 + ucs.finished_count * 3) AS score
            FROM user_category_stats ucs
            JOIN categories c ON ucs.category_id = c.category_id
            WHERE ucs.user_id = ?
            ORDER BY score DESC
            LIMIT ?
        ");
        $stmt->bind_param("ii", $userId, $limit);
        $stmt->execute();
        $result = $stmt->get_result();

        $categories = [];
        while ($row = $result->fetch_assoc()) {
            $row['category_image_url'] = !empty($row['image_url'])
                ? BASE_URL . UPLOADS_FOLDER . CATEGORIES_FOLDER . $row['image_url']
                : BASE_URL . DEFAULT_IMAGE;
            unset($row['image_url']);
            $categories[] = $row;
        }

        if (count($categories) < $limit) {
            $fetchedIds = array_column($categories, 'category_id');

            $query = "
                SELECT c.category_id, c.category_name, c.image_url,
                       COUNT(r.recipe_id) AS total
                FROM categories c
                LEFT JOIN recipes r ON r.category_id = c.category_id
                ";

            if (!empty($fetchedIds)) {
                $placeholders = implode(',', array_fill(0, count($fetchedIds), '?'));
                $query .= "WHERE c.category_id NOT IN ($placeholders) ";
            }

            $query .= "GROUP BY c.category_id ORDER BY total DESC LIMIT ?";

            $stmt2 = $conn->prepare($query);

            $types = str_repeat('i', count($fetchedIds)) . 'i';
            $params = array_merge($fetchedIds, [$limit - count($categories)]);
            $stmt2->bind_param($types, ...$params);

            $stmt2->execute();
            $result2 = $stmt2->get_result();

            while ($row = $result2->fetch_assoc()) {
                $row['category_image_url'] = !empty($row['image_url'])
                    ? BASE_URL . UPLOADS_FOLDER . CATEGORIES_FOLDER . $row['image_url']
                    : BASE_URL . DEFAULT_IMAGE;
                unset($row['image_url']);
                $categories[] = $row;
            }
        }

        return $categories;
    }

    public static function getCategoryName($categoryId)
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
}
