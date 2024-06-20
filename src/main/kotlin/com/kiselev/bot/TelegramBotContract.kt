package com.kiselev.bot

interface TelegramBotContract {
    /**
     * Запускает бота.
     */
    fun startBot()

    /**
     * Отправляет текстовое сообщение всем пользователям из указанного проекта.
     *
     * @param textMessage Текст сообщения.
     * @param projectName Название проекта.
     */
    suspend fun sendTextToUsersFromProject(textMessage: String, projectName: String)

    /**
     * Отправляет текстовое сообщение тестовому пользователю.
     *
     * @param textMessage Текст сообщения.
     */
    suspend fun sendTextToTestUser(textMessage: String)
}