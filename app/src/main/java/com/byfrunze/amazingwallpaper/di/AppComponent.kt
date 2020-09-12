package com.byfrunze.amazingwallpaper.di

import android.content.Context
import com.byfrunze.amazingwallpaper.Application
import com.byfrunze.amazingwallpaper.di.modules.*
import com.byfrunze.amazingwallpaper.presentation.screens.main.MainModule
import com.byfrunze.amazingwallpaper.presentation.screens.search.SearchModule
import com.byfrunze.amazingwallpaper.presentation.screens.setup.SetupModule
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
        SetupModule::class,
        SearchModule::class,
        NetworkModule::class,
        ContextModule::class
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