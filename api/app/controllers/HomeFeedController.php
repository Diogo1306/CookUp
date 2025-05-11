<?php

require_once __DIR__ . '/../models/HomeFeed.php';
require_once __DIR__ . '/../models/Recipe.php';
require_once __DIR__ . '/../models/UserStats.php';
require_once __DIR__ . '/../models/Category.php';

class HomeFeedController
{

    private function attachCategoryObjects(&$recipes)
    {
        foreach ($recipes as &$recipe) {
            $recipeId = $recipe['recipe_id'];
            $recipe['categories'] = Category::getCategoriesByRecipeId($recipeId);
        }
    }

    public function getHomeFeed()
    {
        if (!isset($_GET['user_id'])) {
            echo json_encode([
                'success' => false,
                'message' => 'user_id is required'
            ]);
            return;
        }

        $userId = intval($_GET['user_id']);
        $shownIds = [];

        // RECOMMENDED
        $recommended = UserStats::getRecommendedByUserId($userId, $shownIds);
        $this->attachCategoryObjects($recommended);
        foreach ($recommended as $r) $shownIds[] = $r['recipe_id'];

        if (count($recommended) < 10) {
            $fallback = HomeFeed::getFallbackRecipes(10 - count($recommended), $shownIds);
            foreach ($fallback as $r) $shownIds[] = $r['recipe_id'];
            $recommended = array_merge($recommended, $fallback);
        }

        // WEEKLY
        $weekly = HomeFeed::getWeeklyRecipes($shownIds);
        $this->attachCategoryObjects($weekly);
        foreach ($weekly as $r) $shownIds[] = $r['recipe_id'];

        if (count($weekly) < 10) {
            $fallback = HomeFeed::getFallbackRecipes(10 - count($weekly), $shownIds);
            foreach ($fallback as $r) $shownIds[] = $r['recipe_id'];
            $weekly = array_merge($weekly, $fallback);
        }

        // POPULAR
        $popular = HomeFeed::getPopularRecipesFiltered($shownIds);
        $this->attachCategoryObjects($popular);
        foreach ($popular as $r) $shownIds[] = $r['recipe_id'];

        if (count($popular) < 10) {
            $fallback = HomeFeed::getFallbackRecipes(10 - count($popular), $shownIds);
            foreach ($fallback as $r) $shownIds[] = $r['recipe_id'];
            $popular = array_merge($popular, $fallback);
        }

        // CATEGORIAS PERSONALIZADAS
        $categories = Category::getTopCategoriesByUser($userId, 3);
        $catRecipes = [];
        for ($i = 0; $i < 3; $i++) {
            $catId = $categories[$i]['category_id'] ?? null;
            if ($catId) {
                $recipes = HomeFeed::getByCategory($catId, $shownIds);
                $this->attachCategoryObjects($recipes);
                foreach ($recipes as $r) $shownIds[] = $r['recipe_id'];

                if (count($recipes) < 10) {
                    $fill = 10 - count($recipes);
                    $fallback = HomeFeed::getFallbackRecipes($fill, $shownIds);
                    foreach ($fallback as $r) $shownIds[] = $r['recipe_id'];
                    $recipes = array_merge($recipes, $fallback);
                }

                $categoryName = Category::getCategoryName($catId);

                $catRecipes["cat" . ($i + 1)] = [
                    'category_id' => $catId,
                    'category_name' => $categoryName,
                    'recipes' => $recipes
                ];
            }
        }

        echo json_encode([
            'success' => true,
            'data' => [
                'recommended' => $recommended,
                'weekly' => $weekly,
                'popular' => $popular,
                'categories' => $catRecipes
            ]
        ]);
    }
}
