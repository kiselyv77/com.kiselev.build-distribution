package com.kiselev.bot

import dev.inmo.micro_utils.fsm.common.State
import dev.inmo.tgbotapi.types.IdChatIdentifier
import java.util.*

sealed interface BotState : State {
    data class StartedState(override val context: IdChatIdentifier, val locale: Locale) : BotState
}