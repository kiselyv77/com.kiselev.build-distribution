package com.kiselev.gitLab

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*

class GitLabApiImpl : GitLabApi {

    override suspend fun triggerPipeline(
        projectId: Int,
        refName: String,
        triggerToken: String,
        gitLabBaseUrl: String
    ): TriggerResult {
        val url = "$gitLabBaseUrl/api/v4/projects/$projectId/trigger/pipeline"
        val client = HttpClient(CIO)
        return try {
            val response = client.post(url) {
                parameter(TOKEN_KEY, triggerToken)
                parameter(REF_KEY, refName)
            }
            val status = response.status
            TriggerResult(
                success = status.isSuccess(),
                message = "${status.value} ${status.description}"
            )
        } catch (e: Exception) {
            println("Error: ${e.message}")
            TriggerResult(
                success = false,
                message = "Упс... Произошла неизвестная ошибка \uD83D\uDE31"
            )
        }
    }

    companion object {
        private const val TOKEN_KEY = "token"
        private const val REF_KEY = "ref"
    }
}