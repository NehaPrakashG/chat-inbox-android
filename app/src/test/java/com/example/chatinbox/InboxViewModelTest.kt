package com.example.chatinbox

import com.example.chatinbox.data.local.entity.ConversationEntity
import com.example.chatinbox.data.repository.ChatRepository
import com.example.chatinbox.ui.inbox.InboxViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class InboxViewModelTest {

    private val repository: ChatRepository = mockk()

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `observeConversations updates ui state`() = runTest {

        val conversations = listOf(
            ConversationEntity("1", "Team Chat", 1000),
            ConversationEntity("2", "Family", 500)
        )

        every { repository.getConversations() } returns MutableStateFlow(conversations)

        val viewModel = InboxViewModel(repository)

        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertEquals(conversations, state.conversations)
        assertNull(state.error)
    }
}