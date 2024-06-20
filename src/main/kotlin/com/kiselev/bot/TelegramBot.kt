package com.kiselev.bot

import com.kiselev.bot.constants.BotCommands.CREATE_PROJECT_COMMAND
import com.kiselev.bot.constants.BotCommands.HELP_COMMAND
import com.kiselev.bot.constants.BotCommands.SUBSCRIBE_TO_PROJECT_COMMAND
import com.kiselev.bot.constants.BotCommands.UNSUBSCRIBE_FROM_PROJECT_COMMAND
import com.kiselev.bot.constants.BotMessagesConstants.ACCESS_TOKEN_ERROR
import com.kiselev.bot.constants.BotMessagesConstants.ACCESS_TOKEN_FORMAT
import com.kiselev.bot.constants.BotMessagesConstants.BUILD
import com.kiselev.bot.constants.BotMessagesConstants.CHOOSE_PROJECT_MESSAGE
import com.kiselev.bot.constants.BotMessagesConstants.CREATE_PROJECT_ERROR_MESSAGE
import com.kiselev.bot.constants.BotMessagesConstants.CREATE_PROJECT_INSTRUCTION
import com.kiselev.bot.constants.BotMessagesConstants.EMPTY_NAME_ERROR
import com.kiselev.bot.constants.BotMessagesConstants.ERROR
import com.kiselev.bot.constants.BotMessagesConstants.GITLAB_INSTRUCTION_MSG
import com.kiselev.bot.constants.BotMessagesConstants.GITLAB_INSTRUCTION_TRIGGER_TOKENS
import com.kiselev.bot.constants.BotMessagesConstants.GITLAB_INSTRUCTION_UPLOAD_MSG
import com.kiselev.bot.constants.BotMessagesConstants.GIT_ID_FORMAT
import com.kiselev.bot.constants.BotMessagesConstants.GIT_LAB_ID_ERROR
import com.kiselev.bot.constants.BotMessagesConstants.GIT_LAB_TRIGGER_TOKEN_FORMAT
import com.kiselev.bot.constants.BotMessagesConstants.GIT_LAB_URL_FORMAT
import com.kiselev.bot.constants.BotMessagesConstants.INPUT_GITLAB_DATA
import com.kiselev.bot.constants.BotMessagesConstants.INPUT_NAME
import com.kiselev.bot.constants.BotMessagesConstants.INPUT_PROJECT_NAME
import com.kiselev.bot.constants.BotMessagesConstants.INPUT_TOKEN
import com.kiselev.bot.constants.BotMessagesConstants.PROJECT_NAME_FORMAT
import com.kiselev.bot.constants.BotMessagesConstants.SUBSCRIBE_PROJECT_INSTRUCTION
import com.kiselev.bot.constants.BotMessagesConstants.UNSUBSCRIBE_PROJECT_INSTRUCTION
import com.kiselev.bot.constants.BotMessagesConstants.YOUR_NAME_FORMAT
import com.kiselev.bot.controller.BotController
import com.kiselev.bot.messaging.inlineBtnMessaging.*
import com.kiselev.bot.messaging.inlineBtnMessaging.BotCallBack.*
import com.kiselev.dataBase.tables.UserToProjectTable
import com.kiselev.dataBase.tables.ProjectTable
import com.kiselev.dataBase.types.UserStatusInProject
import com.kiselev.fileStore.BuildsFileStore
import com.kiselev.utils.AppConstants
import com.kiselev.utils.AppConstants.TG_API_KEY
import com.kiselev.utils.hashString
import dev.inmo.tgbotapi.bot.ktor.telegramBot
import dev.inmo.tgbotapi.extensions.api.answers.answerCallbackQuery
import dev.inmo.tgbotapi.extensions.api.send.sendMessage
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithFSMAndStartLongPolling
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.command
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.commandWithArgs
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onDataCallbackQuery
import dev.inmo.tgbotapi.extensions.utils.extensions.raw.message
import dev.inmo.tgbotapi.types.ChatId
import dev.inmo.tgbotapi.types.message.MarkdownV2ParseMode
import dev.inmo.tgbotapi.types.queries.callback.DataCallbackQuery
import dev.inmo.tgbotapi.utils.RiskFeature
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*
import java.util.concurrent.Executors

