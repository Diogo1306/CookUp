<?php

require_once __DIR__ . '/../core/Database.php';

class Rating
{
    public static function submitRating($user_id, $recipe_id, $rating)
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

    public static function getAverageRating($recipe_id)
    {
        $db = Database::connect();

        $stmt = $db->prepare("SELECT ROUND(AVG(rating), 1) as average FROM ratings WHERE recipe_id = ?");
        $stmt->bind_param("i", $recipe_id);
        $stmt->execute();
        $result = $stmt->get_result()->fetch_assoc();

        return $result['average'] ?? 0;
    }
}
