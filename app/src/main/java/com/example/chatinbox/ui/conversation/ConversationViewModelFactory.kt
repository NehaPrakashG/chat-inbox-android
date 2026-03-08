package com.example.chatinbox.ui.conversation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.chatinbox.data.repository.ChatRepository

class ConversationViewModelFactory(
    private val repository: ChatRepository,
    private val conversationId: String,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConversationViewModel::class.java)) {
            return ConversationViewModel(repository, conversationId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}