package com.kiselev.modules.downloadFile

import com.kiselev.modules.BaseController
import com.kiselev.utils.AppConstants.ROOT_PACKAGE
import org.koin.core.component.KoinComponent
import java.io.File

class DownloadFileControllerImpl : BaseController(), DownloadFileController, KoinComponent {
    override fun getFile(projectName: String?, dateTime: String?, fileName: String?): File {
        return File("$ROOT_PACKAGE/$projectName/$dateTime/$fileName")
    }
}