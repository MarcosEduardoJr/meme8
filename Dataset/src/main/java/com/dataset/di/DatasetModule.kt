package com.dataset.di

import com.dataset.google.GoogleDataSet
import com.dataset.google.GoogleDataSetImpl
import org.koin.dsl.module

val datasetModule = module {
    single<GoogleDataSet> { GoogleDataSetImpl(get()) }
}