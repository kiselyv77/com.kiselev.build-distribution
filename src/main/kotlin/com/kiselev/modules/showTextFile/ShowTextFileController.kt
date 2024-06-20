package com.kiselev.modules.showTextFile

import java.io.File

interface ShowTextFileController {
    /**
     * Получает файл по указанному названию приложения, дате/времени и имени файла.
     */
    fun getFile(projectName: String?, dateTime: String?, fileName: String?): File
}