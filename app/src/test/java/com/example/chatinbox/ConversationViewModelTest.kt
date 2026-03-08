package com.example.chatinbox.ui.conversation

import com.example.chatinbox.data.local.entity.ConversationEntity
import com.example.chatinbox.data.local.entity.MessageEntity
import com.example.chatinbox.data.repository.ChatRepository
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ConversationViewModelTest {

    private val repository: ChatRepository = mockk(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `sendMessage ignores blank text`() = runTest {
        val conversationId = "conversation_1"

        every { repository.getConversationById(conversationId) } returns flowOf(null)
        every { repository.getMessages(conversationId) } returns MutableStateFlow(emptyList())

        val viewModel = ConversationViewModel(repository, conversationId)

        advanceUntilIdle()

        viewModel.sendMessage("   ")

        advanceUntilIdle()

        coVerify(exactly = 0) {
            repository.sendMessage(any(), any())
        }
    }

    @Test
    fun `conversation and messages update ui state`() = runTest {
        val conversationId = "conversation_1"

        val conversation = ConversationEntity(
            id = conversationId,
            name = "Greg",
            lastUpdated = 1000L
        )

        val messages = listOf(
            MessageEntity("1", conversationId, "Hello", 1000L),
            MessageEntity("2", conversationId, "How are you?", 2000L)
        )

        every { repository.getConversationById(conversationId) } returns flowOf(conversation)
        every { repository.getMessages(conversationId) } returns MutableStateFlow(messages)

        val viewModel = ConversationViewModel(
            repository = repository,
            conversationId = conversationId
        )

        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertEquals("Greg", state.userChat.userName)
        assertEquals(messages, state.userChat.messages)
        assertNull(state.error)
    }
}