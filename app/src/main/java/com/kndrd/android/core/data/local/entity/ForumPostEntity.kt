package com.kndrd.android.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "forum_posts")
data class ForumPostEntity(
    @PrimaryKey val id: String,
    val authorId: String,
    val authorName: String,
    val authorPhotoUrl: String?,
    val title: String,
    val body: String,
    val interestName: String?,
    val upvotes: Int,
    val replyCount: Int,
    val timestampMs: Long,
)
