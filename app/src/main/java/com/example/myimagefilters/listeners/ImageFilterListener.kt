package com.example.myimagefilters.listeners

import com.example.myimagefilters.data.ImageFilter

interface ImageFilterListener {

    //creating a Listener for filter selection process in the application
    fun onFilterSelected(imagefilter: ImageFilter)

}