class TelegramBot : TelegramBotContract, KoinComponent {
    private val bot = telegramBot(TG_API_KEY)
    private val dispatcher = Executors.newFixedThreadPool(5).asCoroutineDispatcher()
    private val botCoroutineScope = CoroutineScope(dispatcher)
    private val fileStore by inject<BuildsFileStore>()
    private val gitLabApi by inject<com.kiselev.gitLab.GitLabApi>()
    private val controller by inject<BotController>()

    @OptIn(RiskFeature::class)
    override fun startBot() {
        botCoroutineScope.launch {
            bot.buildBehaviourWithFSMAndStartLongPolling<BotState> {

                commandWithArgs(CREATE_PROJECT_COMMAND) { message, arguments ->
                    if (arguments.isEmpty()) {
                        sendTextMessage(chatId = message.chat.id, text = CREATE_PROJECT_INSTRUCTION)
                        return@commandWithArgs
                    }

                    val args = arguments.joinToString(" ").split("\n")

                    val creatorChatId = message.chat.id.chatId

                    val projectName = args.getOrNull(0)
                    val userName = args.getOrNull(1)
                    val gitlabBaseUrl = args.getOrNull(2)?.ifEmpty { null }
                    val triggerToken = args.getOrNull(3)?.ifEmpty { null }
                    val gitlabId = args.getOrNull(4)?.ifEmpty { null }

                    val gitlabIdInt = gitlabId?.toIntOrNull()

                    if (gitlabId != null && gitlabIdInt == null) {
                        sendTextMessage(chatId = message.chat.id, text = GIT_LAB_ID_ERROR)
                        return@commandWithArgs
                    }

                    if (projectName.isNullOrEmpty()) {
                        sendTextMessage(chatId = message.chat.id, text = EMPTY_NAME_ERROR)
                        return@commandWithArgs
                    }

                    if (userName.isNullOrEmpty()) {
                        sendTextMessage(chatId = message.chat.id, text = INPUT_NAME)
                        return@commandWithArgs
                    }

                    val accessTokenPublic = UUID.randomUUID().toString() // отправляю создателю
                    val accessTokenPrivate = hashString(accessTokenPublic)

                    try {
                        val project = controller.createProject(
                            projectName = projectName,
                            userName = userName,
                            accessTokenPrivate = accessTokenPrivate,
                            gitlabBaseUrl = gitlabBaseUrl,
                            triggerToken = triggerToken,
                            gitlabIdInt = gitlabIdInt,
                            creatorChatId = creatorChatId
                        )
                        val messageForRespond = StringBuilder()
                        messageForRespond.append("$PROJECT_NAME_FORMAT ${project?.name}\n")
                        messageForRespond.append("$YOUR_NAME_FORMAT ${userName}\n")
                        messageForRespond.append("$ACCESS_TOKEN_FORMAT $accessTokenPublic\n")
                        messageForRespond.append("$GIT_LAB_URL_FORMAT ${project?.gitLabBaseUrl}\n")
                        messageForRespond.append("$GIT_LAB_TRIGGER_TOKEN_FORMAT ${project?.triggerToken}\n")
                        messageForRespond.append("$GIT_ID_FORMAT ${project?.gitlabId}")
                        sendTextMessage(chatId = message.chat.id, text = messageForRespond.toString())
                    } catch (e: Exception) {
                        sendTextMessage(
                            chatId = message.chat.id,
                            text = CREATE_PROJECT_ERROR_MESSAGE + e.localizedMessage
                        )
                    }
                }

                commandWithArgs(SUBSCRIBE_TO_PROJECT_COMMAND) { message, arguments ->
                    if (arguments.isEmpty()) {
                        sendTextMessage(chatId = message.chat.id, text = SUBSCRIBE_PROJECT_INSTRUCTION)
                        return@commandWithArgs
                    }

                    val args = arguments.joinToString(" ").split("\n")
                    val accessTokenPublic = args.getOrNull(0)
                    val userName = args.getOrNull(1)
                    if (accessTokenPublic.isNullOrEmpty()) {
                        sendTextMessage(chatId = message.chat.id, text = INPUT_TOKEN)
                        return@commandWithArgs
                    }
                    if (userName.isNullOrEmpty()) {
                        sendTextMessage(chatId = message.chat.id, text = INPUT_NAME)
                        return@commandWithArgs
                    }
                    val accessToken = hashString(accessTokenPublic)

                    val project = ProjectTable.getProjectByAccessToken(accessToken)
                    if (project == null) {
                        sendTextMessage(chatId = message.chat.id, text = ACCESS_TOKEN_ERROR)
                        return@commandWithArgs
                    }

                    try {
                        controller.addUserToProject(
                            chatId = message.chat.id.chatId,
                            projectName = project.name,
                            userName = userName,
                            status = UserStatusInProject.USER
                        )
                    } catch (e: Exception) {
                        sendTextMessage(chatId = message.chat.id, text = e.message.toString())
                    }
                }

                commandWithArgs(UNSUBSCRIBE_FROM_PROJECT_COMMAND) { message, arguments ->
                    if (arguments.isEmpty()) {
                        sendTextMessage(chatId = message.chat.id, text = UNSUBSCRIBE_PROJECT_INSTRUCTION)
                        return@commandWithArgs
                    }

                    val args = arguments.joinToString(" ").split("\n")
                    val name = args.getOrNull(0)
                    if (name.isNullOrEmpty()) {
                        sendTextMessage(chatId = message.chat.id, text = INPUT_PROJECT_NAME)
                        return@commandWithArgs
                    }

                    try {
                        controller.deleteUserFromProject(chatId = message.chat.id.chatId, projectName = name)
                    } catch (e: Exception) {
                        sendTextMessage(chatId = message.chat.id, text = e.message.toString())
                    }
                }

                command(HELP_COMMAND) {
                    bot.sendMessage(
                        chatId = it.chat.id,
                        text = CHOOSE_PROJECT_MESSAGE,
                        replyMarkup =
                        getProjectsListButtons(
                            UserToProjectTable.getProjectsForUser(it.chat.id.chatId)
                        )
                    )
                }

                onDataCallbackQuery { callback: DataCallbackQuery ->
                    val chatId = callback.message?.chat?.id
                    val args = callback.data.split(":")
                    val callbackCommandStr = args.getOrNull(0)
                    if (callbackCommandStr.isNullOrEmpty()) return@onDataCallbackQuery
                    val callbackCommand = BotCallBack.from(callbackCommandStr)
                    if (chatId == null) return@onDataCallbackQuery

                    val projectName = args.getOrNull(1)
                    val project = if (projectName != null) controller.getProject(projectName) else null

                    val chatStatus = if (projectName != null)
                        controller.getUserFromProject(chatId.chatId, projectName)?.userStatusToProject
                            ?: UserStatusInProject.USER.name
                    else null

                    when (callbackCommand) {
                        OPEN_PROJECT -> {
                            if (project == null || chatStatus == null) return@onDataCallbackQuery
                            visibleProjectMassage(project = project, userStatus = chatStatus, callbackQuery = callback)
                        }

                        GET_LAST_BUILD -> {
                            if (project == null) return@onDataCallbackQuery
                            val messageForRespond = fileStore.getLastBuild(project.name)
                            sendTextMessage(chatId = chatId, text = messageForRespond)
                        }

                        GET_RECENTLY_BUILDS -> {
                            if (project == null) return@onDataCallbackQuery
                            val messageForRespond = fileStore.getRecentlyBuilds(project.name)
                            sendTextMessage(chatId = chatId, text = messageForRespond)
                        }

                        START_BUILD -> {
                            if (project == null) return@onDataCallbackQuery
                            if (project.triggerToken == null || project.gitlabId == null || project.gitLabBaseUrl == null) {
                                sendTextMessage(chatId = chatId, text = INPUT_GITLAB_DATA)
                                bot.answerCallbackQuery(callback)
                                return@onDataCallbackQuery
                            }
                            val result = gitLabApi.triggerPipeline(
                                projectId = project.gitlabId,
                                triggerToken = project.triggerToken,
                                refName = AppConstants.DEVELOP_REF,
                                gitLabBaseUrl = project.gitLabBaseUrl
                            )

                            val message =
                                if (result.success) "$BUILD $projectName успешно запустилась, ожидайте"
                                else "$ERROR ${result.message}"

                            sendTextMessage(chatId = chatId, text = message)
                        }

                        DELETE_PROJECT_CANCEL -> {
                            if (project == null || chatStatus == null) return@onDataCallbackQuery
                            visibleProjectMassage(project, chatStatus, callback)
                        }

                        DELETE_PROJECT -> {
                            if (projectName == null) return@onDataCallbackQuery
                            visibleDeleteProjectDialog(projectName, callback)
                        }

                        DELETE_PROJECT_CONFIRM -> {
                            if (projectName == null) return@onDataCallbackQuery
                            controller.deleteProject(projectName)
                            val projectsForUser = controller.getProjectsForUser(chatId.chatId)
                            visibleRootMassage(projectsForUser, callback)
                        }

                        CREATE_PROJECT -> {
                            sendTextMessage(chatId = chatId, text = CREATE_PROJECT_INSTRUCTION)
                        }

                        SUBSCRIBE_TO_PROJECT -> {
                            sendTextMessage(chatId = chatId, text = SUBSCRIBE_PROJECT_INSTRUCTION)
                        }

                        GET_CHATS_FROM_PROJECT -> {
                            if (projectName == null) return@onDataCallbackQuery
                            val chats = controller.getUsersFromProject(projectName)
                            visibleUsersMassage(chats, projectName, callback)
                        }

                        BACK -> {
                            val projectsForUser = UserToProjectTable.getProjectsForUser(chatId.chatId)
                            visibleRootMassage(projectsForUser, callback)
                        }

                        CLEAR_OLD_BUILDS -> {
                            val report = fileStore.clearOldBuilds(projectName)
                            bot.sendTextMessage(chatId, report)
                        }

                        GET_INSTRUCTION -> {
                            val instruction = GITLAB_INSTRUCTION_MSG
                            val uploadBuildSample =
                                javaClass.classLoader.getResource("uploadBuildSample.txt")?.readText()
                            val triggerSample = javaClass.classLoader.getResource("triggerSample.txt")?.readText()
                            bot.sendTextMessage(chatId, instruction)
                            bot.sendTextMessage(chatId, GITLAB_INSTRUCTION_UPLOAD_MSG)
                            bot.sendTextMessage(chatId, "```$uploadBuildSample```", parseMode = MarkdownV2ParseMode)
                            bot.sendTextMessage(chatId, GITLAB_INSTRUCTION_TRIGGER_TOKENS)
                            bot.sendTextMessage(chatId, "```$triggerSample```", parseMode = MarkdownV2ParseMode)
                        }

                        OPEN_USER -> {
                            if (projectName == null) return@onDataCallbackQuery
                            val chatIdForOpen = args.getOrNull(2)?.toLongOrNull() ?: return@onDataCallbackQuery
                            val userToProject = controller.getUserFromProject(chatId = chatIdForOpen, projectName = projectName)
                                ?: return@onDataCallbackQuery
                            visibleUserActionsMessage(userToProject, callback)
                        }

                        DELETE_USER -> {
                            if (projectName == null) return@onDataCallbackQuery
                            val chatIdForOpen = args.getOrNull(2)?.toLongOrNull() ?: return@onDataCallbackQuery
                            controller.deleteUserFromProject(chatId = chatIdForOpen, projectName = projectName)
                            val chats = controller.getUsersFromProject(projectName)
                            visibleUsersMassage(chats, projectName, callback)
                        }

                        SET_USER_STATUS -> {
                            if (projectName == null) return@onDataCallbackQuery
                            val chatIdForOpen = args.getOrNull(2)?.toLongOrNull() ?: return@onDataCallbackQuery
                            val currentStatus = controller.getUserFromProject(
                                chatId = chatIdForOpen,
                                projectName = projectName
                            )?.userStatusToProject
                            controller.setUserStatusToProject(
                                chatId = chatIdForOpen,
                                projectName = projectName,
                                newStatus = UserStatusInProject.setStatus(currentStatus)
                            )
                            val chats = controller.getUsersFromProject(projectName)
                            visibleUsersMassage(chats, projectName, callback)
                        }

                        null -> Unit

                    }
                    bot.answerCallbackQuery(callback)
                }

            }.join()
        }
    }

    override suspend fun sendTextToUsersFromProject(textMessage: String, projectName: String) {
        controller.getUsersFromProject(projectName).map { it.chatId }.forEach { userId ->
            bot.sendTextMessage(ChatId(userId), textMessage)
        }
    }

    override suspend fun sendTextToTestUser(textMessage: String) {
        val testUserId = TEST_USER
        bot.sendTextMessage(ChatId(testUserId), textMessage)
    }

    companion object {
        const val TEST_USER = 469563031L
    }
}