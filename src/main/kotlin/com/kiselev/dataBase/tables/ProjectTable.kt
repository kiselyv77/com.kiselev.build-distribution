package com.kiselev.dataBase.tables

import com.kiselev.dataBase.dao.ProjectDao
import com.kiselev.dataBase.entity.ProjectEntity
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

object ProjectTable : Table(name = "project"), ProjectDao {

    private val name = varchar(name = "name", 25).uniqueIndex() // primary key
    private val accessToken = varchar(name = "access_token", 64)
    private val gitlabBaseUrl = varchar(name = "gitlab_base_url", 64).nullable()
    private val triggerToken = varchar(name = "trigger_token", 64).nullable()
    private val gitlabId = integer(name = "gitlab_id").nullable()


    override fun insertProject(
        name: String,
        accessToken: String,
        gitlabBaseUrl: String?,
        triggerToken: String?,
        gitlabId: Int?,
    ) {
        transaction {
            insert {
                it[ProjectTable.name] = name
                it[ProjectTable.accessToken] = accessToken
                it[ProjectTable.gitlabBaseUrl] = gitlabBaseUrl
                it[ProjectTable.triggerToken] = triggerToken
                it[ProjectTable.gitlabId] = gitlabId
            }
        }
    }

    override fun getProjectByName(projectName: String): ProjectEntity? {
        return try {
            transaction {
                val projectsResultRow = ProjectTable.select { name.eq(projectName) }.single()
                ProjectEntity(
                    name = projectsResultRow[name],
                    accessToken = projectsResultRow[accessToken],
                    gitLabBaseUrl = projectsResultRow[gitlabBaseUrl],
                    triggerToken = projectsResultRow[triggerToken],
                    gitlabId = projectsResultRow[gitlabId]
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    override fun getProjectByAccessToken(accessToken: String): ProjectEntity? {
        return try {
            transaction {
                val projectsResultRow = ProjectTable.select { ProjectTable.accessToken.eq(accessToken) }.single()
                ProjectEntity(
                    name = projectsResultRow[name],
                    accessToken = projectsResultRow[ProjectTable.accessToken],
                    gitLabBaseUrl = projectsResultRow[gitlabBaseUrl],
                    triggerToken = projectsResultRow[triggerToken],
                    gitlabId = projectsResultRow[gitlabId]

                )
            }
        } catch (e: Exception) {
            null
        }
    }

    override fun getProjects(): List<ProjectEntity> {
        return try {
            transaction {
                ProjectTable.selectAll().toList().map {
                    ProjectEntity(
                        name = it[name],
                        accessToken = it[accessToken],
                        gitLabBaseUrl = it[gitlabBaseUrl],
                        triggerToken = it[triggerToken],
                        gitlabId = it[gitlabId]
                    )
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override fun deleteProject(projectName: String) {
        transaction {
            deleteWhere {
                name eq projectName
            }
        }
    }
}