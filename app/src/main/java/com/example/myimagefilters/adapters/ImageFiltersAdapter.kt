package com.example.myimagefilters.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myimagefilters.R
import com.example.myimagefilters.data.ImageFilter
import com.example.myimagefilters.databinding.ItemContainerFilterBinding
import com.example.myimagefilters.listeners.ImageFilterListener

class ImageFiltersAdapter(private val imageFilters : List<ImageFilter>, private val imageFilterListener: ImageFilterListener): RecyclerView.Adapter<ImageFiltersAdapter.ImageFilterViewHolder>() {

    private var selectedFilterPosition = 0
    private var previouslySelectedPosition = 0

    /*
    ItemContainerFilterBinding is generated based on our item container layout for recycler view which is
    "item_container_filter.xml"
     */
    inner class ImageFilterViewHolder(val binding : ItemContainerFilterBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageFilterViewHolder {
        val binding = ItemContainerFilterBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ImageFilterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageFilterViewHolder, position: Int) {
        with(holder){
            with(imageFilters[position]){
                binding.imageFilterPreview.setImageBitmap(filterPreview)
                binding.textFilterName.text = name

                //setting up onClick Listsners on ImageFilterView container
                binding.root.setOnClickListener{
                    if(position != selectedFilterPosition){
                        imageFilterListener.onFilterSelected(this)
                        previouslySelectedPosition = selectedFilterPosition
                        selectedFilterPosition = position
                        with(this@ImageFiltersAdapter){
                            notifyItemChanged(previouslySelectedPosition,Unit)
                            notifyItemChanged(selectedFilterPosition,Unit)
                        }
                    }
                }
            }

            //setting up the color of the selected filter text color so that it's easier for the user to know which filter he just applied
            binding.textFilterName.setTextColor(ContextCompat.getColor(
                binding.textFilterName.context, if(selectedFilterPosition == position)
                    R.color.selectedFilter
            else
                R.color.white
            ))

        }
    }

    override fun getItemCount() = imageFilters.size
}