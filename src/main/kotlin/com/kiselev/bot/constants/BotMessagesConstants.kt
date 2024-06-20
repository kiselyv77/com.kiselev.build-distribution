package com.kiselev.bot.constants

import com.kiselev.bot.constants.BotCommands.CREATE_PROJECT_COMMAND
import com.kiselev.bot.constants.BotCommands.SUBSCRIBE_TO_PROJECT_COMMAND
import com.kiselev.bot.constants.BotCommands.UNSUBSCRIBE_FROM_PROJECT_COMMAND
import com.kiselev.utils.AppConstants.BASE_URL

object BotMessagesConstants {
    const val BUILD = "Сборка"
    const val BUILDS = "Сборки"
    const val BUILD_NOT_FOUND = "Сборок не найдено"
    const val NEW_BUILD = "Новая сборка"
    const val RELEASE_NOTES = "Информация по последним изменениям:"

    const val CREATE_PROJECT_INSTRUCTION = "Для того чтобы создать новый проект введите его имя и данные из гитлаба." +
            "Данные из гитлаба не являются обязательными и нужны для запуска пейплайнов из бота. Пример:\n" +
            "/$CREATE_PROJECT_COMMAND\nИмя проекта - обязательное поле\n" +
            "Ваше имя в этом проекте - обязательное поле\n" +
            "Ссылка на гит-лаб - необязательное поле\n" +
            "Триггер токен - необязательное поле\n" +
            "Id проекта в гитлабе - необязательное поле\n"

    const val SUBSCRIBE_PROJECT_INSTRUCTION =
        "Для того чтобы подписаться на рассылку сборок введите токен доступа который вам выдал администратор и свое уникальное имя." +
                "Пример:\n" +
                "/$SUBSCRIBE_TO_PROJECT_COMMAND\n'ТОКЕН'\n'Ваше имя в этом проекте'"

    const val UNSUBSCRIBE_PROJECT_INSTRUCTION =
        "Для того чтобы отписаться от рассылки сборок введите имя проекта." +
                "Пример:\n" +
                "/$UNSUBSCRIBE_FROM_PROJECT_COMMAND\n'Имя проекта'"

    const val CREATE_PROJECT_ERROR_MESSAGE =
        "Произошла ошибка при создании проекта, возможно проект с таким именем уже сущевствует\n"

    const val EMPTY_NAME_ERROR = "Имя проекта не может быть пустым"

    const val CHOOSE_PROJECT_MESSAGE = "Выберите проект"

    const val CHOOSE_ACTION_MESSAGE = "Выберите действие"

    const val CHOOSE_USERS_MESSAGE = "Пользователи %s"

    const val DELETE_CONFIRM_MESSAGE =
        "Вы действительно хотите удалить проект? Отменить это действие будет не возможно!"

    const val MEMORY_AUTO_CLEARING = "Автоматическая очистка памяти..."

    const val RESENDING = "Повторная отправка..."

    const val GIT_LAB_ID_ERROR = "Project Id из gitlab - должно быть числом!"

    const val ACCESS_TOKEN_ERROR = "Неверный токен!"

    const val INPUT_TOKEN = "Введите токен доступа!"

    const val INPUT_NAME = "Введите имя!"

    const val INPUT_PROJECT_NAME = "Введите имя проекта!"

    const val INPUT_GITLAB_DATA = "Заполните данные из гитлаба!"

    const val ERROR = "Упс.. Произошла ошибка:"

    const val PROJECT_NAME_FORMAT = "Имя проекта:"
    const val YOUR_NAME_FORMAT = "Ваше имя:"
    const val ACCESS_TOKEN_FORMAT = "Токен доступа:"
    const val GIT_LAB_URL_FORMAT = "Gitlab Url:"
    const val GIT_LAB_TRIGGER_TOKEN_FORMAT = "Gitlab Trigger Token:"
    const val GIT_ID_FORMAT = "Gitlab Id:"

    const val USER_FORMAT = "Пользователь:"
    const val CHAT_ID_FORMAT = "Идентификатор чата:"
    const val STATUS_FORMAT = "Статус:"

    const val GITLAB_INSTRUCTION_MSG =
        "Для использования бота необходимо после создания проекта в боте отправлять файлы соброк и changelog.txt (опционально) с GitLab на сервер" +
                "по запросу ${BASE_URL}uploadBuild. Так же в этом запросе необходимо передать имя указанное при создании проекта в боте"
    const val GITLAB_INSTRUCTION_UPLOAD_MSG = "Пример настройки задачи для выгрузки сборки в бота ⬇\uFE0F"
    const val GITLAB_INSTRUCTION_TRIGGER_TOKENS = "Пример настройки задачи для выгрузки сборки в бота ⬇\uFE0F"
}