package com.kiselev.modules.downloadFile

import com.kiselev.modules.RequestsKeys.PROJECT_NAME_KEY
import com.kiselev.modules.RequestsKeys.DATE_TIME_KEY
import com.kiselev.modules.RequestsKeys.FILE_NAME_KEY
import com.kiselev.utils.AppConstants.APKS_PATH
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.downloadFileModule() {
    val controller by inject<DownloadFileController>()
    get("/$APKS_PATH/{$PROJECT_NAME_KEY}/{$DATE_TIME_KEY}/{$FILE_NAME_KEY}") {
        val projectName = call.parameters[PROJECT_NAME_KEY]
        val dateTime = call.parameters[DATE_TIME_KEY]
        val fileName = call.parameters[FILE_NAME_KEY]
        val file = controller.getFile(
            projectName = projectName,
            dateTime = dateTime,
            fileName = fileName
        )
        call.response.header(
            "Content-Disposition",
            "attachment; filename=\"${file.name}\""
        )
        call.respondFile(file)
    }
}