package com.byfrunze.amazingwallpaper.presentation.screens.main

import com.byfrunze.amazingwallpaper.data.Wallpaper

sealed class MainAction {
    class ShowError(val error: String) : MainAction()
}

data class MainViewState(
    val wallpaperStatus: WallpaperStatus,
    val data: List<Wallpaper>?
)

sealed class WallpaperStatus {
    object Loading : WallpaperStatus()
    object Success : WallpaperStatus()
    object AddMoreWallpaper : WallpaperStatus()
}

sealed class SideEffect {
    class ScreenShow(val id: String) : SideEffect()
    class LoadMoreWallpaper(val page: Int, val id: String) : SideEffect()
}