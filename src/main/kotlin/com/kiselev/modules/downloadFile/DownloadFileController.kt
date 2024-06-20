package com.kiselev.modules.downloadFile

import java.io.File

interface DownloadFileController {
    /**
     * Получает файл по указанному названию приложения, дате/времени и имени файла.
     */
    fun getFile(projectName: String?, dateTime: String?, fileName: String?): File
}