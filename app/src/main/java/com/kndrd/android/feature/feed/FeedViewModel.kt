package com.kndrd.android.feature.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kndrd.android.core.data.preferences.UserPreferences
import com.kndrd.android.core.model.Interest
import com.kndrd.android.core.model.Plan
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class FeedStatusFilter(val label: String) {
    ALL("All"),
    GOING("Going"),
    HOSTING("Hosting"),
    SAVED("Saved"),
}

data class FeedUiState(
    val plans: List<Plan> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val selectedInterest: Interest? = null,
    val selectedStatus: FeedStatusFilter = FeedStatusFilter.ALL,
    val error: String? = null,
)

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val repository: FeedRepository,
    private val userPreferences: UserPreferences,
) : ViewModel() {

    private val _selectedInterest = MutableStateFlow<Interest?>(null)
    private val _selectedStatus = MutableStateFlow(FeedStatusFilter.ALL)
    private val _isRefreshing = MutableStateFlow(false)

    val uiState = combine(
        repository.observePlans(),
        _selectedInterest,
        _selectedStatus,
        _isRefreshing,
        userPreferences.currentUserId,
    ) { plans, interest, status, isRefreshing, currentUserId ->
        val byStatus = when (status) {
            FeedStatusFilter.ALL     -> plans
            FeedStatusFilter.GOING   -> plans.filter { it.isJoined }
            FeedStatusFilter.HOSTING -> plans.filter { it.hostId == currentUserId }
            FeedStatusFilter.SAVED   -> emptyList() // saved feature not yet implemented
        }
        val filtered = if (interest == null) byStatus else byStatus.filter { it.interest == interest }
        FeedUiState(
            plans = filtered,
            selectedInterest = interest,
            selectedStatus = status,
            isRefreshing = isRefreshing,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = FeedUiState(isLoading = true),
    )

    fun selectInterest(interest: Interest?) {
        _selectedInterest.value = interest
    }

    fun selectStatus(status: FeedStatusFilter) {
        _selectedStatus.value = status
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            repository.refresh()
            _isRefreshing.value = false
        }
    }
}
