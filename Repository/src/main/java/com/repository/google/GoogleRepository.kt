package com.repository.google

import com.dataset.google.GoogleDataSet
import com.model.GoogleResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleRepository {
    suspend fun getMemeImages(
        q: String,
    ): GoogleResponse
}

class GoogleRepositoryImpl(
    private val googleDataSet: GoogleDataSet
) : GoogleRepository {
    override suspend fun getMemeImages(q: String): GoogleResponse =
          googleDataSet.getMemeImages(q)

}