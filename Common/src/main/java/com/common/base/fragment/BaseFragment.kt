package com.common.base.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.android.ext.android.inject
import org.koin.core.module.Module
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment : Fragment(),
    CoroutineScope {

    val navigation: Navigation by inject()
    abstract fun initialize()

    open fun initialize(savedInstanceState: Bundle?){
        initialize()
    }

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    open fun getModule(): Module?{return null}



}