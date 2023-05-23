package com.repository.di

import com.repository.google.GoogleRepository
import com.repository.google.GoogleRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<GoogleRepository> { GoogleRepositoryImpl(get()) }
}