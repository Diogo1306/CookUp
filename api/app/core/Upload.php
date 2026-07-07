<?php

class Upload
{
    /** Extensões de imagem permitidas para upload. */
    const ALLOWED_IMAGE_EXTENSIONS = ['jpg', 'jpeg', 'png', 'gif', 'webp'];

    /**
     * Valida um ficheiro enviado ($_FILES[...]) e devolve uma extensão de
     * imagem segura (minúscula), ou null se não for uma imagem permitida.
     *
     * Verifica a extensão E o conteúdo real do ficheiro (getimagesize),
     * impedindo que um ficheiro executável (ex.: .php renomeado) seja
     * gravado na pasta pública de uploads.
     */
    public static function safeImageExtension($file): ?string
    {
        if (!is_array($file) || empty($file['tmp_name']) || !is_uploaded_file($file['tmp_name'])) {
            return null;
        }

        $ext = strtolower(pathinfo($file['name'] ?? '', PATHINFO_EXTENSION));
        if (!in_array($ext, self::ALLOWED_IMAGE_EXTENSIONS, true)) {
            return null;
        }

        // Confirma que o conteúdo é mesmo uma imagem, não apenas a extensão.
        if (@getimagesize($file['tmp_name']) === false) {
            return null;
        }

        return $ext;
    }
}
