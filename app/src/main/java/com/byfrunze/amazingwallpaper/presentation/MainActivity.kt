package com.byfrunze.amazingwallpaper.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.byfrunze.amazingwallpaper.R
import dagger.android.AndroidInjection

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }
}