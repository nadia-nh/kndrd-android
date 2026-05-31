package com.kndrd.android.feature.forum

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kndrd.android.core.data.preferences.UserPreferences
import com.kndrd.android.core.model.ForumPost
import com.kndrd.android.core.model.Interest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class ForumUiState(
    val posts: List<ForumPost> = emptyList(),
    val isLoading: Boolean = true,
)

@HiltViewModel
class ForumViewModel @Inject constructor(
    private val forumRepository: ForumRepository,
    private val userPreferences: UserPreferences,
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)

    val uiState = combine(
        forumRepository.observePosts(),
        _isLoading,
    ) { posts, loading ->
        ForumUiState(posts = posts, isLoading = loading)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ForumUiState(isLoading = true),
    )

    init {
        viewModelScope.launch {
            _isLoading.value = false
        }
    }

    fun upvotePost(postId: String) {
        viewModelScope.launch {
            val userId = userPreferences.currentUserId.first() ?: "user-1"
            forumRepository.upvotePost(postId, userId)
        }
    }

    fun createPost(title: String, body: String, interest: Interest?, onDone: () -> Unit) {
        if (title.isBlank() || body.isBlank()) return
        viewModelScope.launch {
            val userId = userPreferences.currentUserId.first() ?: "user-1"
            val userName = userPreferences.currentUserName.first() ?: "You"
            val post = ForumPost(
                id = UUID.randomUUID().toString(),
                authorId = userId,
                authorName = userName,
                authorPhotoUrl = null,
                title = title,
                body = body,
                interest = interest,
                upvotes = 0,
                replyCount = 0,
                timestampMs = System.currentTimeMillis(),
                isUpvotedByCurrentUser = false,
            )
            forumRepository.createPost(post).onSuccess { onDone() }
        }
    }
}
