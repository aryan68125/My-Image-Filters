package com.example.myimagefilters.dependencyinjection

import com.example.myimagefilters.viewmodels.EditImageViewModel
import com.example.myimagefilters.viewmodels.SavedImagesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        EditImageViewModel(editImageRepository = get())
    }
    viewModel{
        SavedImagesViewModel(savedImagesRepository = get())
    }
}