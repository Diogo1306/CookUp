<?php

require_once __DIR__ . '/../models/Recipe.php';
require_once __DIR__ . '/../core/Response.php';

class RecipeController
{

    public function save()
    {
        if (isset($_POST['title'])) {
            $data = $_POST;

            if (isset($data['categories'])) $data['categories'] = json_decode($data['categories'], true);
            if (isset($data['ingredients'])) $data['ingredients'] = json_decode($data['ingredients'], true);

            $oldGallery = [];
            if (isset($data['old_gallery'])) {
                $oldGallery = json_decode($data['old_gallery'], true);
            }

            $oldGalleryNames = [];
            foreach ($oldGallery as $img) {
                $oldGalleryNames[] = basename($img);
            }

            $galleryNames = [];
            if (isset($_FILES['gallery'])) {
                $uploadDir = __DIR__ . '/../../uploads/recipes/';
                if (!is_dir($uploadDir)) mkdir($uploadDir, 0777, true);

                $count = count($_FILES['gallery']['name']);
                for ($i = 0; $i < $count; $i++) {
                    $ext = pathinfo($_FILES['gallery']['name'][$i], PATHINFO_EXTENSION);
                    $uniqueName = uniqid('recipe_', true) . '_' . bin2hex(random_bytes(4)) . '.' . $ext;
                    move_uploaded_file($_FILES['gallery']['tmp_name'][$i], $uploadDir . $uniqueName);
                    $galleryNames[] = $uniqueName;
                }
            }

            if (!empty($data['recipe_id'])) {
                $currentGallery = Recipe::getGalleryImages($data['recipe_id']);
                $toRemove = array_diff($currentGallery, $oldGalleryNames);
                $uploadDir = __DIR__ . '/../../uploads/recipes/';
                foreach ($toRemove as $imgName) {
                    if ($imgName !== 'default.png' && file_exists(filename: $uploadDir . $imgName)) {
                        @unlink($uploadDir . $imgName);
                    }
                }
            }

            $finalGallery = array_merge($oldGalleryNames, $galleryNames);

            if (!empty($finalGallery)) {
                $data['image'] = $finalGallery[0];
            } else {
                $data['image'] = 'default.png';
            }

            $data['gallery'] = $finalGallery;
        } else {
            $data = json_decode(file_get_contents("php://input"), true);
        }

        $recipeId = Recipe::saveOrUpdate($data);

        if (isset($data['categories']) && is_array($data['categories'])) {
            Recipe::updateCategories($recipeId, $data['categories']);
        }
        if (isset($data['ingredients']) && is_array($data['ingredients'])) {
            Recipe::updateIngredients($recipeId, $data['ingredients']);
        }
        if (isset($data['gallery']) && is_array($data['gallery']) && count($data['gallery']) > 0) {
            Recipe::saveGalleryImages($recipeId, $data['gallery']);
        }

        Response::json([
            "success" => true,
            "recipe_id" => $recipeId
        ]);
    }

    public function delete()
    {
        if (!isset($_POST['recipe_id'])) {
            return Response::json(["success" => false, "message" => "recipe_id obrigatório!"], 422);
        }

        $recipeId = intval($_POST['recipe_id']);
        $ok = Recipe::delete($recipeId);

        Response::json(
            $ok
                ? ["success" => true, "message" => "Receita (e imagens) deletadas com sucesso!"]
                : ["success" => false, "message" => "Erro ao deletar receita."]
        );
    }

    public function uploadGallery()
    {
        $recipeId = $_POST['recipe_id'] ?? null;
        if (!$recipeId) {
            return Response::json(['success' => false, 'message' => 'recipe_id em falta']);
        }
        if (!isset($_FILES['images'])) {
            return Response::json(['success' => false, 'message' => 'Imagens em falta']);
        }

        Recipe::saveGalleryImages($recipeId, $_FILES['images']);
        Response::json(['success' => true, 'message' => 'Imagens carregadas com sucesso!']);
    }


    public function getAll()
    {
        $recipes = Recipe::getAll();
        Response::json(
            !empty($recipes)
                ? ["success" => true, "data" => $recipes]
                : ["success" => false, "message" => "Nenhuma receita encontrada."]
        );
    }

    public function getById()
    {
        if (!isset($_GET['recipe_id'])) {
            return Response::json(['success' => false, 'message' => 'ID da receita em falta.']);
        }

        $recipe_id = intval($_GET['recipe_id']);
        $recipe = Recipe::getById($recipe_id);

        Response::json(
            $recipe
                ? ['success' => true, 'data' => $recipe]
                : ['success' => false, 'message' => 'Receita não encontrada.']
        );
    }

    public function getPopularPaginated()
    {
        $page = isset($_GET['page']) ? intval($_GET['page']) : 1;
        $data = Recipe::getPopularWithPagination($page);
        Response::json(['success' => true, 'data' => $data]);
    }

    public function getRecommended()
    {
        $recipes = Recipe::getRecommendedRecipes();
        Response::json(
            !empty($recipes)
                ? ["success" => true, "data" => $recipes]
                : ["success" => false, "message" => "Nenhuma receita recomendada."]
        );
    }

    public function getMostFavorited()
    {
        $recipes = Recipe::getMostFavoritedRecipes();
        Response::json(
            !empty($recipes)
                ? ["success" => true, "data" => $recipes]
                : ["success" => false, "message" => "Nenhuma receita favorita encontrada."]
        );
    }

    public function getByCategory()
    {
        if (!isset($_GET['category_id'])) {
            return Response::json(['success' => false, 'message' => 'Parâmetro category_id em falta.']);
        }

        $categoryId = intval($_GET['category_id']);
        $recipes = Recipe::getRecipesByMealType($categoryId);

        Response::json(
            !empty($recipes)
                ? ["success" => true, "data" => $recipes]
                : ["success" => false, "message" => "Nenhuma receita encontrada para esta categoria."]
        );
    }
}
