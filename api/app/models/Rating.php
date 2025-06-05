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
    public static function getAverageRatingByAuthor($user_id)
    {
        $db = Database::connect();
        $stmt = $db->prepare("SELECT AVG(average_rating) as avg_rating FROM recipes WHERE author_id = ? AND average_rating > 0");
        $stmt->bind_param("i", $user_id);
        $stmt->execute();
        $result = $stmt->get_result()->fetch_assoc();
        return round($result['avg_rating'] ?? 0, 2);
    }
}
