package com.kndrd.android.core.model

data class User(
    val id: String,
    val name: String,
    val age: Int,
    val bio: String,
    val photoUrl: String?,
    val neighborhood: String,
    val interests: List<Interest>,
    val isVerified: Boolean,
    val joinedPlanIds: List<String>,
)
