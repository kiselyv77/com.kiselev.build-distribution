package com.kiselev.modules.uploadBuild

import com.kiselev.modules.RequestsKeys.PROJECT_NAME_KEY
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.uploadBuildModule() {
    val controller by inject<UploadController>()
    post("/uploadBuild") {
        val multipart = call.receiveMultipart()
        val parts = multipart.readAllParts()
        val parameters = parts.filterIsInstance<PartData.FormItem>()
        val files = parts.filterIsInstance<PartData.FileItem>()
        val projectName = parameters.firstOrNull { it.name == PROJECT_NAME_KEY }?.value
        controller.saveFiles(files, projectName)
    }
}