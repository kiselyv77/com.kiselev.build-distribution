package com.kiselev.dataBase.dao

import com.kiselev.dataBase.entity.ProjectEntity

interface ProjectDao {

    /**
     * Вставляет новый проект в базу данных.
     *
     * @param name Название проекта.
     * @param accessToken Приватный токен доступа.
     * Остальные параметры нужны для запуска пейплайнов по запросу
     * @param gitlabBaseUrl
     * @param triggerToken
     * @param gitlabId
     */
    fun insertProject(
        name: String,
        accessToken: String,
        gitlabBaseUrl: String?,
        triggerToken: String?,
        gitlabId: Int?,
    )

    /**
     * Получает проект по названию.
     *
     * @param projectName Название проекта.
     * @return Проект, или null, если проект не найден.
     */

    fun getProjectByName(projectName: String): ProjectEntity?

    /**
     * Получает проект по токену доступа.
     *
     * @param accessToken Токен доступа.
     * @return Проект, или null, если проект не найден.
     */
    fun getProjectByAccessToken(accessToken: String): ProjectEntity?

    /**
     * Получает все проекты.
     */
    fun getProjects(): List<ProjectEntity>

    /**
     * Удаляет проект по названию.
     */
    fun deleteProject(projectName: String)
}