package com.byfrunze.amazingwallpaper.presentation.screens.search

import com.bumptech.glide.RequestManager
import com.byfrunze.amazingwallpaper.data.Wallpaper
import com.byfrunze.amazingwallpaper.network.IRepositories
import com.byfrunze.amazingwallpaper.presentation.base.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchViewModel
@Inject constructor() : BaseViewModel<SearchViewState, SearchAction, SearchEffect>() {

    private val compositeDisposable = CompositeDisposable()

    init {
        viewState =
            SearchViewState(
                wallpaperStatus = WallpaperStatus.Loading,
                data = emptyList()
            )
    }

    private val API_KEY = "8304bf2e6183717d7caa61fe4ba68da2"

    @Inject
    lateinit var iRepositories: IRepositories

    @Inject
    lateinit var glide: RequestManager


    override fun obtainEvent(viewEvent: SearchEffect) {
        when (viewEvent) {
            is SearchEffect.ScreenShow -> {
                initialWallpaper(term = viewEvent.term)
            }
            is SearchEffect.LoadMoreWallpaper -> loadMoreWallpaper(
                page = viewEvent.page,
                term = viewEvent.term
            )
        }
    }

    private fun loadMoreWallpaper(page: Int, term: String) {
        viewState = viewState.copy(wallpaperStatus = WallpaperStatus.Loading)
        val disposable =
            iRepositories.getSearch(
                auth = API_KEY,
                page = page,
                term = term
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    cacheWallpaper(wallpapers = it.wallpapers)

                    viewState = viewState.copy(
                        wallpaperStatus = WallpaperStatus.AddMoreWallpaper
                    )

                }, {
                    viewAction = SearchAction.ShowError(error = it.localizedMessage)
                })
        compositeDisposable.add(disposable)
    }

    private fun initialWallpaper(term: String) {
        viewState = viewState.copy(wallpaperStatus = WallpaperStatus.Loading)
        val disposable = iRepositories.getSearch(
            auth = API_KEY,
            term = term
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .debounce(300, TimeUnit.MILLISECONDS)
            .subscribe({
                cacheWallpaper(wallpapers = it.wallpapers)
                viewState = viewState.copy(wallpaperStatus = WallpaperStatus.Success)

            }, {
                viewAction = SearchAction.ShowError(error = it.localizedMessage)
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

    }
}