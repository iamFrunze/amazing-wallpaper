package com.byfrunze.amazingwallpaper.presentation.viewstate

sealed class SetupAction {
    class ShowSnackBar(val text: String) : SetupAction()
}