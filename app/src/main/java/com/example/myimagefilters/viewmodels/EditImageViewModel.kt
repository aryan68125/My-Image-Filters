package com.example.myimagefilters.viewmodels

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myimagefilters.data.ImageFilter
import com.example.myimagefilters.repositories.EditImageRepository
import com.example.myimagefilters.utilities.Coroutines

class EditImageViewModel(private val editImageRepository: EditImageRepository): ViewModel() {

    //region :: Prepare image Preview
    private val imagePreviewDataState = MutableLiveData<ImagePreviewDataState>()
    val imagePreviewUiState: LiveData<ImagePreviewDataState> get() = imagePreviewDataState

    fun prepareImagePreview(imageUri: Uri)
    {
        Coroutines.io{
            runCatching{
                emitImagePreviewUiState(true)
                editImageRepository.prepareImagePreview(imageUri)
            }.onSuccess {bitmap ->
                if(bitmap!=null)
                {
                    emitImagePreviewUiState(bitmap = bitmap)
                }
                else
                {
                    emitImagePreviewUiState(error = "Unable to prepare the image preview")
                }
            }.onFailure {
                emitImagePreviewUiState(error = it.message.toString())
            }
        }
    }

    private fun emitImagePreviewUiState(isLoading: Boolean = false, bitmap:Bitmap? = null, error: String? = null)
    {
       val dataState = ImagePreviewDataState(isLoading,bitmap, error)
        imagePreviewDataState.postValue(dataState)
    }

    data class ImagePreviewDataState(var isLoading: Boolean, val bitmap: Bitmap?, val error:String?)
    // endregion

    // region :: Load image Filters
    private val imageFiltersDataState = MutableLiveData<ImageFiltersDataState>()
    val imageFiltersUiState: LiveData<ImageFiltersDataState> get() = imageFiltersDataState

    fun loadImageFilters(originalImage:Bitmap){
        Coroutines.io{
            runCatching {
               editImageFiltersUiState(isLoading = true)
                editImageRepository.getImageFilters(getPreviewImage(originalImage))
            }.onSuccess {
                imageFilters ->
                editImageFiltersUiState(imageFilters = imageFilters)
            }.onFailure {
                editImageFiltersUiState(error = it.message.toString())
            }
        }
    }

    private fun getPreviewImage(originalImage : Bitmap): Bitmap
    {
        return runCatching {
            val previewWidth = 150
            val previewHeight = originalImage.height * previewWidth / originalImage.width
            Bitmap.createScaledBitmap(originalImage, previewWidth, previewHeight, false)
        }.getOrDefault(originalImage)
    }

    private fun editImageFiltersUiState(isLoading : Boolean = false, imageFilters : List<ImageFilter>? = null , error : String? = null)
    {
        val dataState = ImageFiltersDataState(isLoading , imageFilters , error)
        imageFiltersDataState.postValue(dataState)
    }

    data class ImageFiltersDataState( val isLoading: Boolean, val imageFilters : List<ImageFilter>?, val error: String? )
    //endregion

    //region :: Save the Filtered image
    private val saveFilteredImageDataState = MutableLiveData<SavedFilteredImageDataState>()
    val saveFilteredImageUiData : LiveData<SavedFilteredImageDataState> get() = saveFilteredImageDataState

    fun saveFilteredImage(filteredBitmap : Bitmap){
        Coroutines.io{
            runCatching{
                emitSaveFilteredImageUiState(isLoading =true)
                editImageRepository.saveFilteredImage(filteredBitmap)
            }.onSuccess{
                savedImageUri ->
                emitSaveFilteredImageUiState(uri = savedImageUri)
            }.onFailure{
                emitSaveFilteredImageUiState(error = it.message.toString())
            }
        }
    }

    private fun emitSaveFilteredImageUiState(isLoading : Boolean = false, uri : Uri? = null, error: String? = null ){
        val dataState = SavedFilteredImageDataState(isLoading, uri,error)
        saveFilteredImageDataState.postValue(dataState)
    }

       data class SavedFilteredImageDataState(val isLoading: Boolean, val uri: Uri?, val error : String?)
    //regionEnd
}