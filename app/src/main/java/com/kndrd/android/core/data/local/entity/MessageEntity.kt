package com.kndrd.android.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val id: String,
    val roomId: String,
    val senderId: String,
    val senderName: String,
    val senderPhotoUrl: String?,
    val text: String,
    val timestampMs: Long,
)
