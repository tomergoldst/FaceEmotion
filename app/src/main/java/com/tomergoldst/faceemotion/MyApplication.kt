package com.tomergoldst.faceemotion

import android.app.Application
import com.tomergoldst.faceemotion.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // init DI
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(appModules)
        }

    }
}