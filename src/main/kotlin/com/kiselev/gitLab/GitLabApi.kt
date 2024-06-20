package com.kiselev.gitLab

interface GitLabApi {
    /**
     * Отправляет запрос в GitLab для запуска пейплайна.
     * @param projectId Айди проекта в GitLab.
     * @param refName Название ветки.
     * @param triggerToken Триггер токен созданный в GitLab для проекта.
     * @param gitLabBaseUrl Ссылка на GitLab.
     */
    suspend fun triggerPipeline(
        projectId: Int,
        refName: String,
        triggerToken: String,
        gitLabBaseUrl: String
    ): TriggerResult
}