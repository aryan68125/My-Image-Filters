package com.example.myimagefilters.activities.filteredimage

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myimagefilters.R
import com.example.myimagefilters.activities.editimage.EditImageActivity
import com.example.myimagefilters.databinding.ActivityFilteredImageBinding

class FilteredImageActivity : AppCompatActivity() {

    private lateinit var fileUri: Uri
    private lateinit var binding : ActivityFilteredImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilteredImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        displayFilteredImage()
        setListeners()
    }

    private fun displayFilteredImage(){
        intent.getParcelableExtra<Uri>(EditImageActivity.KEY_FILTERED_IMAGE_URI)?.let{
            imageUri ->
            fileUri = imageUri
            binding.imageFilteredImage.setImageURI(imageUri)
        }
    }

    //This function handles the sharing of filtered images onto other apps like WhatsApp , Telegram , etc.....
    private fun setListeners()
    {
        binding.fabShare.setOnClickListener{
            //Using intent to share OR send the Filtered image onto another application
            with(Intent(Intent.ACTION_SEND)){
                putExtra(Intent.EXTRA_STREAM, fileUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                type = "image/*"
                startActivity(this)
            }
        }
        binding.imageBack.setOnClickListener {
            onBackPressed()
        }
    }
}