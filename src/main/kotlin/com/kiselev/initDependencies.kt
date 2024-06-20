package com.kiselev

import com.kiselev.bot.TelegramBot
import com.kiselev.bot.TelegramBotContract
import com.kiselev.bot.controller.BotController
import com.kiselev.bot.controller.BotControllerImpl
import com.kiselev.dataBase.DataBaseModule
import com.kiselev.dataBase.provider.DatabaseProvider
import com.kiselev.dataBase.provider.DatabaseProviderContract
import com.kiselev.fileStore.BuildsFileStore
import com.kiselev.fileStore.BuildsFileStoreImpl
import com.kiselev.modules.ModulesModule
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

/**
 * Метод для инициализации всех необходимых зависимостей.
 */
fun Application.initDependencies() {
    module {
        install(Koin) {
            modules(
                module {
                    single<TelegramBotContract> { TelegramBot() }
                    single<DatabaseProviderContract> { DatabaseProvider() }
                    single<BuildsFileStore> { BuildsFileStoreImpl() }
                    single<com.kiselev.gitLab.GitLabApi> { com.kiselev.gitLab.GitLabApiImpl() }
                    single<BotController> { BotControllerImpl() }
                },
                ModulesModule.module,
                DataBaseModule.module
            )
        }
    }
}