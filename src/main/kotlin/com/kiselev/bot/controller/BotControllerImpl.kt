package com.kiselev.bot.controller

import com.kiselev.dataBase.dao.UserToProjectDao
import com.kiselev.dataBase.dao.ProjectDao
import com.kiselev.dataBase.entity.UserToProjectEntity
import com.kiselev.dataBase.entity.ProjectEntity
import com.kiselev.dataBase.tables.UserToProjectTable
import com.kiselev.dataBase.types.UserStatusInProject
import com.kiselev.modules.BaseController
import org.koin.core.component.inject

class BotControllerImpl : BaseController(), BotController {

    private val projectDao by inject<ProjectDao>()
    private val usersToProjectDao by inject<UserToProjectDao>()

    override suspend fun createProject(
        projectName: String,
        userName: String,
        accessTokenPrivate: String,
        gitlabBaseUrl: String?,
        triggerToken: String?,
        gitlabIdInt: Int?,
        creatorChatId: Long
    ): ProjectEntity? = dbQuery {
        projectDao.insertProject(
            name = projectName,
            accessToken = accessTokenPrivate,
            gitlabBaseUrl = gitlabBaseUrl,
            triggerToken = triggerToken,
            gitlabId = gitlabIdInt
        )
        usersToProjectDao.insertUserToProject(
            chatId = creatorChatId,
            projectName = projectName,
            userName = userName,
            status = UserStatusInProject.ADMIN.name
        )
        projectDao.getProjectByName(projectName)
    }


    override suspend fun getProject(projectName: String): ProjectEntity? = dbQuery {
        projectDao.getProjectByName(projectName)
    }

    override suspend fun addUserToProject(
        chatId: Long,
        userName: String,
        projectName: String,
        status: UserStatusInProject
    ) = dbQuery {
        UserToProjectTable.insertUserToProject(
            chatId = chatId,
            projectName = projectName,
            userName = userName,
            status = status.name
        )
    }

    override suspend fun getUsersFromProject(projectName: String): List<UserToProjectEntity> = dbQuery {
        usersToProjectDao.getUsersFromProjects(projectName = projectName)
    }


    override suspend fun getProjectsForUser(chatId: Long): List<UserToProjectEntity> = dbQuery {
        usersToProjectDao.getProjectsForUser(chatId = chatId)
    }

    override suspend fun deleteUserFromProject(chatId: Long, projectName: String) = dbQuery {
        usersToProjectDao.deleteUserFromProject(chatId = chatId, projectName = projectName)
    }

    override suspend fun deleteProject(projectName: String) = dbQuery {
        projectDao.deleteProject(projectName)
        usersToProjectDao.deleteAllUsersFromProject(projectName)
    }

    override suspend fun setUserStatusToProject(chatId: Long, projectName: String, newStatus: String) {
        usersToProjectDao.setUserStatusToProject(chatId, projectName, newStatus)
    }

    override suspend fun getUserFromProject(chatId: Long, projectName: String): UserToProjectEntity? {
        return usersToProjectDao.getUserFromProject(chatId = chatId, projectName = projectName)
    }
}