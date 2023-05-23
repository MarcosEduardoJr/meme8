package com.service.di

import com.service.client.GoogleClient
import org.koin.dsl.module


val serviceModule = module {
    single { GoogleClient(get()) }
}