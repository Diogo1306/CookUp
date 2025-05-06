<?php

require_once __DIR__ . '/../models/Comment.php';
require_once __DIR__ . '/../core/Response.php';

class CommentController
{
    public static function submitComment()
    {
        $data = json_decode(file_get_contents("php://input"), true);

        if (!isset($data['user_id'], $data['recipe_id'], $data['comment'])) {
            return Response::json(['success' => false, 'message' => 'Dados incompletos']);
        }

        $success = Comment::submitComment($data['user_id'], $data['recipe_id'], $data['comment']);

        if ($success) {
            Response::json(['success' => true, 'message' => 'Comentário enviado']);
        } else {
            Response::json(['success' => false, 'message' => 'Erro ao gravar comentário']);
        }
    }

    public static function getComments()
    {
        if (!isset($_GET['recipe_id'])) {
            Response::json(['success' => false, 'message' => 'ID da receita em falta.']);
            return;
        }

        $recipe_id = intval($_GET['recipe_id']);
        $comments = Comment::getCommentsByRecipe($recipe_id);

        Response::json([
            'success' => true,
            'data' => $comments
        ]);
    }
}
