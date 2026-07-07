<?php
// ─────────────────────────────────────────────────────────────
//  CookUp API — MODELO de configuração local
//  Copia este ficheiro para "config.local.php" e preenche os valores.
//  O config.local.php NÃO é versionado (está no .gitignore).
// ─────────────────────────────────────────────────────────────

// Base de dados (MySQL / XAMPP)
define('DB_HOST', 'localhost');
define('DB_NAME', 'cookup_db');
define('DB_USER', 'root');
define('DB_PASS', '');

// URL base da API — o teu IP local (cmd > ipconfig > Endereço IPv4)
// e o caminho onde colocaste a pasta api/ dentro do htdocs.
define('BASE_URL', 'http://O_TEU_IP/caminho/para/api/');

// Chave da API Spoonacular — cria a tua em https://spoonacular.com/food-api
define('SPOONACULAR_API_KEY', 'A_TUA_CHAVE_SPOONACULAR');
