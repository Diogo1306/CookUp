<?php

require_once __DIR__ . '/../core/Database.php';

class Translation
{
    public static function translate($text, $from = 'pt', $to = 'en')
    {
        $url = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=$from&tl=$to&dt=t&q=" . urlencode($text);
        $resposta = @file_get_contents($url);
        $resultado = json_decode($resposta, true);
        if (isset($resultado[0][0][0])) {
            return $resultado[0][0][0];
        }
        return $text;
    }
}
