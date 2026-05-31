package com.kndrd.android.feature.createplan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kndrd.android.core.data.preferences.UserPreferences
import com.kndrd.android.core.model.Interest
import com.kndrd.android.core.model.Plan
import com.kndrd.android.feature.feed.FeedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class CreatePlanState(
    val title: String = "",
    val description: String = "",
    val location: String = "",
    val interest: Interest? = null,
    val dateTimeMs: Long? = null,
    val maxAttendees: Int = 8,
    val isSubmitting: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class CreatePlanViewModel @Inject constructor(
    private val feedRepository: FeedRepository,
    private val userPreferences: UserPreferences,
) : ViewModel() {

    private val _state = MutableStateFlow(CreatePlanState())
    val state = _state.asStateFlow()

    fun updateTitle(v: String) = _state.update { it.copy(title = v) }
    fun updateDescription(v: String) = _state.update { it.copy(description = v) }
    fun updateLocation(v: String) = _state.update { it.copy(location = v) }
    fun updateInterest(v: Interest) = _state.update { it.copy(interest = v) }
    fun updateDateTime(ms: Long) = _state.update { it.copy(dateTimeMs = ms) }
    fun updateMaxAttendees(v: Int) = _state.update { it.copy(maxAttendees = v) }

    fun submit(onSuccess: () -> Unit) {
        val s = _state.value
        if (s.title.isBlank() || s.interest == null || s.location.isBlank() || s.dateTimeMs == null) {
            _state.update { it.copy(error = "Please fill in all required fields.") }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isSubmitting = true, error = null) }
            val userId = userPreferences.currentUserId.first() ?: "user-1"
            val userName = userPreferences.currentUserName.first() ?: "You"
            val plan = Plan(
                id = UUID.randomUUID().toString(),
                title = s.title,
                description = s.description,
                hostId = userId,
                hostName = userName,
                hostPhotoUrl = null,
                interest = s.interest,
                location = s.location,
                dateTimeMs = s.dateTimeMs,
                maxAttendees = s.maxAttendees,
                currentAttendees = 0,
                attendeeIds = emptyList(),
                imageUrl = null,
                chatRoomId = UUID.randomUUID().toString(),
                isJoined = false,
            )
            feedRepository.createPlan(plan).fold(
                onSuccess = {
                    _state.update { it.copy(isSubmitting = false, isSuccess = true) }
                    onSuccess()
                },
                onFailure = { e ->
                    _state.update { it.copy(isSubmitting = false, error = e.message) }
                },
            )
        }
    }
}
