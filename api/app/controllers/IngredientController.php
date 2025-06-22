<?php

require_once __DIR__ . '/../models/ingredient.php';
require_once __DIR__ . '/../core/Database.php';

class IngredientController
{

    public function getAll()
    {
        $ingredients = Ingredient::getAll();
        Response::json(
            !empty($ingredients)
                ? ["success" => true, "data" => $ingredients]
                : ["success" => false, "message" => "Nenhum ingrediente encontrado."]
        );
    }

    public function create()
    {
        if (empty($_POST['ingredient_name'])) {
            Response::json(['success' => false, 'message' => 'Nome obrigatório!']);
            return;
        }

        $ingredient_name = $_POST['ingredient_name'];
        $image_url = null;

        if (isset($_FILES['image_url']) && $_FILES['image_url']['error'] === UPLOAD_ERR_OK) {
            $ext = pathinfo($_FILES['image_url']['name'], PATHINFO_EXTENSION);
            $image_url = uniqid('ing_', true) . '.' . $ext;
            $uploadPath = __DIR__ . '/../../uploads/ingredients/' . $image_url;
            if (!move_uploaded_file($_FILES['image_url']['tmp_name'], $uploadPath)) {
                Response::json(['success' => false, 'message' => 'Erro ao salvar imagem.']);
                return;
            }
        }

        $ok = Ingredient::create($ingredient_name, $image_url);

        Response::json($ok ? ['success' => true] : ['success' => false, 'message' => 'Erro ao criar ingrediente.']);
    }

    public function update()
    {
        $id = $_POST['ingredient_id'] ?? null;
        $name = $_POST['ingredient_name'] ?? null;
        $old_image_url = $_POST['image_url'] ?? '';

        if (!$id || !$name) {
            Response::json(['success' => false, 'message' => 'ID e nome obrigatórios.']);
            return;
        }

        // Busca imagem antiga do banco, caso não venha do front (opcional)
        $db = Database::connect();
        $stmt = $db->prepare("SELECT image_url FROM ingredients WHERE ingredient_id = ?");
        $stmt->bind_param("i", $id);
        $stmt->execute();
        $result = $stmt->get_result();
        if ($row = $result->fetch_assoc()) {
            $old_image_url = $row['image_url'];
        }
        $stmt->close();

        $new_image_url = $old_image_url;

        if (isset($_FILES['image_url']) && $_FILES['image_url']['error'] === UPLOAD_ERR_OK) {
            $ext = pathinfo($_FILES['image_url']['name'], PATHINFO_EXTENSION);
            $new_image_url = uniqid('ing_', true) . '.' . $ext;
            $uploadPath = __DIR__ . '/../../uploads/ingredients/' . $new_image_url;
            if (!move_uploaded_file($_FILES['image_url']['tmp_name'], $uploadPath)) {
                Response::json(['success' => false, 'message' => 'Erro ao salvar imagem.']);
                return;
            }
            if (!empty($old_image_url) && file_exists(__DIR__ . '/../../uploads/ingredients/' . $old_image_url)) {
                @unlink(__DIR__ . '/../../uploads/ingredients/' . $old_image_url);
            }
        }

        $ok = Ingredient::update($id, $name, $new_image_url);
        Response::json($ok ? ['success' => true] : ['success' => false, 'message' => 'Erro ao atualizar ingrediente.']);
    }

    public function delete()
    {
        $id = $_POST['ingredient_id'] ?? null;
        if (!$id) {
            Response::json(['success' => false, 'message' => 'ID obrigatório.']);
            return;
        }
        $db = Database::connect();
        $stmt = $db->prepare("SELECT image_url FROM ingredients WHERE ingredient_id = ?");
        $stmt->bind_param("i", $id);
        $stmt->execute();
        $result = $stmt->get_result();
        if ($row = $result->fetch_assoc()) {
            $image = $row['image_url'];
            if (!empty($image) && file_exists(__DIR__ . '/../../uploads/ingredients/' . $image)) {
                @unlink(__DIR__ . '/../../uploads/ingredients/' . $image);
            }
        }
        $stmt->close();

        $ok = Ingredient::delete($id);
        Response::json($ok ? ['success' => true] : ['success' => false, 'message' => 'Erro ao eliminar ingrediente.']);
    }

    public function addIngredientToRecipe()
    {
        $data = json_decode(file_get_contents('php://input'), true);

        if (!isset($data['ingredient_name'], $data['quantity'], $data['recipe_id'])) {
            return Response::json(["success" => false, "message" => "Parâmetros obrigatórios em falta."], 422);
        }

        $ingredientName = trim($data['ingredient_name']);
        $quantity = trim($data['quantity']);
        $recipeId = (int)$data['recipe_id'];

        $result = Ingredient::addToRecipe($ingredientName, $quantity, $recipeId);

        return Response::json($result);
    }

    public function autocomplete()
    {
        $input = isset($_GET['q']) ? trim($_GET['q']) : '';
        if (strlen($input) < 2) {
            return Response::json([], 200);
        }
        $pdo = Database::connect();
        $results = Ingredient::autocomplete($pdo, $input);
        return Response::json($results, 200);
    }
}
