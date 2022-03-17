package com.example.myimagefilters.listeners

import java.io.File

interface SavedImageListener {

    fun onImageClicked(file: File)
}