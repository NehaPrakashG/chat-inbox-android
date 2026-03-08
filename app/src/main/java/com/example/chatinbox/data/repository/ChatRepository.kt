package com.example.chatinbox.data.repository

import com.example.chatinbox.data.local.entity.ConversationEntity
import com.example.chatinbox.data.local.entity.MessageEntity
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun initialize()
    fun getConversations(): Flow<List<ConversationEntity>>
    fun getConversationById(conversationId: String): Flow<ConversationEntity?>
    fun getMessages(conversationId: String): Flow<List<MessageEntity>>
    suspend fun sendMessage(conversationId: String, text: String)
}