package com.byfrunze.amazingwallpaper.di.modules

import android.content.Context
import com.byfrunze.amazingwallpaper.di.AppScope
import dagger.Binds
import dagger.Module

@Module
abstract class ContextModule {

    @Binds
    @AppScope
    abstract fun provideAppContext(app: com.byfrunze.amazingwallpaper.Application): Context
}