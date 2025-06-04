<?php
require_once __DIR__ . '/../config/config.php';

try {
    $mysqli = @new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);

    if ($mysqli->connect_errno) {
        http_response_code(500);
        header("Content-Type: application/json");
        echo json_encode(["success" => false, "message" => "Base de dados indisponível."]);
        exit();
    }
} catch (Throwable $e) {
    http_response_code(500);
    header("Content-Type: application/json");
    echo json_encode(["success" => false, "message" => "Base de dados indisponível.", "details" => $e->getMessage()]);
    exit();
}

http_response_code(200);
header("Content-Type: application/json");
echo json_encode(["success" => true, "message" => "Ligação com a base de dados estabelecida."]);
exit();
