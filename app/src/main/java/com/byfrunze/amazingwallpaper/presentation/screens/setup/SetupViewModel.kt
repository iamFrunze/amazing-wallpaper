package com.byfrunze.amazingwallpaper.presentation.screens.setup

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.view.Display
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.byfrunze.amazingwallpaper.presentation.base.BaseViewModel
import javax.inject.Inject
import kotlin.random.Random

class SetupViewModel
@Inject constructor() : BaseViewModel<SetupState, SetupAction, SetupEffect>() {

    init {
        viewState = SetupState(loadStatus = LoadStatus.Success)
    }

    override fun obtainEvent(viewEvent: SetupEffect) {
        when (viewEvent) {
            is SetupEffect.Download ->
                download(drawable = viewEvent.drawableUrl, context = viewEvent.context)

            is SetupEffect.SetupScreen ->
                setupScreen(url = viewEvent.drawableUrl, context = viewEvent.context)

            is SetupEffect.SetupScreenLock ->
                setupScreenLock(url = viewEvent.drawableUrl, context = viewEvent.context)
        }
    }

    private fun setupScreenLock(url: String, context: Context) {
        val metrics = DisplayMetrics()
        viewState = viewState.copy(loadStatus = LoadStatus.Loading)
        val wallpaperManager = WallpaperManager.getInstance(context)
        Glide.with(context).asBitmap()
            .load(url)
            .override(metrics.widthPixels, metrics.heightPixels)
            .optionalCenterCrop()
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    viewAction =
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            wallpaperManager.setBitmap(
                                resource,
                                null,
                                true,
                                WallpaperManager.FLAG_LOCK
                            )
                            viewState = viewState.copy(loadStatus = LoadStatus.Success)
                            SetupAction.ShowSnackBar("Изображение установленно на экран блокировки")

                        } else SetupAction.ShowSnackBar("Ваш телефон не поддерживает данную функцию")
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }

    private fun setupScreen(url: String, context: Context) {
        val metrics = DisplayMetrics()

        viewState = viewState.copy(loadStatus = LoadStatus.Loading)
        val wallpaperManager = WallpaperManager.getInstance(context)
        Glide.with(context).asBitmap()
            .load(url)
            .override(1080,1920)
            .centerCrop()
            .into(object : CustomTarget<Bitmap>() {

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        wallpaperManager.setBitmap(resource)
                    viewAction =
                        SetupAction.ShowSnackBar("Изображение установленно на рабочий стол")
                    viewState = viewState.copy(loadStatus = LoadStatus.Success)

                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }

    private fun download(drawable: String, context: Context) {
        viewState = viewState.copy(loadStatus = LoadStatus.Loading)
        Glide.with(context).asBitmap()
            .load(drawable)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val url = MediaStore.Images.Media.insertImage(
                        context.contentResolver,
                        resource,
                        "IB${Random.nextInt(120)}",
                        "IB${Random.nextInt(120)}"
                    )
                    viewAction = SetupAction.ShowSnackBar("Изображение сохранено в галерею")
                    viewState = viewState.copy(loadStatus = LoadStatus.Success)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }
}