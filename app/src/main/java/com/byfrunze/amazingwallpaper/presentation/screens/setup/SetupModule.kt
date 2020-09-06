package com.byfrunze.amazingwallpaper.presentation.screens.setup

import androidx.lifecycle.ViewModel
import com.byfrunze.amazingwallpaper.di.modules.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SetupModule {

    @Binds
    @IntoMap
    @ViewModelKey(SetupViewModel::class)
    internal abstract fun setupViewModel(viewModel: SetupViewModel): ViewModel
}