package com.example.chatinbox.ui.conversation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatinbox.data.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update

class ConversationViewModel(
    private val repository: ChatRepository,
    private val conversationId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConversationUiState())
    val uiState: StateFlow<ConversationUiState> = _uiState.asStateFlow()

    init {
        observeConversation()
    }

    private fun observeConversation() {
        viewModelScope.launch {
            repository.getConversationById(conversationId)
                .combine(repository.getMessages(conversationId)) { conversation, messages ->
                    ConversationUiState(
                        isLoading = false,
                        userChat = UserChatDetails(
                            userName = conversation?.name.orEmpty(),
                            messages = messages
                        ),
                        error = null
                    )
                }
                .catch { e ->
                    emit(
                        ConversationUiState(
                            isLoading = false,
                            error = e.message ?: "Failed to load conversation"
                        )
                    )
                }
                .collect { state ->
                    _uiState.value = state
                }
        }
    }

    fun sendMessage(text: String) {
        val trimmedText = text.trim()
        if (trimmedText.isBlank()) return

        viewModelScope.launch {
            try {
                repository.sendMessage(
                    conversationId = conversationId,
                    text = trimmedText
                )
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = e.message ?: "Failed to send message")
                }
            }
        }
    }
}