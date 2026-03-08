package com.example.chatinbox.ui.conversation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.chatinbox.data.local.entity.MessageEntity
import com.example.chatinbox.ui.components.ErrorView
import com.example.chatinbox.ui.components.LoadingView
import com.example.chatinbox.util.formatMessageDate

@Composable
fun ConversationScreen(
    viewModel: ConversationViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ConversationScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onSendClick = viewModel::sendMessage
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationScreenContent(
    uiState: ConversationUiState,
    onBackClick: () -> Unit,
    onSendClick: (String) -> Unit
) {
    var replyText by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.userChat.userName) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            ReplyBar(
                text = replyText,
                onTextChange = { replyText = it },
                onSendClick = {
                    val trimmed = replyText.trim()
                    if (trimmed.isNotEmpty()) {
                        onSendClick(trimmed)
                        replyText = ""
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {
            when {
                uiState.isLoading -> LoadingView()
                uiState.error != null -> ErrorView(uiState.error)
                else -> MessageList(messages = uiState.userChat.messages)
            }
        }
    }
}

@Composable
private fun MessageList(messages: List<MessageEntity>) {
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(messages, key = { it.id }) { message ->
            MessageBubble(message = message)
        }
    }
}

@Composable
private fun MessageBubble(message: MessageEntity) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.widthIn(max = 320.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = message.text, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatMessageDate(message.lastUpdated),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

@Composable
private fun ReplyBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    // Reply input anchored to the bottom of the screen.
    Surface(
        tonalElevation = 2.dp,
        modifier = Modifier.imePadding() // Pushes up with keyboard
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = onTextChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type a message...") },
                maxLines = 4,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
            )

            IconButton(
                onClick = onSendClick,
                enabled = text.isNotBlank(),
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary,
                    disabledContentColor = MaterialTheme.colorScheme.outline
                )
            ) {
                Icon(Icons.Default.Send, contentDescription = "Send")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConversationScreenPreview() {
    ConversationScreenContent(
        uiState = ConversationUiState(
            isLoading = false,
            userChat = UserChatDetails("Greg", listOf(
                MessageEntity(
                    id = "1",
                    conversationId = "conversation_1",
                    text = "Hello there!",
                    lastUpdated = System.currentTimeMillis()
                ),
                MessageEntity(
                    id = "2",
                    conversationId = "conversation_1",
                    text = "This is a preview message.",
                    lastUpdated = System.currentTimeMillis() - 60_000
                ),
            )),
            error = null
        ),
        onBackClick = {},
        onSendClick = {}
    )
}