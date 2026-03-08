package com.example.chatinbox.navigation

import android.net.Uri


object Routes {
    const val INBOX = "inbox"
    const val CONVERSATION = "conversation"
    const val CONVERSATION_ID_ARG = "conversationId"

    const val CONVERSATION_ROUTE = "$CONVERSATION/{$CONVERSATION_ID_ARG}"

    fun conversationRoute(conversationId: String): String {
        return "$CONVERSATION/${Uri.encode(conversationId)}"
    }
}