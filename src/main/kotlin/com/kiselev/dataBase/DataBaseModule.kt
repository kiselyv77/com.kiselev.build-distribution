package com.kiselev.dataBase

import com.kiselev.dataBase.dao.UserToProjectDao
import com.kiselev.dataBase.dao.ProjectDao
import com.kiselev.dataBase.tables.UserToProjectTable
import com.kiselev.dataBase.tables.ProjectTable
import org.koin.dsl.module

object DataBaseModule {
    val module = module {
        single<ProjectDao> { ProjectTable }
        single<UserToProjectDao> { UserToProjectTable }
    }
}