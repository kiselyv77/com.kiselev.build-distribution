package com.kiselev.modules.uploadBuild

import io.ktor.http.content.*

interface UploadController {
    /**
     * Сохраняет файлы для указанного проекта.
     *
     * @param files Список файлов для сохранения.
     * @param projectName Название проекта.
     */
    suspend fun saveFiles(files: List<PartData.FileItem>, projectName: String?)
}