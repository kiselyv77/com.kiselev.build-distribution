package com.kiselev.modules

import com.kiselev.modules.downloadFile.DownloadFileController
import com.kiselev.modules.downloadFile.DownloadFileControllerImpl
import com.kiselev.modules.showTextFile.ShowTextFileController
import com.kiselev.modules.showTextFile.ShowTextFileControllerImpl
import com.kiselev.modules.uploadBuild.UploadBuildControllerImpl
import com.kiselev.modules.uploadBuild.UploadController
import org.koin.dsl.module

object ModulesModule {
    val module = module {
        single<DownloadFileController> { DownloadFileControllerImpl() }
        single<ShowTextFileController> { ShowTextFileControllerImpl() }
        single<UploadController> { UploadBuildControllerImpl() }
    }
}