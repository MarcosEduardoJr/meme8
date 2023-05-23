package com.di

import com.dataset.di.datasetModule
import com.domain.di.useCaseModule
import com.repository.di.repositoryModule
import com.service.di.serviceModule
import org.koin.core.module.Module
import org.koin.dsl.module

//todo configuration project modules
val memeModule = module { }

val memeModules: List<Module> = mutableListOf(
    memeModule,
    useCaseModule,
    repositoryModule,
    datasetModule,
    serviceModule
)