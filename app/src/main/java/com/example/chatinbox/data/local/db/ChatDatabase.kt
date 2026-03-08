package com.example.chatinbox.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.chatinbox.data.local.dao.ConversationDao
import com.example.chatinbox.data.local.dao.MessageDao
import com.example.chatinbox.data.local.entity.ConversationEntity
import com.example.chatinbox.data.local.entity.MessageEntity
@Database(
    entities = [
        ConversationEntity::class,
        MessageEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ChatDatabase : RoomDatabase() {

    abstract fun conversationDao(): ConversationDao

    abstract fun messageDao(): MessageDao
}