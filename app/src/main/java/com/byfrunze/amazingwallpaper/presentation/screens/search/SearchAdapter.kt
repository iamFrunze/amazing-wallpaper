package com.byfrunze.amazingwallpaper.presentation.screens.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.byfrunze.amazingwallpaper.R
import com.byfrunze.amazingwallpaper.data.Wallpaper
import kotlinx.android.synthetic.main.cell_main_screen.view.*
import javax.inject.Inject

interface ImagesAdapterClicks {
    fun onItemClick(model: Wallpaper, image: ImageView)
}

interface OnLoadImages {
    fun onLoad(load: Boolean)
}


class SearchAdapter : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private val arrayOfImages: MutableList<Wallpaper> = ArrayList()


    var imagesAdapterClicks: ImagesAdapterClicks? = null
    var loadImages: OnLoadImages? = null

    var callback = false

    @Inject
    lateinit var glide: RequestManager

    fun initArray(arrayOfImages: List<Wallpaper>) {
        this.arrayOfImages.clear()
        this.arrayOfImages.addAll(arrayOfImages)
        notifyDataSetChanged()
    }

    fun addImages(arrayOfImages: List<Wallpaper>) {
        this.arrayOfImages.addAll(arrayOfImages)
        notifyItemInserted(this.arrayOfImages.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.cell_main_screen, parent, false)
        )


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == arrayOfImages.size - 1) {
            loadImages?.onLoad(true)
        } else loadImages?.onLoad(false)
        holder.bind(model = arrayOfImages[position])

    }

    override fun getItemCount(): Int = arrayOfImages.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image = itemView.image_view_cell

        fun bind(model: Wallpaper) {
            Glide.with(image)
                .load(model.url_image)
                .thumbnail(0.5f)
                .placeholder(R.drawable.placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .optionalCenterCrop()
                .into(image)
            image.setOnClickListener{
                imagesAdapterClicks?.onItemClick(model = model, image = image)
            }
        }
    }
}