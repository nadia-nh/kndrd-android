package com.kndrd.android.core.model

data class Plan(
    val id: String,
    val title: String,
    val description: String,
    val hostId: String,
    val hostName: String,
    val hostPhotoUrl: String?,
    val interest: Interest,
    val location: String,
    val dateTimeMs: Long,
    val maxAttendees: Int,
    val currentAttendees: Int,
    val attendeeIds: List<String>,
    val imageUrl: String?,
    val chatRoomId: String,
    val isJoined: Boolean,
)
