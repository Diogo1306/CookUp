<?php
require_once __DIR__ . '/../config/config.php';

try {
    $mysqli = @new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);

    if ($mysqli->connect_errno) {
        http_response_code(500);
        echo json_encode(["status" => "error", "msg" => "DB off"]);
        exit();
    }
} catch (Throwable $e) {
    http_response_code(500);
    echo json_encode(["status" => "error", "msg" => "DB off", "details" => $e->getMessage()]);
    exit();
}

http_response_code(200);
echo json_encode(["status" => "ok"]);
exit();
