package com.byfrunze.amazingwallpaper.di.modules


import com.byfrunze.amazingwallpaper.presentation.screens.main.MainFragment
import com.byfrunze.amazingwallpaper.presentation.screens.search.SearchFragment
import com.byfrunze.amazingwallpaper.presentation.screens.setup.SetupFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ScreenBindingModule {
    @ContributesAndroidInjector
    abstract fun mainFragment(): MainFragment


    @ContributesAndroidInjector
    abstract fun setupFragment(): SetupFragment

    @ContributesAndroidInjector
    abstract fun searchFragment(): SearchFragment
}