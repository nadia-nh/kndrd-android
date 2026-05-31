package com.kndrd.android.feature.chats

import com.kndrd.android.core.model.ChatMessage
import com.kndrd.android.core.model.ChatRoom
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor() : ChatRepository {

    private val _rooms = MutableStateFlow<List<ChatRoom>>(emptyList())
    private val _messages = MutableStateFlow<Map<String, List<ChatMessage>>>(seedMessages())

    override fun observeRooms(userId: String): Flow<List<ChatRoom>> =
        _rooms.map { rooms -> rooms.filter { userId in it.memberIds } }

    override fun observeMessages(roomId: String): Flow<List<ChatMessage>> =
        _messages.map { it[roomId] ?: emptyList() }

    override suspend fun sendMessage(
        roomId: String,
        senderId: String,
        senderName: String,
        text: String,
    ): Result<ChatMessage> {
        delay(100)
        val message = ChatMessage(
            id = UUID.randomUUID().toString(),
            roomId = roomId,
            senderId = senderId,
            senderName = senderName,
            senderPhotoUrl = null,
            text = text,
            timestampMs = System.currentTimeMillis(),
            isFromCurrentUser = true,
        )
        val updated = (_messages.value[roomId] ?: emptyList()) + message
        _messages.value = _messages.value + (roomId to updated)

        _rooms.value = _rooms.value.map { room ->
            if (room.id == roomId) room.copy(lastMessage = text, lastMessageTimeMs = message.timestampMs)
            else room
        }
        return Result.success(message)
    }

    override suspend fun getRoomForPlan(planId: String): ChatRoom? =
        _rooms.value.find { it.planId == planId }

    override fun addRoom(room: ChatRoom) {
        if (_rooms.value.none { it.id == room.id }) {
            _rooms.value = _rooms.value + room
        }
    }
}

private val now = System.currentTimeMillis()
private val oneMin = 60_000L

private fun seedMessages(): Map<String, List<ChatMessage>> = mapOf(
    "chat-1" to listOf(
        ChatMessage("m1", "chat-1", "user-2", "Maya K.", null, "Hey everyone! So excited ☕", now - 10 * oneMin, false),
        ChatMessage("m2", "chat-1", "user-3", "Sofia L.", null, "Can't wait! Blue Bottle is my fave", now - 8 * oneMin, false),
        ChatMessage("m3", "chat-1", "user-4", "Lena P.", null, "See you all there!", now - 5 * oneMin, false),
    ),
    "chat-2" to listOf(
        ChatMessage("m4", "chat-2", "user-5", "Priya S.", null, "Meeting at the Great Lawn entrance 🧘", now - 20 * oneMin, false),
        ChatMessage("m5", "chat-2", "user-6", "Aisha M.", null, "Bringing my mat and some extra water for anyone who needs", now - 15 * oneMin, false),
    ),
)
