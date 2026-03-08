package com.example.chatinbox.data.repository

import androidx.room.withTransaction
import com.example.chatinbox.data.local.db.ChatDatabase
import com.example.chatinbox.data.local.entity.ConversationEntity
import com.example.chatinbox.data.local.entity.MessageEntity
import com.example.chatinbox.data.seed.ChatLocalDataSeeder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.UUID
class DefaultChatRepository(
    private val database: ChatDatabase,
    private val chatLocalDataSeeder: ChatLocalDataSeeder,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ChatRepository {

    private val conversationDao = database.conversationDao()
    private val messageDao = database.messageDao()

    override suspend fun initialize() = withContext(ioDispatcher) {
        chatLocalDataSeeder.seedIfNeeded()
    }

    override fun getConversations(): Flow<List<ConversationEntity>> =
        conversationDao.observeConversations()

    override fun getConversationById(conversationId: String): Flow<ConversationEntity?> =
        conversationDao.getConversationById(conversationId)

    override fun getMessages(conversationId: String): Flow<List<MessageEntity>> =
        messageDao.getMessages(conversationId)

    override suspend fun sendMessage(conversationId: String, text: String) = withContext(ioDispatcher) {
        val trimmedText = text.trim()
        if (trimmedText.isEmpty()) return@withContext

        val now = System.currentTimeMillis()
        val newMessage = MessageEntity(
            id = UUID.randomUUID().toString(),
            conversationId = conversationId,
            text = trimmedText,
            lastUpdated = now
        )

        database.withTransaction {
            messageDao.insert(newMessage)
            conversationDao.updateLastUpdated(conversationId, now)
        }
    }
}