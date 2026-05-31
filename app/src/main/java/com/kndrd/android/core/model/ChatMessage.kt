package com.kndrd.android.core.model

data class ChatMessage(
    val id: String,
    val roomId: String,
    val senderId: String,
    val senderName: String,
    val senderPhotoUrl: String?,
    val text: String,
    val timestampMs: Long,
    val isFromCurrentUser: Boolean,
)
