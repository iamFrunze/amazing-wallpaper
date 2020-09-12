package com.byfrunze.amazingwallpaper.presentation.screens.search

import com.byfrunze.amazingwallpaper.data.Wallpaper

sealed class SearchAction {
    class ShowError(val error: String) : SearchAction()
}

data class SearchViewState(
    val wallpaperStatus: WallpaperStatus,
    val data: List<Wallpaper>?
)

sealed class WallpaperStatus {
    object Loading : WallpaperStatus()
    object Success : WallpaperStatus()
    object AddMoreWallpaper : WallpaperStatus()
}

sealed class SearchEffect {
    class ScreenShow(val term: String) : SearchEffect()
    class LoadMoreWallpaper(val page: Int, val term: String) : SearchEffect()
}