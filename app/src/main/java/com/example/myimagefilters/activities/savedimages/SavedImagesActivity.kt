package com.example.myimagefilters.activities.savedimages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.FileProvider
import com.example.myimagefilters.R
import com.example.myimagefilters.activities.editimage.EditImageActivity
import com.example.myimagefilters.activities.filteredimage.FilteredImageActivity
import com.example.myimagefilters.adapters.SavedImagesAdapter
import com.example.myimagefilters.databinding.ActivitySavedImagesBinding
import com.example.myimagefilters.listeners.SavedImageListener
import com.example.myimagefilters.utilities.displayToast
import com.example.myimagefilters.viewmodels.SavedImagesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class SavedImagesActivity : AppCompatActivity() , SavedImageListener{

    private lateinit var binding : ActivitySavedImagesBinding
    private val viewModel: SavedImagesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupObserver()
        setListeners()
        viewModel.loadSavedImages()
    }

    private fun setupObserver(){
        viewModel.savedImagesUiState.observe(this,{
            val savedImagesDataState = it?: return@observe
            binding.savedImagesProgressBar.visibility = if (savedImagesDataState.isLoading) View.VISIBLE else View.GONE
            savedImagesDataState.savedImages?.let{
                savedImages->
                //displayToast("${savedImages.size} images loaded")
                SavedImagesAdapter(savedImages, this).also {
                    adapter->
                    with(binding.savedImagesRecyclerView){
                        this.adapter = adapter
                        visibility = View.VISIBLE

                    }
                }

            } ?: run{
                savedImagesDataState.error?.let{
                    error->
                    displayToast(error)
                }
            }
        })
    }

    override fun onImageClicked(file: File) {
        val fileUri = FileProvider.getUriForFile(applicationContext,"${packageName}.provider", file)
        Intent(applicationContext, FilteredImageActivity::class.java).also{
            filteredImageIntent->
            filteredImageIntent.putExtra(EditImageActivity.KEY_FILTERED_IMAGE_URI,fileUri)
            startActivity(filteredImageIntent)
        }
    }

    private fun setListeners(){
        binding.imageBack.setOnClickListener{
            onBackPressed()
        }
    }
}