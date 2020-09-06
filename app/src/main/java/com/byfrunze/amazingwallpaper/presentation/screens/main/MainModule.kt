package com.byfrunze.amazingwallpaper.presentation.screens.main

import androidx.lifecycle.ViewModel
import com.byfrunze.amazingwallpaper.di.modules.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun mainViewModel(viewModel: MainViewModel): ViewModel
}