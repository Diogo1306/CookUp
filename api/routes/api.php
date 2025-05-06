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

// Recipes
Router::add('GET',  'recipe',                  [$recipeController, 'getAllRecipes']);
Router::add('POST', 'recipe',                  [$recipeController, 'createOrUpdateRecipe']);
Router::add('GET',  'recipe_detail',           [$recipeController, 'getRecipeDetail']);
Router::add('GET',  'popular_recipes',         [$recipeController, 'getPopularRecipes']);
Router::add('GET',  'most_favorited_recipes',  [$recipeController, 'getMostFavoritedRecipes']);
Router::add('GET',  'recipes_by_mealtype',     [$recipeController, 'getRecipesByMealType']);
Router::add('GET',  'recommended',             [$recipeController, 'getRecommendedRecipes']);

// Categories
Router::add('GET', 'categories',         [$categoryController, 'getAll']);
Router::add('GET', 'user_categories', [$categoryController, 'getUserCategories']);
Router::add('GET', 'popular_categories', [$categoryController, 'getPopular']);

// Comments
Router::add('POST', 'comment', [$commentController, 'submitComment']);
Router::add('GET',  'comment', [$commentController, 'getComments']);

// Tracking
Router::add('POST', 'track_interaction', [$trackingController, 'trackInteraction']);

// Home Feed (algoritmo completo)
Router::add('GET', 'home_feed',               [$homeFeedController, 'getHomeFeed']);
Router::add('GET', 'week_recipes',            [$homeFeedController, 'getWeekly']);
Router::add('GET', 'popular_home_recipes',    [$homeFeedController, 'getPopular']);
Router::add('GET', 'top_user_categories',     [$homeFeedController, 'getTopUserCategories']);
Router::add('GET', 'recipes_by_category',     [$homeFeedController, 'getRecipesByCategory']);

// Ratings
Router::add('POST', 'rating', [$ratingController, 'submitRating']);
Router::add('GET',  'rating', [$ratingController, 'getAverage']);

// User
Router::add('GET',  'user', [$userController, 'getUser']);
Router::add('POST', 'user', [$userController, 'createOrUpdateUser']);

// User Stats
Router::add('POST', 'update_stat',       [$userStatsController, 'updateStat']);
Router::add('POST', 'mark_finished',     [$userStatsController, 'markFinished']);
Router::add('GET',  'recommended_user',  [$userStatsController, 'getUserRecommendations']);

// Saved Lists
Router::add('GET',  'lists',                  [$savedListController, 'getLists']);
Router::add('POST', 'lists',                  [$savedListController, 'createList']);
Router::add('POST', 'update_list',            [$savedListController, 'updateList']);
Router::add('GET',  'deleteList',             [$savedListController, 'deleteList']);
Router::add('POST', 'deleteRecipeFromList',   [$savedListController, 'deleteRecipeFromList']);
Router::add('GET',  'list_recipes',           [$savedListController, 'getRecipes']);
Router::add('POST', 'list_recipes',           [$savedListController, 'addRecipe']);
Router::add('GET',  'recipeLists',            [$savedListController, 'getRecipeLists']);
Router::add('GET',  'savedRecipeIds',         [$savedListController, 'getUserSavedRecipeIds']);

// Execute route
Router::dispatch();
