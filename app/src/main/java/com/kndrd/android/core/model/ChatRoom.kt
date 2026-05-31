package com.kndrd.android.core.model

data class ChatRoom(
    val id: String,
    val planId: String,
    val planTitle: String,
    val memberIds: List<String>,
    val lastMessage: String?,
    val lastMessageTimeMs: Long?,
    val unreadCount: Int,
)
