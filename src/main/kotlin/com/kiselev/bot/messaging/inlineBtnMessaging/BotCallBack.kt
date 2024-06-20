package com.kiselev.bot.messaging.inlineBtnMessaging

import com.kiselev.bot.messaging.inlineBtnMessaging.BotCallBack.*

enum class BotCallBack {
    OPEN_PROJECT,
    CREATE_PROJECT,
    SUBSCRIBE_TO_PROJECT,
    GET_LAST_BUILD,
    GET_RECENTLY_BUILDS,
    DELETE_PROJECT,
    DELETE_PROJECT_CONFIRM,
    DELETE_PROJECT_CANCEL,
    START_BUILD,
    GET_CHATS_FROM_PROJECT,
    CLEAR_OLD_BUILDS,
    GET_INSTRUCTION,
    DELETE_USER,
    OPEN_USER,
    SET_USER_STATUS,
    BACK;

    companion object {
        fun from(type: String?): BotCallBack? = entries.find { it.name == type }
    }
}

fun BotCallBack.getDisplayText() = when (this) {
    OPEN_PROJECT -> ""
    CREATE_PROJECT -> "Создать проект"
    SUBSCRIBE_TO_PROJECT -> "Подписаться на проект"
    GET_LAST_BUILD -> "Последняя сборка %s"
    GET_RECENTLY_BUILDS -> "Последние 10 сборок %s"
    DELETE_PROJECT -> "Удалить проект %s"
    DELETE_PROJECT_CONFIRM -> "Удалить"
    DELETE_PROJECT_CANCEL -> "Отмена"
    START_BUILD -> "Запустить сборку %s"
    GET_CHATS_FROM_PROJECT -> "Все пользователи %s"
    CLEAR_OLD_BUILDS -> "Удалить старые сборки %s"
    BACK -> "Назад"
    GET_INSTRUCTION -> "Инструкция по настройке GitLab"
    DELETE_USER -> "Удалить пользователя из проекта"
    OPEN_USER -> ""
    SET_USER_STATUS -> ""
}

