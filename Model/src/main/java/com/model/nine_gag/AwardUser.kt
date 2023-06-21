package com.model.nine_gag

data class AwardUser(
    val about: String,
    val accountId: String,
    val activeTs: Int,
    val avatarUrl: String,
    val creationTs: Int,
    val emojiStatus: String,
    val fullName: String,
    val isActivePro: Boolean,
    val isActiveProPlus: Boolean,
    val isVerifiedAccount: Boolean,
    val preferences: Preferences,
    val profileUrl: String,
    val userId: String,
    val username: String
)