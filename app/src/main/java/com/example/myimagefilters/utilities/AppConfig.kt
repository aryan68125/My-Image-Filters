package com.example.myimagefilters.utilities

import android.app.Application
import com.example.myimagefilters.dependencyinjection.repositoryModule
import com.example.myimagefilters.dependencyinjection.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

@Suppress("unused")
class AppConfig : Application() {

    override fun onCreate(){
        super.onCreate()
        startKoin{
            androidContext(this@AppConfig)
            modules(listOf(repositoryModule, viewModelModule))
        }
    }

}