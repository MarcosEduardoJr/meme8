package com.meme8

import android.app.Application
import com.di.memeModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MemeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        buildLibraries()
    }

    private fun buildLibraries() {
        startKoin()
    }

    private fun startKoin() {
        startKoin {
            androidLogger()
            androidContext(this@MemeApplication)
            androidFileProperties()
            koin.loadModules(memeModules)
        }
    }

}