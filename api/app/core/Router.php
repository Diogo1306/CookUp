<?php

class Router
{
    private static $routes = [];

    public static function add($method, $route, $callback)
    {
        self::$routes[strtoupper($method)][$route] = $callback;
    }

    public static function dispatch()
    {
        $method = strtoupper($_SERVER['REQUEST_METHOD']);
        $route = trim($_GET['route'] ?? '');

        if (isset(self::$routes[$method][$route])) {
            call_user_func(self::$routes[$method][$route]);
        } else {
            http_response_code(404);
            header('Content-Type: application/json');
            echo json_encode([
                "success" => false,
                "message" => "Rota '$route' não encontrada para método $method",
            ]);
        }
    }
}
