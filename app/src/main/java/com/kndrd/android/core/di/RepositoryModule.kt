package com.kndrd.android.core.di

import com.kndrd.android.feature.chats.ChatRepository
import com.kndrd.android.feature.chats.ChatRepositoryImpl
import com.kndrd.android.feature.feed.FeedRepository
import com.kndrd.android.feature.feed.FeedRepositoryImpl
import com.kndrd.android.feature.forum.ForumRepository
import com.kndrd.android.feature.forum.ForumRepositoryImpl
import com.kndrd.android.feature.profile.UserRepository
import com.kndrd.android.feature.profile.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindFeedRepository(impl: FeedRepositoryImpl): FeedRepository

    @Binds
    @Singleton
    abstract fun bindChatRepository(impl: ChatRepositoryImpl): ChatRepository

    @Binds
    @Singleton
    abstract fun bindForumRepository(impl: ForumRepositoryImpl): ForumRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository
}
