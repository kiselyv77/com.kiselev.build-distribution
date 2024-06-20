package com.kiselev.bot.messaging.textMessaging

import com.kiselev.bot.constants.BotMessagesConstants.RELEASE_NOTES
import com.kiselev.utils.AppConstants
import com.kiselev.utils.AppConstants.APKS_PATH
import com.kiselev.utils.AppConstants.CHANGELOG_FILE_NAME
import com.kiselev.utils.AppConstants.SHOW_TEXT_PATH

fun configureBuildMessage(
    title: String,
    time: String?,
    projectName: String?,
    apkDir: String,
    apkName: String?,
): String {
    return StringBuilder().apply {
        append("$title $projectName от ")
        append(time)
        append("\n")
        append("${AppConstants.BASE_URL}/$APKS_PATH/$projectName/$apkDir/$apkName")
        append("\n")
        append(RELEASE_NOTES)
        append("\n")
        append("${AppConstants.BASE_URL}/$SHOW_TEXT_PATH/$projectName/$apkDir/$CHANGELOG_FILE_NAME")
    }.toString()
}