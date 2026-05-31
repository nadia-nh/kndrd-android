package com.kndrd.android.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val age: Int,
    val bio: String,
    val photoUrl: String?,
    val neighborhood: String,
    val interestsJson: String,
    val isVerified: Boolean,
    val joinedPlanIdsJson: String,
)
