package com.byfrunze.amazingwallpaper.presentation.viewstate

import android.content.Context

sealed class SetupEffect {
    class Download(val drawableUrl: String, val context: Context) : SetupEffect()
    class SetupScreenLock(val drawableUrl: String, val context: Context) : SetupEffect()
    class SetupScreen(val drawableUrl: String, val context: Context) : SetupEffect()
}