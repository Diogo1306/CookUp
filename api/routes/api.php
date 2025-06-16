<?php

require_once __DIR__ . '/../app/core/Router.php';

// Controllers
require_once __DIR__ . '/../app/controllers/RecipeController.php';
require_once __DIR__ . '/../app/controllers/UserController.php';
require_once __DIR__ . '/../app/controllers/SavedListController.php';
require_once __DIR__ . '/../app/controllers/CommentController.php';
require_once __DIR__ . '/../app/controllers/RatingController.php';
require_once __DIR__ . '/../app/controllers/CategoryController.php';
require_once __DIR__ . '/../app/controllers/UserStatsController.php';
require_once __DIR__ . '/../app/controllers/HomeFeedController.php';
require_once __DIR__ . '/../app/controllers/TrackingController.php';
require_once __DIR__ . '/../app/controllers/SearchController.php';
require_once __DIR__ . '/../app/controllers/ProfileController.php';
require_once __DIR__ . '/../app/controllers/IngredientController.php';
require_once __DIR__ . '/../app/controllers/TranslationController.php';
require_once __DIR__ . '/../app/controllers/SearchFiltersController.php';

// Instantiate controllers
$recipeController       = new RecipeController();
$userController         = new UserController();
$savedListController    = new SavedListController();
$commentController      = new CommentController();
$ratingController       = new RatingController();
$categoryController     = new CategoryController();
$userStatsController    = new UserStatsController();
$homeFeedController     = new HomeFeedController();
$trackingController     = new TrackingController();
$searchController       = new SearchController();
$profileController      = new ProfileController();
$ingredientController   = new IngredientController();
$translationController = new TranslationController();
$searchFiltersController = new SearchFiltersController();

// RECIPES
Router::add('GET',  'recipes',     [$recipeController, 'getAll']);
Router::add('GET',  'recipe',      [$recipeController, 'getById']);
Router::add('POST', 'recipes',     [$recipeController, 'save']);
Router::add('GET',  'popular_recipes_pagination', [$recipeController, 'getPopularPaginated']);
Router::add('GET',  'most_favorited_recipes',     [$recipeController, 'getMostFavorited']);
Router::add('GET',  'recipes_by_mealtype',        [$recipeController, 'getByCategory']);
Router::add('GET',  'recommended',                [$recipeController, 'getRecommended']);
Router::add('POST', 'upload_recipe_gallery', [$recipeController, 'uploadGallery']);
Router::add('GET', 'recipe_gallery', [$recipeController, 'getGallery']);
Router::add('POST', 'delete_recipe', [$recipeController, 'delete']);
Router::add('POST', 'upload_recipe_gallery', [$recipeController, 'uploadGallery']);

// PROFILE
Router::add('GET',  'profile_summary',   [$profileController, 'getSummary']);
Router::add('GET',  'profile_recipes',   [$profileController, 'getRecipes']);

// CATEGORIES
Router::add('GET',  'categories',            [$categoryController, 'getAll']);
Router::add('GET',  'popular_categories',    [$categoryController, 'getPopular']);
Router::add('GET',  'user_categories',       [$categoryController, 'getUserCategories']);

// COMMENTS
Router::add('POST', 'comment',   [$commentController, 'submit']);
Router::add('GET',  'comment',   [$commentController, 'getByRecipe']);

// ingredients API spoonacular Tests
Router::add('POST', 'add_recipe_ingredient', [$ingredientController, 'addIngredientToRecipe']);
Router::add('POST', 'translate', [$translationController, 'translate']);
Router::add('GET', 'autocomplete_ingredient', [$ingredientController, 'autocomplete']);

// TRACKING
Router::add('POST', 'track_interaction', [$trackingController, 'trackInteraction']);

// SEARCH
Router::add('GET',  'search_all', [$searchController, 'searchAll']);
Router::add('GET', 'search_recipes_filtered', [$searchFiltersController, 'searchFiltered']);

// HOME FEED (algoritmo completo)
Router::add('GET', 'home_feed',               [$homeFeedController, 'getHomeFeed']);
Router::add('GET', 'week_recipes',            [$homeFeedController, 'getWeekly']);
Router::add('GET', 'popular_home_recipes',    [$homeFeedController, 'getPopular']);
Router::add('GET', 'recipes_by_category',     [$homeFeedController, 'getRecipesByCategory']);

// RATINGS
Router::add('POST', 'rating', [$ratingController, 'submit']);
Router::add('GET',  'rating', [$ratingController, 'getAverage']);

// USER
Router::add('GET',  'user', [$userController, 'getUser']);
Router::add('POST', 'user', [$userController, 'save']);
Router::add('POST', 'update_profile', [$userController, 'updateProfile']);
Router::add('POST', 'delete_user', [$userController, 'delete']);

// USER STATS
Router::add('POST', 'update_stat',       [$userStatsController, 'updateStat']);
Router::add('POST', 'mark_finished',     [$userStatsController, 'markFinished']);

// SAVED LISTS
Router::add('GET',  'lists',               [$savedListController, 'getLists']);
Router::add('POST', 'lists',               [$savedListController, 'create']);
Router::add('POST', 'update_list',         [$savedListController, 'update']);
Router::add('GET',  'deleteList',          [$savedListController, 'delete']);
Router::add('POST', 'deleteRecipeFromList', [$savedListController, 'removeRecipe']);
Router::add('GET',  'list_recipes',        [$savedListController, 'getRecipes']);
Router::add('POST', 'list_recipes',        [$savedListController, 'addRecipe']);
Router::add('GET',  'recipeLists',         [$savedListController, 'getRecipeLists']);
Router::add('GET',  'savedRecipeIds',      [$savedListController, 'getUserSavedRecipeIds']);
Router::add('GET', 'lists_with_recipes', [$savedListController, 'getListsWithRecipes']);

// Execute route
Router::dispatch();
