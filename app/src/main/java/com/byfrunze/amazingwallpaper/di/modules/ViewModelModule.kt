package com.byfrunze.amazingwallpaper.di.modules

import androidx.lifecycle.ViewModelProvider
import com.byfrunze.amazingwallpaper.presentation.helpers.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(
        factory: ViewModelFactory
    ): ViewModelProvider.Factory
}