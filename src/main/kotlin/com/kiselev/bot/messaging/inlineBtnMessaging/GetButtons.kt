package com.kiselev.bot.messaging.inlineBtnMessaging

import com.kiselev.bot.messaging.inlineBtnMessaging.BotCallBack.*
import com.kiselev.dataBase.entity.UserToProjectEntity
import com.kiselev.dataBase.entity.ProjectEntity
import com.kiselev.dataBase.types.UserStatusInProject
import dev.inmo.tgbotapi.types.buttons.InlineKeyboardButtons.CallbackDataInlineKeyboardButton
import dev.inmo.tgbotapi.types.buttons.InlineKeyboardMarkup
import dev.inmo.tgbotapi.utils.matrix
import dev.inmo.tgbotapi.utils.row

fun getProjectsListButtons(userToProjectDao: List<UserToProjectEntity>): InlineKeyboardMarkup {
    return InlineKeyboardMarkup(
        keyboard = matrix {
            userToProjectDao.forEach { userToProject ->
                +CallbackDataInlineKeyboardButton(
                    text = userToProject.projectName,
                    callbackData = "${OPEN_PROJECT.name}:${userToProject.projectName}"
                )
            }
            row {
                +CallbackDataInlineKeyboardButton(
                    text = CREATE_PROJECT.getDisplayText(),
                    callbackData = "${CREATE_PROJECT.name}:"
                )
                +CallbackDataInlineKeyboardButton(
                    text = SUBSCRIBE_TO_PROJECT.getDisplayText(),
                    callbackData = "${SUBSCRIBE_TO_PROJECT.name}:"
                )
            }
            +CallbackDataInlineKeyboardButton(
                text = GET_INSTRUCTION.getDisplayText(),
                callbackData = "${GET_INSTRUCTION.name}:"
            )
        }
    )
}

fun getProjectActionsButtons(projectEntity: ProjectEntity, userStatus: String): InlineKeyboardMarkup {
    val projectName = projectEntity.name
    return InlineKeyboardMarkup(
        keyboard = matrix {
            +CallbackDataInlineKeyboardButton(
                text = GET_LAST_BUILD.getDisplayText().format(projectName),
                callbackData = "$GET_LAST_BUILD:$projectName"
            )
            +CallbackDataInlineKeyboardButton(
                text = GET_RECENTLY_BUILDS.getDisplayText().format(projectName),
                callbackData = "$GET_RECENTLY_BUILDS:$projectName"
            )
            +CallbackDataInlineKeyboardButton(
                text = START_BUILD.getDisplayText().format(projectName),
                callbackData = "$START_BUILD:$projectName"
            )

            if (UserStatusInProject.from(userStatus) == UserStatusInProject.ADMIN) {
                +CallbackDataInlineKeyboardButton(
                    text = DELETE_PROJECT.getDisplayText().format(projectName),
                    callbackData = "$DELETE_PROJECT:$projectName"
                )
                +CallbackDataInlineKeyboardButton(
                    text = GET_CHATS_FROM_PROJECT.getDisplayText().format(projectName),
                    callbackData = "$GET_CHATS_FROM_PROJECT:$projectName"
                )
                +CallbackDataInlineKeyboardButton(
                    text = CLEAR_OLD_BUILDS.getDisplayText().format(projectName),
                    callbackData = "$CLEAR_OLD_BUILDS:$projectName"
                )
            }

            +CallbackDataInlineKeyboardButton(
                text = BACK.getDisplayText(),
                callbackData = BACK.name
            )
        }
    )
}

fun getProjectDeleteConfirmButtons(projectName: String): InlineKeyboardMarkup {
    return InlineKeyboardMarkup(
        keyboard = matrix {
            row {
                +CallbackDataInlineKeyboardButton(
                    text = DELETE_PROJECT_CONFIRM.getDisplayText(),
                    callbackData = "$DELETE_PROJECT_CONFIRM:$projectName"
                )
                +CallbackDataInlineKeyboardButton(
                    text = DELETE_PROJECT_CANCEL.getDisplayText(),
                    callbackData = "$DELETE_PROJECT_CANCEL:$projectName"
                )
            }
        }
    )
}


fun getUsersButtons(usersToProject: List<UserToProjectEntity>, projectName: String): InlineKeyboardMarkup {
    return InlineKeyboardMarkup(
        keyboard = matrix {
            usersToProject.forEach { userToProject ->
                +CallbackDataInlineKeyboardButton(
                    text = userToProject.userName + " - " + userToProject.userStatusToProject,
                    callbackData = "${OPEN_USER}:${projectName}:${userToProject.chatId}"
                )
            }
            +CallbackDataInlineKeyboardButton(
                text = BACK.getDisplayText(),
                callbackData = "${OPEN_PROJECT.name}:${projectName}"
            )
        }
    )
}

fun getUserActionsButtons(userToProject: UserToProjectEntity): InlineKeyboardMarkup {
    return InlineKeyboardMarkup(
        keyboard = matrix {
            +CallbackDataInlineKeyboardButton(
                text = DELETE_USER.getDisplayText(),
                callbackData = "$DELETE_USER:${userToProject.projectName}:${userToProject.chatId}"
            )

            val currentStatus = UserStatusInProject.from(userToProject.userStatusToProject)
            if (currentStatus != UserStatusInProject.CREATOR && currentStatus != null) {
                +CallbackDataInlineKeyboardButton(
                    text = if (currentStatus == UserStatusInProject.USER)
                        "Сделать администратором"
                    else
                        "Сделать обычным пользователем",
                    callbackData = "${SET_USER_STATUS}:${userToProject.projectName}:${userToProject.chatId}"
                )
            }
            +CallbackDataInlineKeyboardButton(
                text = BACK.getDisplayText(),
                callbackData = "$GET_CHATS_FROM_PROJECT:${userToProject.projectName}"
            )
        }
    )
}