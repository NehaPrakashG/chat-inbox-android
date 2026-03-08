package com.example.chatinbox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.room.Room
import com.example.chatinbox.data.local.db.ChatDatabase
import com.example.chatinbox.data.repository.ChatRepository
import com.example.chatinbox.data.repository.DefaultChatRepository
import com.example.chatinbox.data.seed.ChatLocalDataSeeder
import com.example.chatinbox.navigation.ChatNavGraph
import com.example.chatinbox.ui.components.LoadingView
import com.example.chatinbox.ui.theme.ChatInboxTheme

class MainActivity : ComponentActivity() {

    private lateinit var repository: ChatRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = Room.databaseBuilder(
            applicationContext,
            ChatDatabase::class.java,
            "chat_database"
        )
            .build()

        repository = DefaultChatRepository(
            database = database,
            chatLocalDataSeeder = ChatLocalDataSeeder(
                context = applicationContext,
                database = database
            )
        )

        setContent {
            ChatInboxTheme {
                var isAppReady by rememberSaveable { mutableStateOf(false) }

                // Seed the local database from the bundled JSON on first launch
                // before showing the main app content.
                LaunchedEffect(Unit) {
                    repository.initialize()
                    isAppReady = true
                }

                if (isAppReady) {
                    ChatNavGraph(repository = repository)
                } else {
                    LoadingView()
                }
            }
        }
    }
}