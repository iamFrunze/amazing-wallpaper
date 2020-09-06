package com.byfrunze.amazingwallpaper.presentation.viewstate

data class SetupState(
    val loadStatus: LoadStatus,
)

sealed class LoadStatus {
    object Loading : LoadStatus()
    object Success : LoadStatus()
    object Error : LoadStatus()
}