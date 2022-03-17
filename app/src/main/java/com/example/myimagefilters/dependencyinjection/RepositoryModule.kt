package com.example.myimagefilters.dependencyinjection

import com.example.myimagefilters.repositories.EditImageRepository
import com.example.myimagefilters.repositories.EditImageRepositoryImpl
import com.example.myimagefilters.repositories.SavedImagesRepository
import com.example.myimagefilters.repositories.SavedImagesRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    factory<EditImageRepository>{
        EditImageRepositoryImpl(androidContext())
    }
    factory<SavedImagesRepository>{SavedImagesRepositoryImpl(androidContext())}
}