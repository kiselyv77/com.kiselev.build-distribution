package com.kiselev.dataBase.provider

import com.kiselev.dataBase.tables.UserToProjectTable
import com.kiselev.dataBase.tables.ProjectTable
import com.kiselev.utils.DbConstants
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.KoinComponent
import java.util.concurrent.Executors

class DatabaseProvider : DatabaseProviderContract, KoinComponent {
    private val dispatcher = Executors.newFixedThreadPool(5).asCoroutineDispatcher()
    override fun init() {
        Database.connect(
            user = DbConstants.DB_USERNAME,
            url = DbConstants.DB_URI,
            password = DbConstants.DB_PASSWORD,
            driver = "org.postgresql.Driver"
        )
        transaction {
            SchemaUtils.create(ProjectTable, UserToProjectTable)
        }
    }

    override suspend fun <T> dbQuery(block: () -> T): T = withContext(dispatcher) {
        transaction { block() }
    }
}