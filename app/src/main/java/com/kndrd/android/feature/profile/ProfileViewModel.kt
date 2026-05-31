package com.kndrd.android.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kndrd.android.core.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class ProfileUiState(
    val user: User? = null,
    val isLoading: Boolean = true,
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    userRepository: UserRepository,
) : ViewModel() {

    val uiState = userRepository.observeCurrentUser()
        .map { user -> ProfileUiState(user = user, isLoading = false) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProfileUiState(isLoading = true),
        )
}
