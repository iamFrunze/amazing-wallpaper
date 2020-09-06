package com.byfrunze.amazingwallpaper.di.modules


import com.byfrunze.amazingwallpaper.presentation.screens.main.MainFragment
import com.byfrunze.amazingwallpaper.presentation.screens.setup.SetupFragment
import com.byfrunze.amazingwallpaper.presentation.screens.tragetcategory.TargetCategoryFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ScreenBindingModule {
    @ContributesAndroidInjector
    abstract fun mainFragment(): MainFragment

    @ContributesAndroidInjector
    abstract fun targetCategoryFragment(): TargetCategoryFragment

    @ContributesAndroidInjector
    abstract fun setupFragment(): SetupFragment
}