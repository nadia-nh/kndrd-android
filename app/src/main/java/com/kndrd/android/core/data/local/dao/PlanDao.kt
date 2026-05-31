package com.kndrd.android.core.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.kndrd.android.core.data.local.entity.PlanEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlanDao {

    @Query("SELECT * FROM plans ORDER BY dateTimeMs ASC")
    fun observeAll(): Flow<List<PlanEntity>>

    @Query("SELECT * FROM plans WHERE id = :id")
    suspend fun getById(id: String): PlanEntity?

    @Upsert
    suspend fun upsertAll(plans: List<PlanEntity>)

    @Query("DELETE FROM plans WHERE cachedAtMs < :before")
    suspend fun evictStale(before: Long)
}
