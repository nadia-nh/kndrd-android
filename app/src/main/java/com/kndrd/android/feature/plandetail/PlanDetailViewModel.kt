package com.kndrd.android.feature.plandetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kndrd.android.core.data.preferences.UserPreferences
import com.kndrd.android.core.model.Plan
import com.kndrd.android.feature.feed.FeedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlanDetailUiState(
    val plan: Plan? = null,
    val isLoading: Boolean = true,
    val isJoining: Boolean = false,
    val error: String? = null,
)

sealed class PlanDetailEvent {
    data class NavigateToChat(val roomId: String) : PlanDetailEvent()
}

@HiltViewModel
class PlanDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val feedRepository: FeedRepository,
    private val userPreferences: UserPreferences,
) : ViewModel() {

    private val planId: String = checkNotNull(savedStateHandle["planId"])

    private val _uiState = MutableStateFlow(PlanDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<PlanDetailEvent>()
    val events = _events.asSharedFlow()

    init {
        viewModelScope.launch {
            val plan = feedRepository.getPlan(planId)
            _uiState.update { it.copy(plan = plan, isLoading = false) }
        }
    }

    fun joinPlan() {
        viewModelScope.launch {
            _uiState.update { it.copy(isJoining = true) }
            val userId = userPreferences.currentUserId.first() ?: "user-1"
            feedRepository.joinPlan(planId, userId).fold(
                onSuccess = { room ->
                    _uiState.update { it.copy(isJoining = false, plan = it.plan?.copy(isJoined = true)) }
                    _events.emit(PlanDetailEvent.NavigateToChat(room.id))
                },
                onFailure = { e ->
                    _uiState.update { it.copy(isJoining = false, error = e.message) }
                },
            )
        }
    }
}
