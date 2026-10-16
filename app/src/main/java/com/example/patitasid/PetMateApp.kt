package com.example.patitasid

import android.app.Application
import com.example.patitasid.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PetMateApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PetMateApp)
            modules(appModules)
        }
    }
}
