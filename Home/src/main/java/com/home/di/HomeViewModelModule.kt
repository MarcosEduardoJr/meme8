package com.home.di

import com.home.ui.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeViewModel = module {
   viewModel { HomeViewModel(get()) }
}