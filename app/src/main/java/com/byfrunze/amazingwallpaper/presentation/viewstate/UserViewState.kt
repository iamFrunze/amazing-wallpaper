package com.byfrunze.amazingwallpaper.presentation.viewstate

import com.byfrunze.amazingwallpaper.data.Wallpaper


data class UserViewState(
    val authStatus: AuthStatus,
    val flag: Int,
    val data: List<Wallpaper>?
)

sealed class AuthStatus {
    object Loading : AuthStatus()
    object Success : AuthStatus()
    object AddMoreWallpaper : AuthStatus()

    //    object  SearchWallpaper: AuthStatus()
//    object  SearchWallpaperMore: AuthStatus()
    object Error : AuthStatus()
}