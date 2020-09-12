package com.byfrunze.amazingwallpaper.presentation.screens.search

import androidx.lifecycle.ViewModel
import com.byfrunze.amazingwallpaper.di.modules.ViewModelKey
import com.byfrunze.amazingwallpaper.presentation.screens.setup.SetupViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SearchModule {

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    internal abstract fun searchViewModel(viewModel: SearchViewModel): ViewModel
}