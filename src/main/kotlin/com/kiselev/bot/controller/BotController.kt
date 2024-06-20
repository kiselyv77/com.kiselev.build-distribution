package com.kiselev.bot.controller

import com.kiselev.dataBase.entity.UserToProjectEntity
import com.kiselev.dataBase.entity.ProjectEntity
import com.kiselev.dataBase.types.UserStatusInProject

interface BotController {

    /**
     * Создает новый проект. Добавляет создателя в этот проект.
     */
    suspend fun createProject(
        projectName: String,
        userName: String,
        accessTokenPrivate: String,
        gitlabBaseUrl: String?,
        triggerToken: String?,
        gitlabIdInt: Int?,
        creatorChatId: Long
    ): ProjectEntity?

    /**
     * Получает проект по его названию.
     */
    suspend fun getProject(projectName: String): ProjectEntity?

    /**
     * Добавляет пользователя к проекту с указанным статусом.
     */
    suspend fun addUserToProject(chatId: Long, userName: String, projectName: String, status: UserStatusInProject)

    /**
     * Получает список пользователей, связанных с проектом.
     */
    suspend fun getUsersFromProject(projectName: String): List<UserToProjectEntity>

    /**
     * Получает список проектов, связанных с указанным пользоватем.
     */
    suspend fun getProjectsForUser(chatId: Long): List<UserToProjectEntity>

    /**
     * Удаляет пользователя из проекта.
     */
    suspend fun deleteUserFromProject(chatId: Long, projectName: String)

    /**
     * Удаляет проект.
     */
    suspend fun deleteProject(projectName: String)

    /**
     * Устанавливает новый статус для пользователя в проекте.
     */
    suspend fun setUserStatusToProject(chatId: Long, projectName: String, newStatus: String)


    /**
     * Получает информацию пользователе в проекте.
     */
    suspend fun getUserFromProject(chatId: Long, projectName: String): UserToProjectEntity?

}