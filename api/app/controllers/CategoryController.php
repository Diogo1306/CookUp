<?php

require_once __DIR__ . '/../models/Category.php';
require_once __DIR__ . '/../core/Response.php';

class CategoryController
{
    public function getAll()
    {
        $categories = Category::getAll();
        Response::json(
            !empty($categories)
                ? ["success" => true, "data" => $categories]
                : ["success" => false, "message" => "Nenhuma categoria encontrada."]
        );
    }

    public function getPopular()
    {
        $categories = Category::getPopular();
        Response::json(
            !empty($categories)
                ? ["success" => true, "data" => $categories]
                : ["success" => false, "message" => "Nenhuma categoria popular encontrada."]
        );
    }

    public function getUserCategories()
    {
        if (!isset($_GET['user_id'])) {
            Response::json(['success' => false, 'message' => 'Parâmetro user_id em falta.']);
            return;
        }

        $userId = intval($_GET['user_id']);
        $categories = Category::getUserTop($userId);

        Response::json(
            !empty($categories)
                ? ["success" => true, "data" => $categories]
                : ["success" => false, "message" => "Nenhuma categoria favorita para este utilizador."]
        );
    }
}
