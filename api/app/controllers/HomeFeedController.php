<?php

require_once __DIR__ . '/../models/HomeFeed.php';
require_once __DIR__ . '/../models/UserStats.php';
require_once __DIR__ . '/../models/Category.php';

class HomeFeedController
{
    /** Helper para anexar categorias de cada receita */
    private function attachCategoriesToRecipes(array &$recipes)
    {
        foreach ($recipes as &$recipe) {
            $recipeId = $recipe['recipe_id'];
            $recipe['categories'] = Category::getByRecipe($recipeId);
        }
    }

    /** Helper para preencher até N receitas usando fallback */
    private function fillWithFallback(array $recipes, int $target, array &$excludeIds): array
    {
        $count = count($recipes);
        if ($count < $target) {
            $fill = $target - $count;
            $fallback = HomeFeed::getFallback($fill, $excludeIds);
            foreach ($fallback as $r) $excludeIds[] = $r['recipe_id'];
            $recipes = array_merge($recipes, $fallback);
        }
        return $recipes;
    }

    public function getHomeFeed()
    {
        if (!isset($_GET['user_id'])) {
            echo json_encode([
                'success' => false,
                'message' => 'Parâmetro user_id em falta.'
            ]);
            return;
        }

        $userId = intval($_GET['user_id']);
        $shownIds = [];

        // RECOMMENDED
        $recommended = UserStats::getRecommendedByUserId($userId, $shownIds);
        $this->attachCategoriesToRecipes($recommended);
        foreach ($recommended as $r) $shownIds[] = $r['recipe_id'];
        $recommended = $this->fillWithFallback($recommended, 10, $shownIds);

        // WEEKLY
        $weekly = HomeFeed::getWeekly($shownIds);
        $this->attachCategoriesToRecipes($weekly);
        foreach ($weekly as $r) $shownIds[] = $r['recipe_id'];
        $weekly = $this->fillWithFallback($weekly, 10, $shownIds);

        // POPULAR
        $popular = HomeFeed::getPopular($shownIds);
        $this->attachCategoriesToRecipes($popular);
        foreach ($popular as $r) $shownIds[] = $r['recipe_id'];
        $popular = $this->fillWithFallback($popular, 10, $shownIds);

        // CATEGORIAS PERSONALIZADAS
        $categories = Category::getUserTop($userId, 3);
        $catRecipes = [];
        for ($i = 0; $i < 3; $i++) {
            $catId = $categories[$i]['category_id'] ?? null;
            if ($catId) {
                $recipes = HomeFeed::getByCategory($catId, $shownIds);
                $this->attachCategoriesToRecipes($recipes);
                foreach ($recipes as $r) $shownIds[] = $r['recipe_id'];
                $recipes = $this->fillWithFallback($recipes, 10, $shownIds);

                $categoryName = $categories[$i]['category_name'] ?? 'Sem nome';
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
