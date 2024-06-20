package com.kiselev.dataBase.tables

import com.kiselev.dataBase.dao.UserToProjectDao
import com.kiselev.dataBase.entity.UserToProjectEntity
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

object UserToProjectTable : Table(name = "chat_to_project"), UserToProjectDao {
    private val id = varchar("id", 50).uniqueIndex()
    private val projectName = varchar("project_name", 50)
    private val chatId = long("chat_id")
    private val userName = varchar("user_name", 50)
    private val userStatusToProject = varchar("chat_status_to_project", 15)

    override fun insertUserToProject(chatId: Long, userName: String, projectName: String, status: String) {
        try {
            transaction {
                insert {
                    it[id] = projectName + chatId
                    it[UserToProjectTable.projectName] = projectName
                    it[UserToProjectTable.chatId] = chatId
                    it[UserToProjectTable.userName] = userName
                    it[userStatusToProject] = status
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun getUsersFromProjects(projectName: String): List<UserToProjectEntity> {
        return try {
            transaction {
                UserToProjectTable.select { UserToProjectTable.projectName.eq(projectName) }.toList().map {
                    UserToProjectEntity(
                        projectName = it[UserToProjectTable.projectName],
                        chatId = it[chatId],
                        userName = it[userName],
                        userStatusToProject = it[userStatusToProject]
                    )
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override fun getProjectsForUser(chatId: Long): List<UserToProjectEntity> {
        return try {
            transaction {
                UserToProjectTable.select { UserToProjectTable.chatId.eq(chatId) }.toList().map {
                    UserToProjectEntity(
                        projectName = it[projectName],
                        chatId = it[UserToProjectTable.chatId],
                        userName = it[userName],
                        userStatusToProject = it[userStatusToProject]
                    )
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }


    override fun setUserStatusToProject(chatId: Long, projectName: String, newStatus: String) {
        transaction {
            update({
                UserToProjectTable.chatId eq chatId and (UserToProjectTable.projectName eq projectName)
            }) {
                it[userStatusToProject] = newStatus
            }
        }
    }

    override fun deleteAllUsersFromProject(projectName: String) {
        transaction {
            deleteWhere {
                UserToProjectTable.projectName eq projectName
            }
        }
    }

    override fun deleteUserFromAllProject(chatId: Long) {
        transaction {
            deleteWhere {
                UserToProjectTable.chatId eq chatId
            }
        }
    }

    override fun deleteUserFromProject(chatId: Long, projectName: String) {
        transaction {
            deleteWhere {
                UserToProjectTable.chatId eq chatId and (UserToProjectTable.projectName eq projectName)
            }
        }
    }

    override fun getUserFromProject(chatId: Long, projectName: String): UserToProjectEntity? {
        return try {
            transaction {
                val resultRow = select {
                    UserToProjectTable.chatId eq chatId and (UserToProjectTable.projectName eq projectName)
                }.single()
                UserToProjectEntity(
                    projectName = resultRow[UserToProjectTable.projectName],
                    chatId = resultRow[UserToProjectTable.chatId],
                    userName = resultRow[UserToProjectTable.userName],
                    userStatusToProject = resultRow[userStatusToProject]
                )
            }

        } catch (e: Exception) {
            null
        }
    }
}