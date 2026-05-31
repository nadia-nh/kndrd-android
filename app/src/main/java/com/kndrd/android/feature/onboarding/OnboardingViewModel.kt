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

    fun continueAsGuest(onDone: () -> Unit) {
        // Set up the in-memory user synchronously so the rest of the app sees a valid
        // current user before any recomposition fires.
        val user = User(
            id = "guest",
            name = "Guest",
            age = 0,
            bio = "",
            photoUrl = null,
            neighborhood = "New York City",
            interests = listOf(Interest.COFFEE, Interest.FOOD, Interest.OUTDOORS),
            isVerified = false,
            joinedPlanIds = emptyList(),
        )
        userRepository.setCurrentUser(user)

        // Navigate immediately — before DataStore write, so the MainActivity
        // recomposition triggered by the DataStore emission arrives after the
        // NavController has already moved to the main graph.
        onDone()

        // Persist in the background so next launch also skips onboarding.
        viewModelScope.launch {
            userPreferences.completeOnboarding(
                userId = "guest",
                name = "Guest",
                interestsJson = "COFFEE,FOOD,OUTDOORS",
            )
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
