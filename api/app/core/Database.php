<?php

require_once '../config/config.php';

class Database
{
    private static $conn = null;

    public static function connect()
    {
        if (!self::$conn) {
            self::$conn = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);

            if (self::$conn->connect_error) {
                if (class_exists('Response') && method_exists('Response', 'json')) {
                    Response::json(['success' => false, 'message' => 'Erro de conexão: ' . self::$conn->connect_error], 500);
                } else {
                    die("Erro de conexão: " . self::$conn->connect_error);
                }
            }

            self::$conn->set_charset("utf8mb4");
        }

        return self::$conn;
    }
}
