<?php

require_once __DIR__ . '/../core/Database.php';

class Comment
{
    /**
     * Insere um novo comentário para uma receita.
     */

    public static function submit($user_id, $recipe_id, $comment): bool
    {
        $db = Database::connect();
        $stmt = $db->prepare("INSERT INTO comments (user_id, recipe_id, comment) VALUES (?, ?, ?)");
        $stmt->bind_param("iis", $user_id, $recipe_id, $comment);
        return $stmt->execute();
    }
    /**
     * Retorna os comentários de uma receita, mais dados do user e rating se houver.
     */

    public static function getByRecipe($recipe_id): array
    {
        $db = Database::connect();

        $stmt = $db->prepare("
            SELECT 
                c.comment, 
                c.created_at, 
                u.username AS name,
                u.profile_picture AS image,
                (
                    SELECT r.rating 
                    FROM ratings r 
                    WHERE r.user_id = c.user_id AND r.recipe_id = c.recipe_id 
                    LIMIT 1
                ) AS rating
            FROM comments c
            JOIN users u ON c.user_id = u.user_id
            WHERE c.recipe_id = ?
            ORDER BY c.created_at DESC
        ");

        $stmt->bind_param("i", $recipe_id);
        $stmt->execute();
        $result = $stmt->get_result();

        $comments = [];
        while ($row = $result->fetch_assoc()) {
            $comments[] = $row;
        }

        return $comments;
    }
}
