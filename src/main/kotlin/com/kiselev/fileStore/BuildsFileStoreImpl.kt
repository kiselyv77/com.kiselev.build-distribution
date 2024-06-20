package com.kiselev.fileStore

import com.kiselev.bot.constants.BotMessagesConstants.BUILD
import com.kiselev.bot.constants.BotMessagesConstants.BUILDS
import com.kiselev.bot.constants.BotMessagesConstants.BUILD_NOT_FOUND
import com.kiselev.bot.messaging.textMessaging.configureBuildMessage
import com.kiselev.utils.AppConstants.BUILDS_DIR
import com.kiselev.utils.AppConstants.DATE_TIME_FORMAT
import com.kiselev.utils.AppConstants.DATE_TIME_FORMAT_FOR_DISPLAY
import com.kiselev.utils.AppConstants.EXTENSIONS_LIST
import com.kiselev.utils.AppConstants.ROOT_PACKAGE
import com.kiselev.utils.findFileByExtensions
import com.kiselev.utils.toDataString
import com.kiselev.utils.toDateTime
import org.koin.core.component.KoinComponent
import java.io.File

class BuildsFileStoreImpl : BuildsFileStore, KoinComponent {

    override fun getLastBuild(projectName: String): String {
        val appDir = File(BUILDS_DIR, projectName)
        val directories = appDir.listFiles()?.toList()?.filter { it.isDirectory }?.sortedBy {
            it.name.toDateTime(DATE_TIME_FORMAT)
        }
        val apkDirectory = directories?.lastOrNull()
        val apkFile = apkDirectory?.findFileByExtensions(EXTENSIONS_LIST)
        return if (apkDirectory != null && apkFile != null) {
            val dateForDisplay = apkDirectory.name.toDateTime(DATE_TIME_FORMAT)?.toDataString(
                DATE_TIME_FORMAT_FOR_DISPLAY
            )
            val textMessage = configureBuildMessage(
                title = BUILD,
                time = dateForDisplay,
                projectName = projectName,
                apkDir = apkDirectory.name,
                apkName = apkFile.name,
            )
            textMessage
        } else {
            BUILD_NOT_FOUND
        }
    }

    override fun getRecentlyBuilds(projectName: String): String {
        val message = StringBuilder()
        message.append("$BUILDS $projectName")
        message.append("\n \n")
        val appDir = File(BUILDS_DIR, projectName)
        appDir.listFiles()?.toList()?.filter { it.isDirectory }?.sortedBy {
            it.name.toDateTime(DATE_TIME_FORMAT)
        }?.takeLast(10)?.map { file ->
            val apkDirectoryName = file?.name
            val apkName = file?.findFileByExtensions(EXTENSIONS_LIST)

            if (apkDirectoryName != null && apkName != null) {
                val dateForDisplay = apkDirectoryName.toDateTime(DATE_TIME_FORMAT)?.toDataString(
                    DATE_TIME_FORMAT_FOR_DISPLAY
                )
                val textMessage = configureBuildMessage(
                    title = BUILD,
                    time = dateForDisplay,
                    projectName = projectName,
                    apkDir = apkDirectoryName,
                    apkName = apkName.name,
                )
                message.append(textMessage)
                message.append("\n \n")
            }
        }
        return message.toString()
    }

    override fun clearOldBuilds(projectName: String?): String {
        val report = StringBuilder()
        report.append("------------------\n")
        report.append(projectName)
        report.append("\n")
        val appDirPath =
            "$ROOT_PACKAGE/$projectName/"
        val appPackage = File("$appDirPath/")
        val names = appPackage.listFiles()?.toList()?.filter { it.isDirectory }?.sortedBy {
            it.name.toDateTime(DATE_TIME_FORMAT)
        }
        val countForDelete = (names?.size ?: 0) / 2
        names?.take(countForDelete)?.forEach { buildPackage ->
            val result = buildPackage.deleteRecursively()
            report.append("file:${buildPackage.name} delete: $result\n")
        }
        return report.toString()
    }


}