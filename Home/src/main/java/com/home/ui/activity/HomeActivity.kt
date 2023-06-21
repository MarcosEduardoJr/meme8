package com.home.ui.activity

import android.os.Bundle
import com.common.base.activity.BaseActivity
import com.google.firebase.FirebaseApp
import com.home.R
import com.home.di.homeViewModel
import org.koin.core.module.Module

class HomeActivity :  BaseActivity() {

    override fun getLayoutId(): Int? = R.layout.activity_home

    override fun getModule(): List<Module> = listOf(homeViewModel)

    override fun initialize(savedInstanceSTate: Bundle?) {
        super.initialize(savedInstanceSTate)
    }
}