package com.kndrd.android.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kndrd.android.core.data.preferences.UserPreferences
import com.kndrd.android.core.model.Interest
import com.kndrd.android.feature.profile.UserRepository
import com.kndrd.android.core.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class OnboardingState(
    val name: String = "",
    val phone: String = "",
    val verificationCode: String = "",
    val selectedInterests: Set<Interest> = emptySet(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(OnboardingState())
    val state = _state.asStateFlow()

    fun updateName(name: String) = _state.update { it.copy(name = name) }
    fun updatePhone(phone: String) = _state.update { it.copy(phone = phone) }
    fun updateCode(code: String) = _state.update { it.copy(verificationCode = code) }

    fun toggleInterest(interest: Interest) {
        _state.update { s ->
            val updated = if (interest in s.selectedInterests) {
                s.selectedInterests - interest
            } else {
                s.selectedInterests + interest
            }
            s.copy(selectedInterests = updated)
        }
    }

    fun completeOnboarding(onDone: () -> Unit) {
        val s = _state.value
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val userId = UUID.randomUUID().toString()
            val user = User(
                id = userId,
                name = s.name,
                age = 0,
                bio = "",
                photoUrl = null,
                neighborhood = "New York City",
                interests = s.selectedInterests.toList(),
                isVerified = false,
                joinedPlanIds = emptyList(),
            )
            userRepository.setCurrentUser(user)
            userPreferences.completeOnboarding(
                userId = userId,
                name = s.name,
                interestsJson = s.selectedInterests.joinToString(",") { it.name },
            )
            _state.update { it.copy(isLoading = false) }
            onDone()
        }
    }
}
