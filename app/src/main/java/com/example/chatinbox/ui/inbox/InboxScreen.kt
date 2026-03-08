package com.example.chatinbox.ui.inbox

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.chatinbox.data.local.entity.ConversationEntity
import com.example.chatinbox.ui.components.EmptyDataView
import com.example.chatinbox.ui.components.ExitHandler
import com.example.chatinbox.ui.components.ErrorView
import com.example.chatinbox.ui.components.LoadingView
import com.example.chatinbox.util.formatMessageDate

@Composable
fun InboxScreen(
    viewModel: InboxViewModel,
    onConversationClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    InboxScreenContent(
        uiState = uiState,
        onConversationClick = onConversationClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InboxScreenContent(
    uiState: InboxUiState,
    onConversationClick: (String) -> Unit
) {
    ExitHandler()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inbox") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                uiState.isLoading -> LoadingView()
                uiState.error != null -> ErrorView(uiState.error)
                uiState.conversations.isEmpty() -> EmptyDataView("No chats")
                else -> {
                    ChatList(
                        conversations = uiState.conversations,
                        onConversationClick = onConversationClick
                    )
                }
            }
        }
    }
}

@Composable
private fun ChatList(
    conversations: List<ConversationEntity>,
    onConversationClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(conversations, key = { it.id }) { conversation ->
            ConversationItem(
                conversation = conversation,
                onClick = { onConversationClick(conversation.id) }
            )
        }
    }
}

@Composable
private fun ConversationItem(
    conversation: ConversationEntity,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = conversation.name,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = formatMessageDate(conversation.lastUpdated),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InboxScreenPreview() {
    val fakeState = InboxUiState(
        isLoading = false,
        conversations = listOf(
            ConversationEntity(
                id = "1",
                name = "Team Chat",
                lastUpdated = System.currentTimeMillis()
            ),
            ConversationEntity(
                id = "2",
                name = "Family",
                lastUpdated = System.currentTimeMillis()
            )
        )
    )

    InboxScreenContent(
        uiState = fakeState,
        onConversationClick = {}
    )
}