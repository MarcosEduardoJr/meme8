package com.dataset.google

import com.model.GoogleResponse
import com.service.client.GoogleClient
import com.service.endpoint.GoogleEndpoint

interface GoogleDataSet {
    suspend fun getMemeImages(
        q: String,
    ): GoogleResponse
}

class GoogleDataSetImpl(
    private val api: GoogleClient
) : GoogleDataSet {
    override suspend fun getMemeImages(q: String): GoogleResponse =
        api.googleEndpoint.getMemeImages()


    companion object {
        private val GOOGLE_KEY = "AIzaSyAtmFkU4bRssmeZne8hl8Gybp7c6ucnL0E"
        private val CX = "d555aa8bb6c474fd5"
    }
}