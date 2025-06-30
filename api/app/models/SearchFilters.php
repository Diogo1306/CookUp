<?php

require_once __DIR__ . '/../core/Database.php';

class SearchFilters
{
    public static function filterRecipes(array $params): array
    {
        $db = Database::connect();
        $where = [];
        $bindings = [];
        $types = '';
        $extraSelect = '';
        $isRecommended = (!empty($params['filter']) && $params['filter'] === 'recommended' && !empty($params['userId']));

        if (!empty($params['query'])) {
            $searchTerm = '%' . strtolower(trim($params['query'])) . '%';
            $where[] = "(
        LOWER(r.title) LIKE ?
        OR EXISTS (
            SELECT 1 FROM recipe_category rc
            JOIN categories c ON rc.category_id = c.category_id
            WHERE rc.recipe_id = r.recipe_id AND LOWER(c.category_name) LIKE ?
        )
        OR EXISTS (
            SELECT 1 FROM recipe_ingredients ri
            JOIN ingredients i ON ri.ingredient_id = i.ingredient_id
            WHERE ri.recipe_id = r.recipe_id AND LOWER(i.ingredient_name) LIKE ?
        )
    )";
            $bindings[] = $searchTerm;
            $bindings[] = $searchTerm;
            $bindings[] = $searchTerm;
            $types .= 'sss';
        }

        if (!empty($params['difficulty'])) {
            $where[] = "difficulty = ?";
            $bindings[] = $params['difficulty'];
            $types .= 's';
        }

        if (!empty($params['min_time'])) {
            $where[] = "preparation_time >= ?";
            $bindings[] = (int)$params['min_time'];
            $types .= 'i';
        }

        if (!empty($params['max_time'])) {
            $where[] = "preparation_time <= ?";
            $bindings[] = (int)$params['max_time'];
            $types .= 'i';
        }

        if (!empty($params['max_ingredients'])) {
            $where[] = "(SELECT COUNT(*) FROM recipe_ingredients ri WHERE ri.recipe_id = r.recipe_id) <= ?";
            $bindings[] = (int)$params['max_ingredients'];
            $types .= 'i';
        }

        if (!empty($params['excludeIds']) && is_array($params['excludeIds'])) {
            $placeholders = implode(',', array_fill(0, count($params['excludeIds']), '?'));
            $where[] = "recipe_id NOT IN ($placeholders)";
            $bindings = array_merge($bindings, $params['excludeIds']);
            $types .= str_repeat('i', count($params['excludeIds']));
        }

        if (!empty($params['filter']) && $params['filter'] === 'week') {
            $where[] = "created_at >= DATE_SUB(NOW(), INTERVAL 7 DAY)";
        }

        // --- SQL Base ---
        if ($isRecommended) {
            $userId = (int)$params['userId'];
            $types = 'i' . $types;
            array_unshift($bindings, $userId);

            $sql = "SELECT * FROM (
                        SELECT r.*, 
                            (COALESCE(ucs.views_count,0) * 1.5 +
                             COALESCE(ucs.favorites_count,0) * 3 +
                             COALESCE(ucs.finished_count,0) * 5) AS user_score
                        FROM recipes r
                        JOIN recipe_category rc ON r.recipe_id = rc.recipe_id
                        JOIN user_category_stats ucs ON ucs.category_id = rc.category_id AND ucs.user_id = ?
                    ) r";
            if (!empty($where)) {
                $sql .= " WHERE " . implode(" AND ", $where);
            }
            $sql .= " GROUP BY recipe_id ORDER BY user_score DESC, (favorites_count * 2 + views_count) DESC";
        } else {
            $sql = "SELECT * FROM recipes r";
            if (!empty($where)) {
                $sql .= " WHERE " . implode(" AND ", $where);
            }

            switch ($params['filter'] ?? '') {
                case 'popular':
                    $sql .= " ORDER BY (favorites_count * 2 + views_count) DESC";
                    break;
                case 'recent':
                    $sql .= " ORDER BY created_at DESC";
                    break;
                case 'week':
                    $sql .= " ORDER BY (favorites_count * 2 + views_count) DESC";
                    break;
                default:
                    $sql .= " ORDER BY recipe_id DESC";
            }
        }

        // --- Paginação (desativada por agora) ---
        // if (isset($params['limit']) && is_numeric($params['limit'])) {
        //     $limit = (int)$params['limit'];
        //     $offset = isset($params['offset']) && is_numeric($params['offset']) ? (int)$params['offset'] : 0;
        //     $sql .= " LIMIT $limit OFFSET $offset";
        // }

        $stmt = $db->prepare($sql);
        if (!empty($bindings)) {
            $stmt->bind_param($types, ...$bindings);
        }
        $stmt->execute();
        $result = $stmt->get_result();

        $recipes = [];
        while ($row = $result->fetch_assoc()) {
            $img = $row['image'] ?? '';
            $row['image'] = !empty($img)
                ? BASE_URL . UPLOADS_FOLDER . RECIPES_FOLDER . $img
                : BASE_URL . DEFAULT_IMAGE;
            $recipes[] = $row;
        }

        return $recipes;
    }
}
