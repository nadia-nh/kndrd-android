package com.kndrd.android.core.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.kndrd.android.core.data.local.entity.ForumPostEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ForumPostDao {

    @Query("SELECT * FROM forum_posts ORDER BY timestampMs DESC")
    fun observeAll(): Flow<List<ForumPostEntity>>

    @Upsert
    suspend fun upsertAll(posts: List<ForumPostEntity>)
}
