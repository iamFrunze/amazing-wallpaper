package com.byfrunze.amazingwallpaper.di

import com.byfrunze.amazingwallpaper.Application
import com.byfrunze.amazingwallpaper.di.modules.ActivityBindingModule
import com.byfrunze.amazingwallpaper.di.modules.NetworkModule
import com.byfrunze.amazingwallpaper.di.modules.ScreenBindingModule
import com.byfrunze.amazingwallpaper.di.modules.ViewModelModule
import com.byfrunze.amazingwallpaper.presentation.screens.main.MainModule
import com.byfrunze.amazingwallpaper.presentation.screens.setup.SetupModule
import com.byfrunze.amazingwallpaper.presentation.screens.tragetcategory.TargetCategoryModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector

@Component(
    modules = [
        AndroidInjectionModule::class,
        ActivityBindingModule::class,
        ScreenBindingModule::class,
        ViewModelModule::class,
        MainModule::class,
        TargetCategoryModule::class,
        SetupModule::class,
        NetworkModule::class
    ]
)
@AppScope
interface AppComponent : AndroidInjector<Application> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}