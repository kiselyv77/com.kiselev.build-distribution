package com.kiselev.dataBase.entity

/**
 * Модель связи пользователь в проекте.
 * @param projectName имя проекта
 * @param chatId id чата из Telegram (также уникальный айди пользователя)
 * @param userName имя пользователя в проекте
 * @param userStatusToProject статус пользователя в проекте
 */
data class UserToProjectEntity(
    val projectName: String,
    val chatId: Long,
    val userName: String,
    val userStatusToProject: String
)
