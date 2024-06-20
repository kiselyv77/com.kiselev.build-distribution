package com.kiselev.fileStore

interface BuildsFileStore {

    /**
     * Получает последний билд для указанного проекта.
     *
     * @param projectName Название проекта.
     * @return Ссылка на последний билд
     */
    fun getLastBuild(projectName: String): String

    /**
     * Получает недавние билды для указанного проекта.
     *
     * @param projectName Название проекта.
     * @return Ссылка на последний билд
     */
    fun getRecentlyBuilds(projectName: String): String

    /**
     * Очищает старые билды для указанного проекта.
     *
     * @param projectName Название проекта (необязательно).
     * @return Тест с отчетом
     */
    fun clearOldBuilds(projectName: String?): String
}