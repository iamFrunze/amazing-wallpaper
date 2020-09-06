package com.byfrunze.amazingwallpaper.presentation.viewstate

sealed class SideEffect {
    class ScreenShow(val term: String): SideEffect()
    class LoadMoreWallpaper(val page: Int, val term: String) : SideEffect()
}