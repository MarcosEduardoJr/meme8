package com.splash.di

import com.splash.ui.viewmodel.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val splashViewModelModule = module(override = true){
    viewModel { SplashViewModel() }
}