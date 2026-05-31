package com.kndrd.android.feature.feed

import com.kndrd.android.core.model.ChatRoom
import com.kndrd.android.core.model.Interest
import com.kndrd.android.core.model.Plan
import kotlinx.coroutines.flow.Flow

interface FeedRepository {
    fun observePlans(): Flow<List<Plan>>
    fun observePlansByInterest(interest: Interest): Flow<List<Plan>>
    suspend fun getPlan(id: String): Plan?
    suspend fun joinPlan(planId: String, userId: String): Result<ChatRoom>
    suspend fun createPlan(plan: Plan): Result<Plan>
    suspend fun refresh()
}
