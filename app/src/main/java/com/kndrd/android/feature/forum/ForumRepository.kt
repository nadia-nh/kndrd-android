package com.kndrd.android.feature.forum

import com.kndrd.android.core.model.ForumPost
import kotlinx.coroutines.flow.Flow

interface ForumRepository {
    fun observePosts(): Flow<List<ForumPost>>
    suspend fun createPost(post: ForumPost): Result<ForumPost>
    suspend fun upvotePost(postId: String, userId: String): Result<Unit>
    suspend fun refresh()
}
