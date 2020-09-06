package com.byfrunze.amazingwallpaper.presentation.viewstate


sealed class MainAction {
    class ShowError(val error: String): MainAction()
}