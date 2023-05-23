package com.home.ui.activity

import com.common.base.activity.BaseActivity
import com.home.R
import com.home.di.homeViewModel
import org.koin.core.module.Module

class HomeActivity :  BaseActivity() {

    override fun getLayoutId(): Int? = R.layout.activity_home

    override fun getModule(): List<Module> = listOf(homeViewModel)
}