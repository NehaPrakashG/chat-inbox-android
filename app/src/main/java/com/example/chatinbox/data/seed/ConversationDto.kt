package com.example.chatinbox.data.seed

data class ConversationDto(
    val id: String,
    val name: String,
    val last_updated: String,
    val messages: List<MessageDto>
)

data class MessageDto(
    val id: String,
    val text: String,
    val last_updated: String
)

