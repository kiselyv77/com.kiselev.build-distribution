package com.kiselev.modules.showTextFile

import com.kiselev.modules.RequestsKeys.PROJECT_NAME_KEY
import com.kiselev.modules.RequestsKeys.DATE_TIME_KEY
import com.kiselev.modules.RequestsKeys.FILE_NAME_KEY
import com.kiselev.utils.AppConstants.SHOW_TEXT_PATH
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.showTextFileModule() {
    val controller by inject<ShowTextFileController>()
    get("/$SHOW_TEXT_PATH/{$PROJECT_NAME_KEY}/{$DATE_TIME_KEY}/{$FILE_NAME_KEY}") {
        val projectName = call.parameters[PROJECT_NAME_KEY]
        val dateTime = call.parameters[DATE_TIME_KEY]
        val fileName = call.parameters[FILE_NAME_KEY]
        val file = controller.getFile(
            projectName = projectName,
            dateTime = dateTime,
            fileName = fileName
        )
        val text = file.readText()
        if (text.isEmpty()) {
            call.respondText("Для этой сборки нет информации по последним изменениям", ContentType.Text.Plain)
        } else {
            call.respondText(text, ContentType.Text.Plain)
        }
    }
}