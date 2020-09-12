package com.byfrunze.amazingwallpaper.data

data class Categories(
    val categories: List<Category>,
    val success: Boolean
)

data class Category(
    val count: Int,
    val id: Int,
    val name: String,
    val url: String
)