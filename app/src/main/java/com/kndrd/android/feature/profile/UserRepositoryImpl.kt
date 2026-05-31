package com.kndrd.android.feature.profile

import com.kndrd.android.core.model.Interest
import com.kndrd.android.core.model.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor() : UserRepository {

    private val _currentUser = MutableStateFlow<User?>(seedCurrentUser())

    override fun observeCurrentUser(): Flow<User?> = _currentUser

    override suspend fun updateProfile(user: User): Result<User> {
        delay(400)
        _currentUser.value = user
        return Result.success(user)
    }

    override suspend fun getUser(id: String): User? {
        return if (id == _currentUser.value?.id) _currentUser.value else null
    }

    override fun setCurrentUser(user: User) {
        _currentUser.value = user
    }
}

private fun seedCurrentUser() = User(
    id = "user-1",
    name = "Alex",
    age = 28,
    bio = "Software engineer by day, amateur chef by night. New to Brooklyn and always down for an adventure.",
    photoUrl = null,
    neighborhood = "Williamsburg, Brooklyn",
    interests = listOf(Interest.COFFEE, Interest.FOOD, Interest.OUTDOORS, Interest.BOOKS),
    isVerified = true,
    joinedPlanIds = emptyList(),
)
