<?php

require_once __DIR__ . '/../models/Comment.php';
require_once __DIR__ . '/../core/Response.php';

class CommentController
{
    public function submit()
    {
        $data = json_decode(file_get_contents("php://input"), true);

        if (!isset($data['user_id'], $data['recipe_id'], $data['comment'])) {
            return Response::json(['success' => false, 'message' => 'Dados incompletos.']);
        }

        $success = Comment::submit($data['user_id'], $data['recipe_id'], $data['comment']);

        Response::json(
            $success
                ? ['success' => true, 'message' => 'Comentário enviado com sucesso.']
                : ['success' => false, 'message' => 'Erro ao gravar comentário.']
        );
    }

    public function getByRecipe()
    {
        if (!isset($_GET['recipe_id'])) {
            Response::json(['success' => false, 'message' => 'ID da receita em falta.']);
            return;
        }

        $recipe_id = intval($_GET['recipe_id']);
        $comments = Comment::getByRecipe($recipe_id);

        Response::json([
            'success' => true,
            'data' => $comments
        ]);
    }
}
