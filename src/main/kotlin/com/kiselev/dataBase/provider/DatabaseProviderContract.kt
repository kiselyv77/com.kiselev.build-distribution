package com.kiselev.dataBase.provider

interface DatabaseProviderContract {
    fun init()
    suspend fun <T> dbQuery(block: () -> T): T
}