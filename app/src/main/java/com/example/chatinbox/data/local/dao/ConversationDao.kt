package com.example.chatinbox.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.chatinbox.data.local.entity.ConversationEntity
import kotlinx.coroutines.flow.Flow

// DAO for accessing conversation data from the Room database.
// Conversations are exposed as a Flow so the UI reacts automatically
// when conversation timestamps change (e.g., when a new message is sent).

@Dao
interface ConversationDao {
    @Query("SELECT * FROM conversations ORDER BY lastUpdated DESC")
    fun observeConversations(): Flow<List<ConversationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(conversations: List<ConversationEntity>)

    // Expose the selected conversation as a Flow so it can be combined
    // with the message stream into a single UI state.
    @Query("SELECT * FROM conversations WHERE id = :conversationId LIMIT 1")
    fun getConversationById(conversationId: String): Flow<ConversationEntity?>

    @Query("UPDATE conversations SET lastUpdated = :lastUpdated WHERE id = :conversationId")
    suspend fun updateLastUpdated(conversationId: String, lastUpdated: Long)

    @Query("SELECT COUNT(*) FROM conversations")
    suspend fun count(): Int
}