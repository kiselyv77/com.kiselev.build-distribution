package com.kiselev.dataBase.dao

import com.kiselev.dataBase.entity.UserToProjectEntity

interface UserToProjectDao {

    /**
     * Вставляет пользователя в проект с указанным статусом.
     *
     * @param chatId Идентификатор чата.
     * @param userName Имя пользователя.
     * @param projectName Название проекта.
     * @param status Статус пользователя в проекте.
     */
    fun insertUserToProject(chatId: Long, userName: String, projectName: String, status: String)

    /**
     * Получает список пользователей из проекта.
     *
     * @param projectName Название проекта.
     */
    fun getUsersFromProjects(projectName: String): List<UserToProjectEntity>

    /**
     * Получает список проектов для указанного пользователя.
     *
     * @param chatId Идентификатор чата.
     * */
    fun getProjectsForUser(chatId: Long): List<UserToProjectEntity>

    /**
     * Устанавливает новый статус для пользователя в проекте.
     *
     * @param chatId Идентификатор чата.
     * @param projectName Название проекта.
     * @param newStatus Новый статус чата в проекте.
     */
    fun setUserStatusToProject(chatId: Long, projectName: String, newStatus: String)

    /**
     * Удаляет всех пользователей из проекта.
     *
     * @param projectName Название проекта.
     */
    fun deleteAllUsersFromProject(projectName: String)

    /**
     * Удаляет пользователя из всех проектов.
     *
     * @param chatId Идентификатор чата.
     */
    fun deleteUserFromAllProject(chatId: Long)

    /**
     * Удаляет пользователя из указанного проекта.
     *
     * @param chatId Идентификатор чата.
     * @param projectName Название проекта.
     */
    fun deleteUserFromProject(chatId: Long, projectName: String)

    /**
     * Получает пользователя из проекта.
     *
     * @param chatId Идентификатор чата.
     * @param projectName Название проекта.
     */
    fun getUserFromProject(chatId: Long, projectName: String): UserToProjectEntity?
}