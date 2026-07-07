<?php
// ─────────────────────────────────────────────────────────────
//  CookUp API — configuração base
//  Os valores por-máquina (BD, IP, chaves) estão em config.local.php,
//  que NÃO é versionado. Este ficheiro só carrega esse e deriva
//  os caminhos das pastas — não precisa de ser editado.
// ─────────────────────────────────────────────────────────────

$localConfig = __DIR__ . '/config.local.php';

if (!file_exists($localConfig)) {
    http_response_code(500);
    die('Falta o ficheiro de configuração. Copia "config.local.example.php" para "config.local.php" e preenche os valores.');
}

require_once $localConfig;

// Caminhos das pastas de uploads (derivados de BASE_URL — não editar)
define('UPLOADS_FOLDER', 'uploads/');
define('INGREDIENTS_FOLDER', 'ingredients/');
define('CATEGORIES_FOLDER', 'categories/');
define('DEFAULT_IMAGE', 'uploads/default.png');
define('RECIPES_FOLDER', 'recipes/');
define('PROFILE_PICTURES', 'profile_pictures/');
define('PROFILE_PICTURE_PATH', BASE_URL . UPLOADS_FOLDER . PROFILE_PICTURES);
