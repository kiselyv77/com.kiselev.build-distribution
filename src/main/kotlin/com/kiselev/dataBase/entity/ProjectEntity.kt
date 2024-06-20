package com.kiselev.dataBase.entity

/**
 * Модель проекта.
 * @param name уникальный идентификатор
 * @param accessToken приватный токен доступа
 *
 * Остальные параметры нужны для запуска пейплайнов по запросу
 * @param gitLabBaseUrl
 * @param triggerToken
 * @param gitlabId
 */
data class ProjectEntity(
    val name: String,
    val accessToken: String,
    val gitLabBaseUrl: String?,
    val triggerToken: String?,
    val gitlabId: Int?
)