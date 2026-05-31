package com.kndrd.android.feature.chats

import com.kndrd.android.core.model.ChatMessage
import com.kndrd.android.core.model.ChatRoom
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun observeRooms(userId: String): Flow<List<ChatRoom>>
    fun observeMessages(roomId: String): Flow<List<ChatMessage>>
    suspend fun sendMessage(roomId: String, senderId: String, senderName: String, text: String): Result<ChatMessage>
    suspend fun getRoomForPlan(planId: String): ChatRoom?
    fun addRoom(room: ChatRoom)
}
