package com.di

import org.koin.core.module.Module
import org.koin.dsl.module

//todo configuration project modules
val memeModule = module{ }

val memeModules: List<Module> = mutableListOf(
    memeModule,
)