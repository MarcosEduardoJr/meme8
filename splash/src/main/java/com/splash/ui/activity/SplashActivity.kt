package com.splash.ui.activity

import com.common.base.activity.BaseActivity
import com.splash.R
import com.splash.di.splashViewModelModule
import org.koin.core.module.Module

class SplashActivity : BaseActivity() {

    override fun getLayoutId(): Int? = R.layout.activity_splash

    override fun getModule(): List<Module> = listOf(splashViewModelModule)
}