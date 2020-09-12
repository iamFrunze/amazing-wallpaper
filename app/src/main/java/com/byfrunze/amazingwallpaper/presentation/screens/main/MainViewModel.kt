package com.byfrunze.amazingwallpaper.presentation.screens.main

import android.util.Log
import com.bumptech.glide.RequestManager
import com.byfrunze.amazingwallpaper.data.Wallpaper
import com.byfrunze.amazingwallpaper.network.IRepositories
import com.byfrunze.amazingwallpaper.presentation.base.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel
@Inject constructor() : BaseViewModel<MainViewState, MainAction, SideEffect>() {

    private val compositeDisposable = CompositeDisposable()

    init {
        viewState =
            MainViewState(
                wallpaperStatus = WallpaperStatus.Loading,
                data = emptyList()
            )
    }

    private val API_KEY = "8304bf2e6183717d7caa61fe4ba68da2"

    @Inject
    lateinit var iRepositories: IRepositories

    @Inject
    lateinit var glide: RequestManager


    private fun initialWallpaper(id: String) {
        viewState = viewState.copy(wallpaperStatus = WallpaperStatus.Loading)
        val disposable = iRepositories.getCategories(
            auth = API_KEY,
            id = id
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                cacheWallpaper(wallpapers = it.wallpapers)
                viewState = viewState.copy(wallpaperStatus = WallpaperStatus.Success)

            }, {
                viewAction = MainAction.ShowError(error = it.localizedMessage)

            })

        compositeDisposable.add(disposable)
    }

    private fun cacheWallpaper(wallpapers: List<Wallpaper>) {
        for (wallpaper in wallpapers) {
            glide.downloadOnly()
                .load(wallpaper.url_image)
                .preload()

        }
        viewState = viewState.copy(data = wallpapers)
        Log.i("RX", "KST")

    }


    private fun loadMoreWallpaper(page: Int, id: String) {
        viewState = viewState.copy(wallpaperStatus = WallpaperStatus.Loading)
        val disposable =
            iRepositories.getCategories(
                auth = API_KEY,
                page = page,
                id = id
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    cacheWallpaper(wallpapers = it.wallpapers)

                    viewState = viewState.copy(
                        wallpaperStatus = WallpaperStatus.AddMoreWallpaper
                    )

                }, {
                    viewAction = MainAction.ShowError(error = it.localizedMessage)
                })
        compositeDisposable.add(disposable)
    }


    override fun obtainEvent(viewEvent: SideEffect) {
        when (viewEvent) {
            is SideEffect.ScreenShow -> {
                initialWallpaper(id = viewEvent.id)
            }
            is SideEffect.LoadMoreWallpaper -> loadMoreWallpaper(
                page = viewEvent.page,
                id = viewEvent.id
            )
        }
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}

