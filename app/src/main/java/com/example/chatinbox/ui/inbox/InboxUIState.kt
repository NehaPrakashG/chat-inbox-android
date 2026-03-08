package com.example.chatinbox.ui.inbox

import com.example.chatinbox.data.local.entity.ConversationEntity

data class InboxUiState(
    val isLoading: Boolean = true,
    val conversations: List<ConversationEntity> = emptyList(),
    val error: String? = null
)