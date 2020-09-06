package com.byfrunze.amazingwallpaper.di.modules

import com.byfrunze.amazingwallpaper.presentation.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {
    @ContributesAndroidInjector
    abstract fun mainActivity(): MainActivity
}