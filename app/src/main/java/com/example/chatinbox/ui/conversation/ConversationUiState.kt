package com.example.chatinbox.ui.conversation

import com.example.chatinbox.data.local.entity.MessageEntity

data class ConversationUiState(
    val isLoading: Boolean = true,
    val userChat: UserChatDetails = UserChatDetails(),
    val error: String? = null
)

data class UserChatDetails(
    val userName: String = "",
    val messages: List<MessageEntity> = emptyList(),
)