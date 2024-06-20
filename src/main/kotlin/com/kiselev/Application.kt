package com.kiselev

import com.kiselev.bot.TelegramBotContract
import com.kiselev.dataBase.provider.DatabaseProviderContract
import com.kiselev.modules.downloadFile.downloadFileModule
import com.kiselev.modules.showTextFile.showTextFileModule
import com.kiselev.modules.uploadBuild.uploadBuildModule
import com.kiselev.plugins.configureSerialization
import com.kiselev.plugins.configureSockets
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun main(args: Array<String>): Unit = EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    initDependencies()

    val databaseProvider by inject<DatabaseProviderContract>()
    val telegramBot by inject<TelegramBotContract>()
    //подключение базы данных
    databaseProvider.init()
    // Запуск бота
    telegramBot.startBot()

    install(Routing) {
        downloadFileModule()
        showTextFileModule()
        uploadBuildModule()
    }

    configureSockets()
    configureSerialization()
}
