package com.byfrunze.amazingwallpaper.presentation.screens.tragetcategory

import androidx.lifecycle.ViewModel
import com.byfrunze.amazingwallpaper.di.modules.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class TargetCategoryModule {

    @Binds
    @IntoMap
    @ViewModelKey(TargetCategoryViewModel::class)
    internal abstract fun targetCategoryViewModel(viewModel: TargetCategoryViewModel): ViewModel
}