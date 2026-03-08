package com.example.chatinbox.data.seed

import android.content.Context
import androidx.room.withTransaction
import com.example.chatinbox.data.local.db.ChatDatabase
import com.example.chatinbox.data.local.entity.ConversationEntity
import com.example.chatinbox.data.local.entity.MessageEntity
import com.example.chatinbox.util.parseDate
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Seeds the local Room database from the bundled JSON asset the first time the app launches.
class ChatLocalDataSeeder(
    private val context: Context,
    private val database: ChatDatabase
) {
    suspend fun seedIfNeeded() = withContext(Dispatchers.IO) {
        val conversationDao = database.conversationDao()
        val messageDao = database.messageDao()

        if (conversationDao.count() > 0) return@withContext

        val json = context.assets.open("conversations.json")
            .bufferedReader()
            .use { it.readText() }

        val dtos = Gson().fromJson<List<ConversationDto>>(
            json,
            object : TypeToken<List<ConversationDto>>() {}.type
        )

        val conversationEntities = mutableListOf<ConversationEntity>()
        val messageEntities = mutableListOf<MessageEntity>()

        dtos.forEach { convoDto ->
            conversationEntities.add(
                ConversationEntity(
                    id = convoDto.id,
                    name = convoDto.name,
                    lastUpdated = parseDate(convoDto.last_updated)
                )
            )

            convoDto.messages.forEach { msgDto ->
                messageEntities.add(
                    MessageEntity(
                        id = msgDto.id,
                        conversationId = convoDto.id,
                        text = msgDto.text,
                        lastUpdated = parseDate(msgDto.last_updated)
                    )
                )
            }
        }

        database.withTransaction {
            conversationDao.insertAll(conversationEntities)
            messageDao.insertAll(messageEntities)
        }
    }
}