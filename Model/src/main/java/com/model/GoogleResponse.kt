package com.model

data class GoogleResponse(
    val context: Context?,
    var items: List<Item>?,
    val kind: String?,
    val queries: Queries?,
    val searchInformation: SearchInformation?,
    val url: Url?
)