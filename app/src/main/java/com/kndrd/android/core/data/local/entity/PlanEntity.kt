package com.kndrd.android.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plans")
data class PlanEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val hostId: String,
    val hostName: String,
    val hostPhotoUrl: String?,
    val interestName: String,
    val location: String,
    val dateTimeMs: Long,
    val maxAttendees: Int,
    val currentAttendees: Int,
    val attendeeIdsJson: String,
    val imageUrl: String?,
    val chatRoomId: String,
    val cachedAtMs: Long,
)
