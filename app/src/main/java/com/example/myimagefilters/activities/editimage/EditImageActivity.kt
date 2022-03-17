package com.example.myimagefilters.activities.editimage

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.example.myimagefilters.activities.filteredimage.FilteredImageActivity
import com.example.myimagefilters.activities.main.MainActivity
import com.example.myimagefilters.adapters.ImageFiltersAdapter
import com.example.myimagefilters.data.ImageFilter
import com.example.myimagefilters.databinding.ActivityEditImageBinding
import com.example.myimagefilters.listeners.ImageFilterListener
import com.example.myimagefilters.utilities.displayToast
import com.example.myimagefilters.utilities.show
import com.example.myimagefilters.viewmodels.EditImageViewModel
import jp.co.cyberagent.android.gpuimage.GPUImage

//you have to add this line of code manually
import org.koin.androidx.viewmodel.ext.android.viewModel


class EditImageActivity : AppCompatActivity(), ImageFilterListener {

    companion object {
        const val KEY_FILTERED_IMAGE_URI = "filteredImageUri"
    }

    private lateinit var binding: ActivityEditImageBinding

    //this line of code will only work if this is imported properly like the code sample below
    // import org.koin.androidx.viewmodel.ext.android.viewModel
    private val viewModel : EditImageViewModel by viewModel()
    private lateinit var gpuImage: GPUImage

    //image Bitmaps
    private lateinit var originalBitmap: Bitmap
    private val filteredBitmap = MutableLiveData<Bitmap>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        setupObservers()
        prepareImagePreview()
    }

    private fun setupObservers(){
        viewModel.imagePreviewUiState.observe(this, {
            val dataState = it ?: return@observe
            binding.previewProgressBar.visibility = if(dataState.isLoading) View.VISIBLE else View.GONE
            dataState.bitmap?.let{
                bitmap->
                //For the first time filtered image will be  =  original image
                originalBitmap = bitmap
                filteredBitmap.value = bitmap
                with(originalBitmap){
                    gpuImage.setImage(this)
                    binding.imagePreview.show()
                    viewModel.loadImageFilters(this)
                }

            }?: kotlin.run {
                dataState.error?.let{ error->
                    displayToast(error)
                }
            }
        })
        viewModel.imageFiltersUiState.observe(this,{
            val imageFiltersDataState = it?: return@observe
            binding.imageFiltersProgressBar.visibility = if(imageFiltersDataState.isLoading) View.VISIBLE else View.GONE

            imageFiltersDataState.imageFilters?.let{
                imageFilters->

                ImageFiltersAdapter(imageFilters, this).also{
                    adapter ->
                    binding.filtersRecyclerView.adapter = adapter
                }
                }?: kotlin.run {
                imageFiltersDataState.error?.let{
                    error ->
                    displayToast(error)
                }
            }
        })

        //setting up the logic what should happen after the user selects the filters
        filteredBitmap.observe(this, {
            bitmap->
            binding.imagePreview.setImageBitmap(bitmap)
        })

        viewModel.saveFilteredImageUiData.observe(this,{
            val saveFilteredImageDataState = it?: return@observe
            if(saveFilteredImageDataState.isLoading){
                binding.imageSave.visibility = View.GONE
                binding.savingProgressBar.visibility = View.VISIBLE
            }
            else{
                binding.imageSave.visibility = View.VISIBLE
                binding.savingProgressBar.visibility = View.GONE
            }
            saveFilteredImageDataState.uri?.let{
                savedImageUri ->
                Intent(applicationContext, FilteredImageActivity::class.java).also{ filteredImageIntent ->
                    filteredImageIntent.putExtra(KEY_FILTERED_IMAGE_URI, savedImageUri)
                    startActivity(filteredImageIntent)
                }
            } ?: kotlin.run {
                saveFilteredImageDataState.error?.let{error->
                    displayToast(error)
                }
            }
        })

    }

    private fun prepareImagePreview(){
        gpuImage = GPUImage(applicationContext)
        intent.getParcelableExtra<Uri>(MainActivity.KEY_IMAGE_URI)?.let { imageUri ->
            viewModel.prepareImagePreview(imageUri)
        }
    }

    private fun setListeners() {
        binding.imageBack.setOnClickListener{
            onBackPressed()
        }

        binding.imageSave.setOnClickListener{
            filteredBitmap.value?.let{
                bitmap->
                viewModel.saveFilteredImage(bitmap)
            }
        }

    /*
        This will show the original image when we long click the ImageView until we release click,
        So that we can see the difference between the original and our edited pictures
    */
        binding.imagePreview.setOnLongClickListener{
            binding.imagePreview.setImageBitmap(originalBitmap)
            return@setOnLongClickListener false
        }
        binding.imagePreview.setOnClickListener{
            binding.imagePreview.setImageBitmap(filteredBitmap.value)
        }
    }

    //Logic for applying the filters on the original image imported by the user
    override fun onFilterSelected(imageFilter: ImageFilter) {
        with(imageFilter){
            with(gpuImage){
                setFilter(filter)
                filteredBitmap.value = bitmapWithFilterApplied
            }
        }
    }
}