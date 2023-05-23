package com.domain.usecase.google

import com.domain.AbstractUseCase
import com.model.GoogleResponse
import com.repository.google.GoogleRepository

class GetGoogleMemeImagesUseCase(
    private val googleRepository: GoogleRepository
) : AbstractUseCase<String,GoogleResponse>(){
    override suspend fun execute(param: String): GoogleResponse =
         googleRepository.getMemeImages(param)

}