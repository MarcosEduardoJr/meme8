package com.service.endpoint

import com.model.GoogleResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleEndpoint {
    //https://www.googleapis.com/customsearch/v1?key=AIzaSyAtmFkU4bRssmeZne8hl8Gybp7c6ucnL0E&cx=d555aa8bb6c474fd5&q=meme&searchType=image&sort=date
    @GET("customsearch/v1?key=AIzaSyAtmFkU4bRssmeZne8hl8Gybp7c6ucnL0E&cx=d555aa8bb6c474fd5&q=meme&searchType=image&sort=date&lr=lang_pt&num=10&sort=date-sdate")
    suspend fun getMemeImages() : GoogleResponse
}