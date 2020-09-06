package com.byfrunze.amazingwallpaper.presentation.screens.main

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.byfrunze.amazingwallpaper.R
import com.byfrunze.amazingwallpaper.data.Wallpaper
import kotlinx.android.synthetic.main.cell_main_screen.view.*

interface ImagesAdapterClicks {
    fun onItemClick(model: Wallpaper)
}

interface OnLoadImages {
    fun onLoad(load: Boolean)
}


class MainAdapter : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private val arrayOfImages: MutableList<Wallpaper> = ArrayList()
    var imagesAdapterClicks: ImagesAdapterClicks? = null
    var loadImages: OnLoadImages? = null


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
        if (position > arrayOfImages.size - 4) {
            loadImages?.onLoad(true)
        } else loadImages?.onLoad(false)
        holder.bind(model = arrayOfImages[position])
        holder.itemView.setOnClickListener { imagesAdapterClicks?.onItemClick(model = arrayOfImages[position]) }

    }

    override fun getItemCount(): Int = arrayOfImages.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image = itemView.image_view_cell

        fun bind(model: Wallpaper) {
            Log.i("TAG", "adapter $adapterPosition")
            Glide.with(itemView)
                .load(model.url_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.error_placeholder)
                .placeholder(R.drawable.preload)
                .transition(DrawableTransitionOptions.withCrossFade())
                //.centerCrop()
                .into(image)
        }
    }
}