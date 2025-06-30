<?php

define('IS_LOCAL', true);

if (IS_LOCAL) {
    define('DB_HOST', 'localhost');
    define('DB_NAME', 'cookup_db');
    define('DB_USER', 'root');
    define('DB_PASS', '');

    // Troque ip, abre o cmd digite ipconfig e pegue o IPv4 Address, caso seja presiso mudar url tambem pode
    define('BASE_URL', 'http://192.168.0.26/PAP/CookUp_Core/');
} else {
    // define('DB_HOST', 'meu.servidor.com');
    // define('DB_NAME', 'cookup_prod');
    // define('DB_USER', 'admin');
    // define('DB_PASS', 'senha_segura');

    // define('BASE_URL', 'https://teudominio.com/'); 
}

define('UPLOADS_FOLDER', 'uploads/');
define('INGREDIENTS_FOLDER', 'ingredients/');
define('CATEGORIES_FOLDER', 'categories/');
define('DEFAULT_IMAGE', 'uploads/default.png');
define('RECIPES_FOLDER', 'recipes/');
define('PROFILE_PICTURES', 'profile_pictures/');
define('PROFILE_PICTURE_PATH', BASE_URL . UPLOADS_FOLDER . PROFILE_PICTURES);
