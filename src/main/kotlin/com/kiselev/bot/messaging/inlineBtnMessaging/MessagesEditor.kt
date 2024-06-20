package com.kiselev.bot.messaging.inlineBtnMessaging

import com.kiselev.bot.constants.BotMessagesConstants.CHAT_ID_FORMAT
import com.kiselev.dataBase.entity.UserToProjectEntity
import com.kiselev.dataBase.entity.ProjectEntity
import com.kiselev.bot.constants.BotMessagesConstants.CHOOSE_ACTION_MESSAGE
import com.kiselev.bot.constants.BotMessagesConstants.CHOOSE_PROJECT_MESSAGE
import com.kiselev.bot.constants.BotMessagesConstants.CHOOSE_USERS_MESSAGE
import com.kiselev.bot.constants.BotMessagesConstants.DELETE_CONFIRM_MESSAGE
import com.kiselev.bot.constants.BotMessagesConstants.STATUS_FORMAT
import com.kiselev.bot.constants.BotMessagesConstants.USER_FORMAT
import dev.inmo.tgbotapi.bot.TelegramBot
import dev.inmo.tgbotapi.extensions.api.edit.edit
import dev.inmo.tgbotapi.extensions.api.edit.reply_markup.editMessageReplyMarkup
import dev.inmo.tgbotapi.extensions.utils.extensions.raw.message
import dev.inmo.tgbotapi.types.buttons.InlineKeyboardMarkup
import dev.inmo.tgbotapi.types.queries.callback.DataCallbackQuery
import dev.inmo.tgbotapi.utils.RiskFeature

suspend fun TelegramBot.visibleRootMassage(
    projects: List<UserToProjectEntity>,
    callbackQuery: DataCallbackQuery
) {
    setTitleAndButtons(
        title = CHOOSE_PROJECT_MESSAGE,
        callbackQuery = callbackQuery,
        markup = getProjectsListButtons(projects)
    )
}

suspend fun TelegramBot.visibleProjectMassage(
    project: ProjectEntity,
    userStatus: String,
    callbackQuery: DataCallbackQuery
) {
    setTitleAndButtons(
        title = CHOOSE_ACTION_MESSAGE,
        callbackQuery = callbackQuery,
        markup = getProjectActionsButtons(project, userStatus)
    )
}

suspend fun TelegramBot.visibleUsersMassage(
    users: List<UserToProjectEntity>,
    projectName: String,
    callbackQuery: DataCallbackQuery
) {
    setTitleAndButtons(
        title = CHOOSE_USERS_MESSAGE.format(projectName),
        callbackQuery = callbackQuery,
        markup = getUsersButtons(users.sortedBy { it.projectName }, projectName)
    )
}

suspend fun TelegramBot.visibleUserActionsMessage(
    userToProject: UserToProjectEntity,
    callbackQuery: DataCallbackQuery
) {
    setTitleAndButtons(
        title = "$USER_FORMAT ${userToProject.userName}\n" +
                "$CHAT_ID_FORMAT ${userToProject.chatId}\n" +
                "$STATUS_FORMAT ${userToProject.userStatusToProject}",
        callbackQuery = callbackQuery,
        markup = getUserActionsButtons(userToProject)
    )
}

suspend fun TelegramBot.visibleDeleteProjectDialog(
    projectName: String,
    callbackQuery: DataCallbackQuery
) {
    setTitleAndButtons(
        title = DELETE_CONFIRM_MESSAGE,
        callbackQuery = callbackQuery,
        markup = getProjectDeleteConfirmButtons(projectName)
    )
}

@OptIn(RiskFeature::class)
suspend fun TelegramBot.setTitleAndButtons(
    title: String,
    callbackQuery: DataCallbackQuery,
    markup: InlineKeyboardMarkup?
) {
    val message = callbackQuery.message
    val chatId = message?.chat?.id
    val messageId = message?.messageId
    if (chatId == null || messageId == null) return
    edit(
        chatId,
        messageId,
        title
    )
    editMessageReplyMarkup(
        chatId,
        messageId,
        markup
    )
}