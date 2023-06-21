package com.common.base.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import kotlin.coroutines.CoroutineContext

abstract class CoreBaseActivity : AppCompatActivity(), CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    abstract fun initialize(savedInstanceSTate: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        injectFeature()
        super.onCreate(savedInstanceState)
        initialize(savedInstanceState)
    }

    open fun getModule(): List<Module>? = null

    private val loadFeature by lazy {
        getModule()?.let { modulesList ->
            loadKoinModules(modulesList)
            modulesList
        }
    }

    private fun injectFeature() = loadFeature
}