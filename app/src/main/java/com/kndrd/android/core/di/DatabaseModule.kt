package com.kndrd.android.core.di

import android.content.Context
import androidx.room.Room
import com.kndrd.android.core.data.local.KndrdDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): KndrdDatabase =
        Room.databaseBuilder(context, KndrdDatabase::class.java, "kndrd.db").build()

    @Provides
    fun providePlanDao(db: KndrdDatabase) = db.planDao()

    @Provides
    fun provideUserDao(db: KndrdDatabase) = db.userDao()

    @Provides
    fun provideMessageDao(db: KndrdDatabase) = db.messageDao()

    @Provides
    fun provideForumPostDao(db: KndrdDatabase) = db.forumPostDao()
}
