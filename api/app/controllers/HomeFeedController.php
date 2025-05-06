<?php

require_once __DIR__ . '/../models/HomeFeed.php';
require_once __DIR__ . '/../models/Recipe.php';
require_once __DIR__ . '/../models/UserStats.php';
require_once __DIR__ . '/../models/Category.php'; // Para o getCategoryName

class HomeFeedController
{
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
        foreach ($recommended as $r) $shownIds[] = $r['recipe_id'];

        if (count($recommended) < 10) {
            $fallback = HomeFeed::getFallbackRecipes(10 - count($recommended), $shownIds);
            foreach ($fallback as $r) $shownIds[] = $r['recipe_id'];
            $recommended = array_merge($recommended, $fallback);
        }

        // WEEKLY
        $weekly = HomeFeed::getWeeklyRecipes($shownIds);
        foreach ($weekly as $r) $shownIds[] = $r['recipe_id'];

        if (count($weekly) < 10) {
            $fallback = HomeFeed::getFallbackRecipes(10 - count($weekly), $shownIds);
            foreach ($fallback as $r) $shownIds[] = $r['recipe_id'];
            $weekly = array_merge($weekly, $fallback);
        }

        // POPULAR
        $popular = HomeFeed::getPopularRecipesFiltered($shownIds);
        foreach ($popular as $r) $shownIds[] = $r['recipe_id'];

        if (count($popular) < 10) {
            $fallback = HomeFeed::getFallbackRecipes(10 - count($popular), $shownIds);
            foreach ($fallback as $r) $shownIds[] = $r['recipe_id'];
            $popular = array_merge($popular, $fallback);
        }

        // CATEGORIAS PERSONALIZADAS
        $categories = HomeFeed::getTopUserCategories($userId);
        $catRecipes = [];
        for ($i = 0; $i < 3; $i++) {
            $catId = $categories[$i] ?? null;
            if ($catId) {
                $recipes = HomeFeed::getByCategory($catId, $shownIds);

                if (count($recipes) < 10) {
                    $extras = HomeFeed::getByCategory($catId, []);
                    foreach ($extras as $extra) {
                        if (!in_array($extra['recipe_id'], array_column($recipes, 'recipe_id'))) {
                            $recipes[] = $extra;
                            if (count($recipes) >= 10) break;
                        }
                    }
                }

                foreach ($recipes as $r) $shownIds[] = $r['recipe_id'];

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
