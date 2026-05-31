package com.kndrd.android.feature.forum

import com.kndrd.android.core.model.ForumPost
import com.kndrd.android.core.model.Interest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ForumRepositoryImpl @Inject constructor() : ForumRepository {

    private val _posts = MutableStateFlow(seedPosts())

    override fun observePosts(): Flow<List<ForumPost>> = _posts

    override suspend fun createPost(post: ForumPost): Result<ForumPost> {
        delay(400)
        val newPost = post.copy(id = UUID.randomUUID().toString())
        _posts.value = listOf(newPost) + _posts.value
        return Result.success(newPost)
    }

    override suspend fun upvotePost(postId: String, userId: String): Result<Unit> {
        delay(200)
        _posts.value = _posts.value.map { post ->
            if (post.id == postId && !post.isUpvotedByCurrentUser) {
                post.copy(upvotes = post.upvotes + 1, isUpvotedByCurrentUser = true)
            } else {
                post
            }
        }
        return Result.success(Unit)
    }

    override suspend fun refresh() {
        delay(300)
    }
}

private val now = System.currentTimeMillis()
private val oneHour = 3_600_000L

private fun seedPosts() = listOf(
    ForumPost(
        id = "post-1",
        authorId = "user-40",
        authorName = "Rachel B.",
        authorPhotoUrl = null,
        title = "Best spots for solo remote work in Brooklyn?",
        body = "Looking for cafes with good wifi and outlets that don't kick you out after an hour. Park Slope preferred but open to suggestions!",
        interest = Interest.COFFEE,
        upvotes = 24,
        replyCount = 18,
        timestampMs = now - 3 * oneHour,
        isUpvotedByCurrentUser = false,
    ),
    ForumPost(
        id = "post-2",
        authorId = "user-41",
        authorName = "Mia T.",
        authorPhotoUrl = null,
        title = "Anyone else find it hard to make friends in your 30s in NYC?",
        body = "Been here 2 years and most of my coworkers aren't nearby. Kndrd has helped but curious if others have tips for meeting people outside of apps.",
        interest = null,
        upvotes = 67,
        replyCount = 42,
        timestampMs = now - 8 * oneHour,
        isUpvotedByCurrentUser = false,
    ),
    ForumPost(
        id = "post-3",
        authorId = "user-42",
        authorName = "Isabel C.",
        authorPhotoUrl = null,
        title = "Hiking recommendations near the city?",
        body = "I know about the Catskills but looking for something doable on a Sunday without a car. Less than 2 hours from Manhattan ideally.",
        interest = Interest.OUTDOORS,
        upvotes = 31,
        replyCount = 27,
        timestampMs = now - 24 * oneHour,
        isUpvotedByCurrentUser = false,
    ),
    ForumPost(
        id = "post-4",
        authorId = "user-43",
        authorName = "Fatima A.",
        authorPhotoUrl = null,
        title = "Good running clubs in Queens?",
        body = "Just moved to Astoria and looking for a running group. Weekday mornings or weekend runs both work for me.",
        interest = Interest.FITNESS,
        upvotes = 15,
        replyCount = 11,
        timestampMs = now - 2 * 24 * oneHour,
        isUpvotedByCurrentUser = false,
    ),
    ForumPost(
        id = "post-5",
        authorId = "user-44",
        authorName = "Claire D.",
        authorPhotoUrl = null,
        title = "February book recs — what's everyone reading?",
        body = "Finished 'Intermezzo' last week and looking for my next read. Literary fiction or narrative nonfiction preferred!",
        interest = Interest.BOOKS,
        upvotes = 19,
        replyCount = 33,
        timestampMs = now - 3 * 24 * oneHour,
        isUpvotedByCurrentUser = false,
    ),
)
