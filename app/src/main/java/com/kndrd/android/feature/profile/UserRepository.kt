package com.kndrd.android.feature.profile

import com.kndrd.android.core.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun observeCurrentUser(): Flow<User?>
    suspend fun updateProfile(user: User): Result<User>
    suspend fun getUser(id: String): User?
    fun setCurrentUser(user: User)
}
