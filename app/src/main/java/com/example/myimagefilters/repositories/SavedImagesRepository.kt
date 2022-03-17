package com.example.myimagefilters.repositories

import android.graphics.Bitmap
import java.io.File

interface SavedImagesRepository {
    suspend fun loadSavedImages(): List<Pair<File, Bitmap>>?
}