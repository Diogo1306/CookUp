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

    public function create()
    {
        if (empty($_POST['category_name'])) {
            Response::json(['success' => false, 'message' => 'Nome obrigatório!']);
            return;
        }

        $category_name = $_POST['category_name'];
        $color_hex = $_POST['color_hex'] ?? null;
        $image_url = null;

        if (isset($_FILES['image_url']) && $_FILES['image_url']['error'] === UPLOAD_ERR_OK) {
            $ext = pathinfo($_FILES['image_url']['name'], PATHINFO_EXTENSION);
            $image_url = uniqid('cat_', true) . '.' . $ext;
            $uploadPath = __DIR__ . '/../../uploads/categories/' . $image_url;
            if (!move_uploaded_file($_FILES['image_url']['tmp_name'], $uploadPath)) {
                Response::json(['success' => false, 'message' => 'Erro ao salvar imagem.']);
                return;
            }
        }

        $ok = Category::create($category_name, $image_url, $color_hex);

        Response::json($ok ? ['success' => true] : ['success' => false, 'message' => 'Erro ao criar categoria.']);
    }


    public function update()
    {
        $id = $_POST['category_id'] ?? null;
        $name = $_POST['category_name'] ?? null;
        $color_hex = $_POST['color_hex'] ?? null;

        if (!$id || !$name) {
            Response::json(['success' => false, 'message' => 'ID e nome obrigatórios.']);
            return;
        }

        $db = Database::connect();
        $stmt = $db->prepare("SELECT image_url FROM categories WHERE category_id = ?");
        $stmt->bind_param("i", $id);
        $stmt->execute();
        $result = $stmt->get_result();
        $old_image_url = '';
        if ($row = $result->fetch_assoc()) {
            $old_image_url = $row['image_url'];
        }
        $stmt->close();

        $new_image_url = $old_image_url;

        if (isset($_FILES['image_url']) && $_FILES['image_url']['error'] === UPLOAD_ERR_OK) {
            $ext = pathinfo($_FILES['image_url']['name'], PATHINFO_EXTENSION);
            $new_image_url = uniqid('cat_', true) . '.' . $ext;
            $uploadPath = __DIR__ . '/../../uploads/categories/' . $new_image_url;
            if (!move_uploaded_file($_FILES['image_url']['tmp_name'], $uploadPath)) {
                Response::json(['success' => false, 'message' => 'Erro ao salvar imagem.']);
                return;
            }
            if (!empty($old_image_url) && file_exists(__DIR__ . '/../../uploads/categories/' . $old_image_url)) {
                @unlink(__DIR__ . '/../../uploads/categories/' . $old_image_url);
            }
        }

        $ok = Category::update($id, $name, $new_image_url, $color_hex);
        Response::json($ok ? ['success' => true] : ['success' => false, 'message' => 'Erro ao atualizar categoria.']);
    }



    public function delete()
    {
        $id = $_POST['category_id'] ?? null;
        if (!$id) {
            Response::json(['success' => false, 'message' => 'ID obrigatório.']);
            return;
        }
        $db = Database::connect();
        $stmt = $db->prepare("SELECT image_url FROM categories WHERE category_id = ?");
        $stmt->bind_param("i", $id);
        $stmt->execute();
        $result = $stmt->get_result();
        if ($row = $result->fetch_assoc()) {
            $image = $row['image_url'];
            if (!empty($image) && file_exists(__DIR__ . '/../../uploads/categories/' . $image)) {
                @unlink(__DIR__ . '/../../uploads/categories/' . $image);
            }
        }
        $stmt->close();

        $ok = Category::delete($id);
        Response::json($ok ? ['success' => true] : ['success' => false, 'message' => 'Erro ao eliminar categoria.']);
    }
}
