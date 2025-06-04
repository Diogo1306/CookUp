<?php

require_once __DIR__ . '/../models/Search.php';
require_once __DIR__ . '/../core/Response.php';

class SearchController
{
    public function searchAll()
    {
        $query = $_GET['query'] ?? null;

        if (!$query) {
            return Response::json(["success" => false, "message" => "Parâmetro 'query' em falta."], 422);
        }

        $data = Search::searchGlobal($query);
        return Response::json(["success" => true, "data" => $data]);
    }
}
