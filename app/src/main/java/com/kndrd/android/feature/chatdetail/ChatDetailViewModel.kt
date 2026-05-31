package com.kndrd.android.feature.chatdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kndrd.android.core.data.preferences.UserPreferences
import com.kndrd.android.core.model.ChatMessage
import com.kndrd.android.feature.chats.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatDetailUiState(
    val messages: List<ChatMessage> = emptyList(),
    val inputText: String = "",
    val isLoading: Boolean = true,
    val roomTitle: String = "",
)

@HiltViewModel
class ChatDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val chatRepository: ChatRepository,
    private val userPreferences: UserPreferences,
) : ViewModel() {

    val roomId: String = checkNotNull(savedStateHandle["roomId"])

    private val _inputText = MutableStateFlow("")

    val uiState = combine(
        chatRepository.observeMessages(roomId),
        _inputText,
    ) { messages, input ->
        ChatDetailUiState(
            messages = messages,
            inputText = input,
            isLoading = false,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ChatDetailUiState(isLoading = true),
    )

    fun updateInput(text: String) {
        _inputText.value = text
    }

    fun sendMessage() {
        val text = _inputText.value.trim()
        if (text.isBlank()) return
        viewModelScope.launch {
            val userId = userPreferences.currentUserId.first() ?: "user-1"
            val userName = userPreferences.currentUserName.first() ?: "You"
            _inputText.value = ""
            chatRepository.sendMessage(roomId, userId, userName, text)
        }
    }
}
