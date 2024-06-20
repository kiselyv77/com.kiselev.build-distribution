package com.kiselev.dataBase.types

/**
 * Статус пользователя в проекте
 */
enum class UserStatusInProject {
    CREATOR,
    ADMIN,
    USER;

    companion object {
        fun from(type: String?): UserStatusInProject? = UserStatusInProject.entries.find { it.name == type }
        fun setStatus(currentStatus: String?): String {
            return when (from(currentStatus)) {
                CREATOR -> CREATOR.name
                ADMIN -> USER.name
                USER -> ADMIN.name
                null -> USER.name
            }
        }
    }
}