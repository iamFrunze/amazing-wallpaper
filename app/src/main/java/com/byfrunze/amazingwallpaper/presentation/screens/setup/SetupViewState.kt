package com.byfrunze.amazingwallpaper.presentation.screens.setup

import android.content.Context

sealed class SetupAction {
    class ShowSnackBar(val text: String) : SetupAction()
}

sealed class SetupEffect {
    class Download(val drawableUrl: String, val context: Context) : SetupEffect()
    class SetupScreenLock(val drawableUrl: String, val context: Context) : SetupEffect()
    class SetupScreen(val drawableUrl: String, val context: Context) : SetupEffect()
}

data class SetupState(
    val loadStatus: LoadStatus,
)

sealed class LoadStatus {
    object Loading : LoadStatus()
    object Success : LoadStatus()
    object Error : LoadStatus()
}