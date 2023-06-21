package com.model.nine_gag

data class Data(
    val nextCursor: String,
    val posts: List<Post>,
    val tags: List<TagX>
)