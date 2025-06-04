<?php

require_once __DIR__ . '/../core/Database.php';

class Rating
{
    /**
     * Cria ou atualiza o rating de um usuário para uma receita.
     */
    public static function submit($user_id, $recipe_id, $rating): bool
    {
        $db = Database::connect();

        $stmt = $db->prepare("
            INSERT INTO ratings (user_id, recipe_id, rating)
            VALUES (?, ?, ?)
            ON DUPLICATE KEY UPDATE rating = VALUES(rating), created_at = NOW()
        ");
        $stmt->bind_param("iii", $user_id, $recipe_id, $rating);
        return $stmt->execute();
    }

    /**
     * Busca média dos ratings de uma receita (apenas se precisar consultar "por fora").
     */
    public static function getAverage($recipe_id): float
    {
        $db = Database::connect();
        $stmt = $db->prepare("SELECT ROUND(AVG(rating), 1) as average FROM ratings WHERE recipe_id = ?");
        $stmt->bind_param("i", $recipe_id);
        $stmt->execute();
        $result = $stmt->get_result()->fetch_assoc();
        return floatval($result['average'] ?? 0);
    }

    // Retorna média das ratings de várias receitas
    public static function getAverageByRecipeIds($recipeIds)
    {
        if (empty($recipeIds)) return 0;
        $db = Database::connect();
        $placeholders = implode(',', array_fill(0, count($recipeIds), '?'));
        $types = str_repeat('i', count($recipeIds));
        $sql = "SELECT AVG(rating) as avg_rating FROM ratings WHERE recipe_id IN ($placeholders)";
        $stmt = $db->prepare($sql);
        $stmt->bind_param($types, ...$recipeIds);
        $stmt->execute();
        $res = $stmt->get_result()->fetch_assoc();
        return round($res['avg_rating'] ?? 0, 2);
    }
}
