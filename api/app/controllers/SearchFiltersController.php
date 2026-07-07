<?php

require_once __DIR__ . '/../models/SearchFilters.php';

class SearchFiltersController
{
    public function searchFiltered()
    {
        $params = [
            'query' => $_GET['query'] ?? '',
            'filter' => $_GET['filter'] ?? '',
            'userId' => isset($_GET['userId']) ? (int)$_GET['userId'] : null,
            'min_time' => $_GET['min_time'] ?? null,
            'max_time' => $_GET['max_time'] ?? null,
            'max_ingredients' => $_GET['max_ingredients'] ?? null,
            'difficulty' => $_GET['difficulty'] ?? '',
            'excludeIds' => isset($_GET['excludeIds']) ? (array)$_GET['excludeIds'] : [],
            // 'limit' => $_GET['limit'] ?? 30,
            // 'offset' => $_GET['offset'] ?? 0,
        ];

        $data = SearchFilters::filterRecipes($params);
        return Response::json(["success" => true, "data" => $data]);
    }
}
