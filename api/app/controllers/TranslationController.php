<?php
require_once __DIR__ . '/../models/Translation.php';
require_once __DIR__ . '/../core/Response.php';

class TranslationController
{
    public function translate()
    {
        $data = json_decode(file_get_contents('php://input'), true);
        if (!isset($data['text'])) {
            return Response::json(["success" => false, "message" => "Texto em falta."], 422);
        }
        $translated = Translation::translate($data['text']);
        return Response::json(["success" => true, "translated" => $translated]);
    }
}
