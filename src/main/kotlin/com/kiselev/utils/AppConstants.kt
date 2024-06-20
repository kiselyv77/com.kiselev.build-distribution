package com.kiselev.utils

import java.io.File

object AppConstants {
    const val TG_API_KEY = "Telegram API KEY" // подставте Telegram API KEY от вашего бота
    const val ROOT_PACKAGE = "root/serverFiles/builds"
    const val DATE_TIME_FORMAT = "dd_MM_yyyy_HH_mm_ss"
    const val DATE_TIME_FORMAT_FOR_DISPLAY = "dd.MM.yyyy HH:mm:ss"
    const val BASE_URL = "http://11.111.11.1/" // вместо 11.111.11.1 вставте ваш хост сервера
    const val CHANGELOG_FILE_NAME = "changelog.txt"
    const val APKS_PATH = "apks"
    const val SHOW_TEXT_PATH = "showtext"
    const val DEVELOP_REF = "develop"
    val BUILDS_DIR = File("$ROOT_PACKAGE/")

    private const val APK = "apk"
    private const val AAB = "aab"
    val EXTENSIONS_LIST = listOf(APK, AAB)
}
