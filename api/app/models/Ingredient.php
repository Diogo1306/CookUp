<?php

require_once __DIR__ . '/../core/Database.php';
require_once __DIR__ . '/../models/Translation.php';

class Ingredient
{
    public static function findSimilarIngredient($pdo, $inputName, $maxDistance = 2)
    {
        $result = $pdo->query("SELECT ingredient_id, ingredient_name FROM ingredients");
        $closest = null;
        $smallest = null;
        while ($row = $result->fetch_assoc()) {
            $lev = levenshtein(strtolower($inputName), strtolower($row['ingredient_name']));
            if ($lev <= $maxDistance && ($smallest === null || $lev < $smallest)) {
                $closest = $row;
                $smallest = $lev;
            }
        }
        return $closest;
    }


    public static function addToRecipe($ingredientName, $quantity, $recipeId)
    {
        $db = Database::connect();

        $similar = self::findSimilarIngredient($db, $ingredientName);

        if ($similar) {
            $ingredientId = $similar['ingredient_id'];
            $matched = true;
        } else {
            $ingredientNameEn = Translation::translate($ingredientName, 'pt', 'en');
            $image_url = self::fetchAndSaveImage($ingredientNameEn);

            $category_id = 14;
            $stmt = $db->prepare("INSERT INTO ingredients (ingredient_name, category_id, image_url) VALUES (?, ?, ?)");
            $stmt->bind_param("sis", $ingredientName, $category_id, $image_url);
            $stmt->execute();
            $ingredientId = $db->insert_id;
            $matched = false;
        }

        $stmt = $db->prepare("INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity) VALUES (?, ?, ?)");
        $stmt->bind_param("iis", $recipeId, $ingredientId, $quantity);
        $stmt->execute();

        return [
            "success" => true,
            "ingredient_id" => $ingredientId,
            "ingredient_name" => $ingredientName,
            "matched_existing" => $matched
        ];
    }

    public static function getOrCreate($ingredientName)
    {
        $db = Database::connect();

        $existing = self::findSimilarIngredient($db, $ingredientName);
        if ($existing) {
            return $existing['ingredient_id'];
        }

        $ingredientNameEn = Translation::translate($ingredientName, 'pt', 'en');
        $image_url = self::fetchAndSaveImage($ingredientNameEn);
        $category_id = 14;

        $stmt = $db->prepare("INSERT INTO ingredients (ingredient_name, category_id, image_url) VALUES (?, ?, ?)");
        $stmt->bind_param("sis", $ingredientName, $category_id, $image_url);
        $stmt->execute();
        $ingredientId = $db->insert_id;
        $stmt->close();

        return $ingredientId;
    }


    public static function fetchAndSaveImage($ingredientName)
    {
        $apiKey = "44806cf7384f4fbeb461004158d1977f";
        $query = urlencode($ingredientName);
        $url = "https://api.spoonacular.com/food/ingredients/search?query=$query&apiKey=$apiKey";

        $response = file_get_contents($url);
        $data = json_decode($response, true);

        if (isset($data['results'][0]['image'])) {
            $imgUrl = "https://spoonacular.com/cdn/ingredients_250x250/" . $data['results'][0]['image'];
            $filename = strtolower(preg_replace('/[^a-z0-9]/', '', $ingredientName)) . ".jpg";
            $destination = __DIR__ . "/../../uploads/ingredients/" . $filename;

            if (!is_dir(__DIR__ . "/../../uploads/ingredients/")) {
                mkdir(__DIR__ . "/../../uploads/ingredients/", 0777, true);
            }

            file_put_contents($destination, file_get_contents($imgUrl));

            return $filename;
        } else {
            return "default.png";
        }
    }
    public static function autocomplete($pdo, $input)
    {
        $stmt = $pdo->prepare("SELECT * FROM ingredients WHERE ingredient_name LIKE ? ORDER BY ingredient_name LIMIT 10");
        $like = '%' . $input . '%';
        $stmt->bind_param("s", $like);
        $stmt->execute();
        $result = $stmt->get_result();
        $ingredients = [];
        while ($row = $result->fetch_assoc()) {
            $ingredients[] = $row;
        }
        return $ingredients;
    }
}
