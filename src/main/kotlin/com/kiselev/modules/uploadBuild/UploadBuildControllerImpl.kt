package com.kiselev.modules.uploadBuild

import com.kiselev.bot.TelegramBotContract
import com.kiselev.bot.constants.BotMessagesConstants.MEMORY_AUTO_CLEARING
import com.kiselev.bot.constants.BotMessagesConstants.NEW_BUILD
import com.kiselev.bot.constants.BotMessagesConstants.RESENDING
import com.kiselev.bot.messaging.textMessaging.configureBuildMessage
import com.kiselev.fileStore.BuildsFileStore
import com.kiselev.modules.BaseController
import com.kiselev.utils.*
import com.kiselev.utils.AppConstants.DATE_TIME_FORMAT
import com.kiselev.utils.AppConstants.DATE_TIME_FORMAT_FOR_DISPLAY
import com.kiselev.utils.AppConstants.EXTENSIONS_LIST
import com.kiselev.utils.AppConstants.ROOT_PACKAGE
import io.ktor.http.content.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File
import java.io.IOException

class UploadBuildControllerImpl : BaseController(), UploadController, KoinComponent {
    private val telegramBot by inject<TelegramBotContract>()
    private val buildStore by inject<BuildsFileStore>()

    override suspend fun saveFiles(
        files: List<PartData.FileItem>,
        projectName: String?
    ) {
        val dateTime = getCurrentDateTime()
        val dateTimeString = dateTime.toDataString(pattern = DATE_TIME_FORMAT)
        val dateTimeStringDisplay =
            dateTime.toDataString(pattern = DATE_TIME_FORMAT_FOR_DISPLAY)
        val appDirPath = "$ROOT_PACKAGE/$projectName/$dateTimeString"

        files.forEach { filePart ->
            val fileName = filePart.originalFileName
            val file = File("$appDirPath/$fileName")
            val freeSpace = getFreeSpace().bytesToMb()
            try {
                file.saveFileFromPart(filePart)
            } catch (e: IOException) {
                e.printStackTrace()
                if (freeSpace < MIN_FREE_SPACE) {
                    telegramBot.sendTextToTestUser(MEMORY_AUTO_CLEARING)
                    val report = buildStore.clearOldBuilds(projectName)
                    telegramBot.sendTextToTestUser("Отчет: $report")
                    telegramBot.sendTextToTestUser(RESENDING)
                    file.saveFileFromPart(filePart)
                }
            }
        }
        val apkFile = File(appDirPath).findFileByExtensions(EXTENSIONS_LIST)

        if (apkFile != null) {
            val textMessage = configureBuildMessage(
                title = NEW_BUILD,
                time = dateTimeStringDisplay,
                projectName = projectName,
                apkDir = dateTimeString,
                apkName = apkFile.name,
            )
            if (projectName != null) telegramBot.sendTextToUsersFromProject(textMessage, projectName)
        }
    }

    private fun File.saveFileFromPart(filePart: PartData.FileItem) {
        filePart.streamProvider().use { its ->
            if (!this.parentFile.exists()) {
                this.parentFile.mkdirs()
            }
            this.createNewFile()
            this.outputStream().buffered().use {
                its.copyTo(it)
            }
        }
        filePart.dispose()
    }

    companion object {
        private const val MIN_FREE_SPACE = 100L // Количсество памяти при котором необходимо удалить старые сборки
    }
}