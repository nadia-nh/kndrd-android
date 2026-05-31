package com.kndrd.android.feature.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kndrd.android.core.model.Interest
import com.kndrd.android.core.model.Plan
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FeedUiState(
    val plans: List<Plan> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val selectedInterest: Interest? = null,
    val error: String? = null,
)

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val repository: FeedRepository,
) : ViewModel() {

    private val _selectedInterest = MutableStateFlow<Interest?>(null)
    private val _isRefreshing = MutableStateFlow(false)

    val uiState = combine(
        repository.observePlans(),
        _selectedInterest,
        _isRefreshing,
    ) { plans, interest, isRefreshing ->
        val filtered = if (interest == null) plans else plans.filter { it.interest == interest }
        FeedUiState(plans = filtered, selectedInterest = interest, isRefreshing = isRefreshing)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = FeedUiState(isLoading = true),
    )

    fun selectInterest(interest: Interest?) {
        _selectedInterest.value = interest
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            repository.refresh()
            _isRefreshing.value = false
        }
    }
}
