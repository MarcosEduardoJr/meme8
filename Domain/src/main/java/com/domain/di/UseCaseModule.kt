package com.domain.di

import com.domain.usecase.google.GetGoogleMemeImagesUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { GetGoogleMemeImagesUseCase(get()) }
}