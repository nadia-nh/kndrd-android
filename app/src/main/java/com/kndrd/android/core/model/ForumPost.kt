package com.kndrd.android.core.model

data class ForumPost(
    val id: String,
    val authorId: String,
    val authorName: String,
    val authorPhotoUrl: String?,
    val title: String,
    val body: String,
    val interest: Interest?,
    val upvotes: Int,
    val replyCount: Int,
    val timestampMs: Long,
    val isUpvotedByCurrentUser: Boolean,
)
