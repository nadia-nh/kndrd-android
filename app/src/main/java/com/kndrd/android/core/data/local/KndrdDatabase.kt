package com.kndrd.android.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kndrd.android.core.data.local.dao.ForumPostDao
import com.kndrd.android.core.data.local.dao.MessageDao
import com.kndrd.android.core.data.local.dao.PlanDao
import com.kndrd.android.core.data.local.dao.UserDao
import com.kndrd.android.core.data.local.entity.ForumPostEntity
import com.kndrd.android.core.data.local.entity.MessageEntity
import com.kndrd.android.core.data.local.entity.PlanEntity
import com.kndrd.android.core.data.local.entity.UserEntity

@Database(
    entities = [PlanEntity::class, UserEntity::class, MessageEntity::class, ForumPostEntity::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(KndrdTypeConverters::class)
abstract class KndrdDatabase : RoomDatabase() {
    abstract fun planDao(): PlanDao
    abstract fun userDao(): UserDao
    abstract fun messageDao(): MessageDao
    abstract fun forumPostDao(): ForumPostDao
}
