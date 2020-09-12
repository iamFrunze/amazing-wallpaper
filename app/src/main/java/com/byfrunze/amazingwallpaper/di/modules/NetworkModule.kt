package com.byfrunze.amazingwallpaper.di.modules

import android.content.Context
import com.bumptech.glide.Glide
import com.byfrunze.amazingwallpaper.di.AppScope
import com.byfrunze.amazingwallpaper.network.IRepositories
import dagger.Module
import dagger.Provides
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule {

    @Provides
    @AppScope
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://wall.alphacoders.com/")
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @AppScope
    fun provideApiInterface(retrofit: Retrofit): IRepositories =
        retrofit.create(IRepositories::class.java)

    @Provides
    @AppScope
    fun provideGlide(context: Context) = Glide.with(context)
}